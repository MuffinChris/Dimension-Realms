package com.java;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.java.communication.ChatFunctions;
import com.java.communication.MsgCommand;
import com.java.communication.PlayerinfoListener;
import com.java.communication.playerinfoManager;
import com.java.essentials.*;
import com.java.holograms.EntityHealthBars;
import com.java.holograms.Hologram;
import com.java.rpg.Damage;
import com.java.rpg.DamageListener;
import com.java.rpg.DamageTypes;
import com.java.rpg.classes.*;
import com.java.rpg.classes.skills.Pyromancer.*;
import com.java.rpg.classes.skills.Pyromancer.Fireball;
import com.java.rpg.modifiers.Environmental;
import com.java.rpg.player.PlayerListener;
import de.slikey.effectlib.EffectManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.java.rpg.party.Party;
import com.java.rpg.party.PartyCommand;
import com.java.rpg.party.PartyManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

public class Main extends JavaPlugin {

    /*

    DIRECT LINE TODO LIST:

        WHEN THE TIME COMES, DO RELIABLESITE. SYS SETUP FEES MAKE FIRST MONTH MORE EXPENSIVE, MAY AS WELL BLOW IT ALL
        ON SOMETHING BETTER AND HOPE FOR DONOS. UPGRADE INEVITABLE (HOPEFULLY)

        0. Create a settings GUI
            - Add a setting for close Skillmenu on cast, or persist, or disable!
        1. Remedy particles for Pyromancer (less flame, more orange and red redstone)
        2. Create 2 new skills:
            - Blaze -> Gain Speed 3 + Leave a trail of fire + Leave a trail of flaming particles that deal magic damage
            - Meteor -> Fire a flaming meteor (Particle Sphere of Redstone + Flame Particles)
                        Meteor blasts targets away, deals damage, and ignites them.
            - Pyroclasm -> Increased speed, damage, oopmh. Slightly less spammable because Meteor.
        3. Create skillpoint system to level up skills
        4. Create skillpoint GUI to level up skills
        5. Add skill levels to Pyromancer
        6. Create map of HP modifiers and XP modifiers for each mob, and determine a base HP and XP equation.
        7. RPGPlayer improvement: Make all stats, pstrength, armor, mr, tied to a map that holds info on modifiers
        8. Create Hunter and Ninja class
        9. Improve skill castng, allow changing of skillbar slots and binding items
        10. Determine armor system and profs system.

     RIFT THEME IDEAS:
        Players can open a personal rift using a Rift Key or Rift Gem or Rift Stone
        The personal rift is an end world with no blocks, cancel all mob spawning, perhaps do custom biome?
        The personal rift needs to disable things like Wither and stuff.
        Personal rift = hide all players including self, and check if login world is Personal Rift area.
        Surrounded with black wool or concrete and barriers, grant them a 5x5 chunk of space to work in.

        Towns:
            - Anchors - "Anchor" points that allow people to teleport to using rift tech
            - Rift generator - Open a rift and enter it, can teleport to an anchor or random location
            - Terrain control and claiming needs to be done based off of a source block, ie. Nexus
            - Regenerator Core - Heal all damage done to blocks through explosions and mobs


        Rifts can open and spawn bosses
        Rifts can open for other things too...
        Blood Moon - Rifts spawn with red mobs, drops red essence or something of the sort.

    * Class ideas:
    * Build out of components for certain classes, like Warframe. Unlock em!
    * Alternate Resource Systems, like Rage, Shadow, etc, thematic!
    *
    */

    public String noperm = "&cNo permission!";

    public static Main getInstance() {
        return JavaPlugin.getPlugin(Main.class);
    }

    /*

    Damage Variables

     */

    public Map<Entity, Hologram> hpBars = new HashMap<>();
    public Map<Entity, Hologram> getHpBars() {
        return hpBars;
    }

