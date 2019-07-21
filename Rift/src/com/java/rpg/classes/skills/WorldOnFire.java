package com.java.rpg.classes.skills;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WorldOnFire extends Skill implements Listener {

    private Main main = Main.getInstance();

    private double damage = 125;
    private int range = 10;
    private int duration = 8;
    private int hp = 10;
    private int mana = 10;

    public WorldOnFire() {
        super("WorldOnFire", 50, 10 * 20, 0, 0, "%player% has conjured a burst of Flame!", "PASSIVE-CAST");
        setDescription("&bPassive\nCasting spells ignites yourself for 5 seconds.\nPlayers within " + range + " blocks are ignited when you are on fire.\n&bActive\nSurround yourself in a fiery aura for " + duration + " seconds.\nFor each nearby player on fire, regain" + hp + " health and " + mana + " mana.");
    }

    // Event listen fire damage, ignite nearby
    // On server startup loop through RPGPlayer passives list. Get skill from name in list and run passiveCast.
    // Check if player still has that passive, changing classes CLEAR PASSIVE.

    public void cast(Player p) {
        /*Location loc = p.getLocation();
        Vector vec = p.getLocation().getDirection();
        for (int i = 0; i < 200; i++) {
            loc.setX(loc.getX() + i);
            p.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
        }*/

        p.getLineOfSight(null, range).forEach(block -> block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1));
        p.getLineOfSight(null, range).forEach(block -> damageEntities(block, p));
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
    }

    public void igniteEntities(Player caster) {
        for (LivingEntity ent : caster.getLocation().getNearbyLivingEntities(1.5)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            ent.setFireTicks(100);
        }
    }

    public void damageEntities(Block b, Player caster) {
        for (LivingEntity ent : b.getLocation().getNearbyLivingEntities(1.5)) {
            if (ent instanceof ArmorStand) {
                continue;
            }
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party && !main.getPM().getParty(p).getPvp()) {
                    if (main.getPM().getParty(p).getPlayers().contains(caster)) {
                        continue;
                    }
                }
                if (p.equals(caster)) {
                    continue;
                }
            }
            ent.setKiller(caster);
            ent.damage(damage);
            ent.setFireTicks(60);
        }
    }

}
