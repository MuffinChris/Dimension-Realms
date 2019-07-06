package com.java.rpg.classes.skills;

import com.java.Main;
import com.java.rpg.classes.Skill;
import com.java.rpg.party.Party;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Flameburst extends Skill {

    private Main main = Main.getInstance();

    private double damage = 125;
    private int range = 10;

    public Flameburst() {
        super("Flameburst", 100, 140, 0, 5, "%player% has conjured a burst of Flame!");
        setDescription("Fire a burst of flame at your target within a range of " + range + " blocks,\ndealing " + damage + " damage and igniting them for 3 seconds.");
    }

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

    public void damageEntities(Block b, Player caster) {
        for (LivingEntity ent : b.getLocation().getNearbyLivingEntities(1.5)) {
            if (ent instanceof Player) {
                Player p = (Player) ent;
                if (main.getPM().getParty(p) instanceof Party) {
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