    public void remHpBar() {
        new BukkitRunnable() {
            public void run() {
                List<Entity> remove = new ArrayList<>();
                for (Entity e : hpBars.keySet()) {
                    if (e instanceof LivingEntity) {
                        LivingEntity ent = (LivingEntity) e;
                        if (ent.isDead() || ent.getHealth() <= 0 || (ent instanceof Player && !((Player) ent).isOnline())) {
                            remove.add(e);
                        } else {
                            DecimalFormat dF = new DecimalFormat("#.##");
                            getHpBars().get(e).setText("&f" + dF.format(ent.getHealth()) + "&c❤");
                        }
                    } else {
                        remove.add(e);
                    }
                }
                for (Entity e : remove) {
                    getHolos().remove(hpBars.get(e));
                    hpBars.get(e).destroy();
                    hpBars.remove(e);
                }
            }
        }.runTaskTimer(this, 10L, 20L);
    }

    public void riseBars() {
        new BukkitRunnable() {
            public void run() {
                for (Hologram h : getHolos()) {
                    if (h.getType() == Hologram.HologramType.DAMAGE) {
                        ArmorStand stand = h.getStand();
                        stand.teleport(stand.getLocation().add(new Vector(0, 0.025, 0)));
                        h.incrementLifetime();
                        if (h.getLifetime() * 0.025 >= 1) {
                            h.destroy();
                        }
                    } else if (h.getType() == Hologram.HologramType.HOLOGRAM) {
                        h.center();
                        if (h.shouldRemove()) {
                            hpBars.remove(h.getEntity());
                            h.destroy();
                        }
                    }
                }
            }
        }.runTaskTimer(this, 10L, 1L);
    }

    public List<Hologram> holograms = new ArrayList<>();
    public List<Hologram> getHolos() {
        return holograms;
    }

    public DamageTypes dmg = new DamageTypes();

    public List<Damage> getDmg() {
        return dmg.getDamages();
    }

    /*
    *
    * PARTY VARIABLES
    *
    */

    public Map<Player, Party> invite = new HashMap<Player, Party>();
    public Map<Player, Party> getInvites() {
        return invite;
    }

    public Map<Player, Boolean> pchat = new HashMap<Player, Boolean>();
    public Map<Player, Boolean> getPChat() {
        return pchat;
    }

    private PartyManager pm;
    public PartyManager getPM() {
        return pm;
    }

    /*
     *
     * CLASS VARIABLES
     *
     */

    EffectManager em = new EffectManager(this);

    public EffectManager getEm() {
        return em;
    }

    public RPGPlayer getRP(Player p) {
        return getPC().get(p.getUniqueId());
    }

    public int getSkillLevel(Player p, String name) {
        if (getRP(p).getSkillLevels().containsKey(name)) {
            return getRP(p).getSkillLevels().get(name);
        }
        return -1;
    }

    private ClassManager cm;
    public ClassManager getCM() {
        return cm;
    }

    private Map<UUID, RPGPlayer> pc = new HashMap<>();
    public Map<UUID, RPGPlayer> getPC() {
        return pc;
    }

    public double getMana(Player pl) {
        UUID p = pl.getUniqueId();
        if (getPC().get(p) != null) {
            if (getPC().get(p).getPClass() instanceof PlayerClass) {
                return getPC().get(p).getCMana();
            }
            return getPC().get(p).getCMana();
        }
        return 0;
    }

    public void setMana(Player pl, double m) {
        UUID p = pl.getUniqueId();
        if (getPC().get(p) != null) {
            if (getPC().get(p).getPClass() instanceof PlayerClass) {
                getPC().get(p).setMana(m);
            }
        }
    }

    public static void sendHp(Player pl) {
        UUID p = pl.getUniqueId();
        if (getInstance().getPC().get(p) != null) {
            RPGPlayer player = getInstance().getPC().get(p);
            DecimalFormat dF = new DecimalFormat("#.##");
            double mr = player.getPClass().getCalcMR(player.getLevel());
            double armor = player.getPClass().getCalcArmor(player.getLevel());
            String mrper = Main.color("&b" + dF.format(100.0 * (1-(300.0/(300.0+mr)))) + "% MR");
            String amper = Main.color("&c" + dF.format(100.0 * (1-(300.0/(300.0+armor)))) + "% AM");
            pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color("&c" + dF.format(pl.getHealth()) + " HP   &b" + player.getPrettyCMana() + " M   &a" + player.getPrettyPercent() + "% XP   &e" + player.getLevel() + " LVL   " + amper + "   " + mrper)));
        }
    }

    public void hpPeriodic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    sendHp(p);
                }
            }
        }.runTaskTimer(this, 20L, 4L);
    }

    public void manaRegen() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    UUID p = pl.getUniqueId();
                    if (getPC().get(p) != null && getPC().get(p).getPClass() instanceof PlayerClass) {
                        int level = getPC().get(p).getLevel();
                        double mana = getMana(pl);
                        double maxmana = getPC().get(p).getPClass().getCalcMana(level);
                        if (mana < maxmana) {
                            if (!pl.isDead()) {
                                double manaGain = getPC().get(p).getPClass().getCalcManaRegen(level);
                                setMana(pl, Math.min(getMana(pl) + manaGain, maxmana));
                                sendHp(pl);
                            } else {
                                setMana(pl, 0);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 1L, 20L);
    }

    public void cooldownsPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  pl : Bukkit.getOnlinePlayers()) {
                    UUID p = pl.getUniqueId();
                    if (getPC().get(p) != null && getPC().get(p).getBoard() != null) {
                        //try {
                            getPC().get(p).refreshCooldowns();
                            getPC().get(p).getBoard().update();
                        //} catch (ConcurrentModificationException ex) {

                        //}
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);
    }

    public void passivesPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  pl : Bukkit.getOnlinePlayers()) {
                    getCM().passives(pl);
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }

    public void chatPeriodic() {
        new BukkitRunnable() {
            public void run() {
                for (Player  p : Bukkit.getOnlinePlayers()) {
                    ChatFunctions.updateName(p);
                    getRP(p).updateStats();
                }
            }
        }.runTaskTimerAsynchronously(this, 10L, 80L);
    }

    public void onEnable() {

        so("&bRIFT: &fEnabling Plugin!");

        setupChat();
        so("&bRIFT: &fHooked into Vault Chat!");

        protocolManager = ProtocolLibrary.getProtocolManager();
        so("&bRIFT&7: &fProtocolLib hooked!");

        setupPacketListeners();
        so("&bRIFT&7: &fProtocolLib Packet Listeners Enabled!");

        getCommand("party").setExecutor(new PartyCommand());
        getCommand("skill").setExecutor(new SkillCommand());
        getCommand("class").setExecutor(new ClassCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("inv").setExecutor(new InvseeCommand());
        getCommand("killall").setExecutor(new KillAllCommand());
        getCommand("gms").setExecutor(new GamemodeCommand());
        getCommand("gmc").setExecutor(new GamemodeCommand());
        getCommand("gmss").setExecutor(new GamemodeCommand());
        getCommand("lag").setExecutor(new LagCommand());
        getCommand("msg").setExecutor(new MsgCommand());
        getCommand("tell").setExecutor(new MsgCommand());
        getCommand("message").setExecutor(new MsgCommand());
        getCommand("r").setExecutor(new MsgCommand());
        getCommand("reply").setExecutor(new MsgCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("mana").setExecutor(new ManaCommand());
        getCommand("skills").setExecutor(new SkillsCommand());
        getCommand("cd").setExecutor(new CDCommand());
        getCommand("seen").setExecutor(new SeenCommand());
        getCommand("biome").setExecutor(new BiomeLevelCommand());

        getCommand("level").setExecutor(new LevelCommand());

        getCommand("dummy").setExecutor(new DummyCommand());
        so("&bRIFT: &fEnabled commands!");

        Bukkit.getPluginManager().registerEvents(new PartyCommand(), this);
        Bukkit.getPluginManager().registerEvents(new ClassManager(), this);
        Bukkit.getPluginManager().registerEvents(new ChatFunctions(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerinfoListener(), this);
        Bukkit.getPluginManager().registerEvents(new Environmental(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new DummyCommand(), this);
        Bukkit.getPluginManager().registerEvents(new Hologram(), this);
        Bukkit.getPluginManager().registerEvents(new SkillsCommand(), this);
        Bukkit.getPluginManager().registerEvents(new EntityHealthBars(), this);
        Bukkit.getPluginManager().registerEvents(new MobEXP(), this);
        Bukkit.getPluginManager().registerEvents(new AFKInvuln(), this);

        //Skills
        Bukkit.getPluginManager().registerEvents(new Skillcast(), this);

        Bukkit.getPluginManager().registerEvents(new Fireball(), this);
        Bukkit.getPluginManager().registerEvents(new MeteorShower(), this);
        Bukkit.getPluginManager().registerEvents(new WorldOnFire(), this);
        Bukkit.getPluginManager().registerEvents(new InfernoVault(), this);
        Bukkit.getPluginManager().registerEvents(new Pyroclasm(), this);
        so("&bRIFT: &fRegistered events!");

        pm = new PartyManager();
        cm = new ClassManager();
        hpPeriodic();
        manaRegen();
        chatPeriodic();
        passivesPeriodic();
        cooldownsPeriodic();
        remHpBar();
        riseBars();

        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e.getType() == EntityType.ARMOR_STAND) {
                    if (e.isCustomNameVisible() && e.getCustomName().contains("❤")) {
                        e.remove();
                    }
                    continue;
                }
                if (e instanceof LivingEntity && !(e instanceof Player)) {
                    if (!getHpBars().containsKey(e)) {
                        DecimalFormat dF = new DecimalFormat("#.##");
                        getHpBars().put(e, new Hologram(e, e.getLocation().add(new Vector(0, e.getHeight() - 0.2, 0)), "&f" + dF.format(((LivingEntity)e).getHealth()) + "&c❤", Hologram.HologramType.HOLOGRAM));
                    }
                }
            }
        }

        so("&bRIFT: &fSetup complete!");

    }

    public void onDisable() {

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.cancelTasks(this);

        for (Hologram h : holograms) {
            h.destroy();
        }

        List<String> projectiles = new ArrayList<>();
        projectiles.add("Fireball");
        projectiles.add("Meteor");

        for (World w : Bukkit.getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (e != null && e.getCustomName() != null && e.getCustomName() instanceof String && projectiles.contains(e.getCustomName())) {
                    e.remove();
                }
            }
        }

        so("&bRIFT: &fDisabling Plugin!");

    }

    /*
    *
    * Msg variables
    *
     */
    public playerinfoManager msg = new playerinfoManager();
    public playerinfoManager getMsg() {
        return msg;
    }

    /*
    *
    * Party methods.
    *
     */

    public void partyStartup() {
        new BukkitRunnable() {
            @Override
            public void run() {
                getPM().cleanParties();
            }
        }.runTaskTimerAsynchronously(this, 1L, 200L);
    }

    /*
    *
    * Chat methods
    *
     */

    private Chat chat;

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    /*
    *
    * Protocol Lib stuff
    *
     */

    private ProtocolManager protocolManager;
    public void setupPacketListeners() {
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
                    if (event.getPacket().getNewParticles().getValues().get(0).getParticle().name().contains("DAMAGE_INDICATOR")) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

    public Chat getChat() {
        return chat;
    }

    public static void so(String s) {
        Bukkit.getServer().getConsoleSender().sendMessage(color(s));
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void msg(Player p, String text) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

}
