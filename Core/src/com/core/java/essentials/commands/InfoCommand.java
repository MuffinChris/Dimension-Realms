package com.core.java.essentials.commands;

import com.core.java.Constants;
import com.core.java.rpgbase.player.Weapons;
import com.core.java.rpgbase.player.professions.PlayerProf;
import com.core.java.rpgbase.player.professions.Profession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.core.java.essentials.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class InfoCommand implements CommandExecutor, Listener {

	private Main main = Main.getInstance();

	@EventHandler
	public void commandProcess (PlayerCommandPreprocessEvent e) {
		if (e.getMessage().replaceAll(" ", "").equalsIgnoreCase("/info")) {
			e.getPlayer().performCommand("information");
			e.setCancelled(true);
		} else if (e.getMessage().contains(" ") && e.getMessage().split(" ")[0].equalsIgnoreCase("/info") && e.getMessage().split(" ").length >= 2) {
			e.getPlayer().performCommand("information " + e.getMessage().split(" ")[1]);
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void noClick (InventoryClickEvent e) {
		if (e.getView().getTitle().contains("INFORMATION")) {
			e.setCancelled(true);
		}
	}

	public void sendInfoInv(Player v, Player p, boolean thirdparty) {
		String name = "&e&lYOUR INFORMATION";
		if (thirdparty) {
			name = "&e&l" + p.getName() + "'s INFORMATION";
		}
		Inventory playerInv = Bukkit.createInventory(null, 27, Main.color(name));

		//lore.add(Main.color("&8»"));


		PlayerProf pprof = main.getProf().get(p.getUniqueId());
		DecimalFormat df = new DecimalFormat("#");
		DecimalFormat dF = new DecimalFormat("#.##");

		ArrayList<String> lore;
		ItemStack sp = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta spMeta = (SkullMeta) sp.getItemMeta();
		spMeta.setOwningPlayer(p);
		spMeta.setDisplayName(Main.color("&ePlayer: &f" + p.getName()));
		lore = new ArrayList<String>();
		spMeta.setLore(lore);
		sp.setItemMeta(spMeta);
		playerInv.setItem(4, sp);

		ItemStack classes = new ItemStack(Material.IRON_SWORD);
		ItemMeta classesMeta = classes.getItemMeta();
		classesMeta.setDisplayName(Main.color("&6Classes"));
		lore = new ArrayList<String>();
		lore.add("");
		lore.add(Main.color("&8» &eClass: &f" + "not implemented"));
		lore.add(Main.color("&8» &eLevel: &f" + main.getLevel(p) + "&8/&f" + Constants.maxLevel));
		lore.add(Main.color("&8» &eExp: &f" + df.format(main.getExp(p)) + "&8/&f" + df.format(main.getExpMax(p)) + " &8(&e" + dF.format((main.getExp(p) * 100.0D) / (1.0D * main.getExpMax(p))) + "%&8)"));
		lore.add("");
		classesMeta.setLore(lore);
		classesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		classes.setItemMeta(classesMeta);
		playerInv.setItem(11, classes);

		ItemStack profs = new ItemStack(Material.GOLDEN_SHOVEL);
		ItemMeta profsMeta = profs.getItemMeta();
		profsMeta.setDisplayName(Main.color("&6Professions"));
		lore = new ArrayList<>();
		lore.add("");
		for (Profession prof : pprof.getProfs()) {
			lore.add(Main.color("&8» &e" + prof.getName() + ": &f" + prof.getLevel() + "&8/&f" + Constants.maxLevelProf + " &8(&e" + prof.getPercent() + "&8)"));
		}
		lore.add("");
		profsMeta.setLore(lore);
		profsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		profs.setItemMeta(profsMeta);
		playerInv.setItem(12, profs);

		ItemStack stats = new ItemStack(Material.NETHER_STAR);
		ItemMeta statsMeta = stats.getItemMeta();
		statsMeta.setDisplayName(Main.color("&6Combat Stats"));
		lore = new ArrayList<>();
		lore.add("");
		lore.add(Main.color("&8» &cHP: &f" + df.format(p.getHealth()) + "&8/&f" + df.format(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())));
		lore.add(Main.color("&8» &4Attack Damage: &f" + dF.format(Weapons.getWeaponAttackDamage(p))));
		lore.add(Main.color("&8» &bMana: &f" + main.getMana(p) + "&8/&f" + main.getManaMap().get(p.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(p.getUniqueId())) + "/s&8)"));
		lore.add("");
		statsMeta.setLore(lore);
		stats.setItemMeta(statsMeta);
		playerInv.setItem(13, stats);

		ItemStack guild = new ItemStack(Material.BEACON);
		ItemMeta guildMeta = guild.getItemMeta();
		guildMeta.setDisplayName(Main.color("&6Guilds"));
		lore = new ArrayList<>();
		lore.add("");
		lore.add(Main.color("&8» &eGuild: &f" + "Not Implemented"));
		lore.add("");
		guildMeta.setLore(lore);
		guild.setItemMeta(guildMeta);
		playerInv.setItem(14, guild);

		ItemStack general = new ItemStack(Material.BOOK);
		ItemMeta generalMeta = general.getItemMeta();
		generalMeta.setDisplayName(Main.color("&6General Info"));
		lore = new ArrayList<>();
		File pFile = new File("plugins/Core/data/" + p.getUniqueId() + ".yml");
		FileConfiguration pData = YamlConfiguration.loadConfiguration(pFile);
		int kills = pData.getInt("Kills");
		int deaths = pData.getInt("Deaths");
		double kdr = (1.0 * kills) / (1.0 * deaths);
		if (deaths == 0) {
			kdr = 0.0;
		}
		lore.add("");
		lore.add(Main.color("&8» &cKDR: &f" + df.format(kdr) + " &8(&c" + kills + "&8/&c" + deaths + "&8)"));
		lore.add("");
		generalMeta.setLore(lore);
		general.setItemMeta(generalMeta);
		playerInv.setItem(15, general);

		ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta glassMeta = glass.getItemMeta();
		glassMeta.setDisplayName(Main.color(" "));
		glass.setItemMeta(glassMeta);
		for (int i = 0; i < 27; i++) {
			if (playerInv.getContents()[i] == null) {
				playerInv.setItem(i, glass);
			}
		}

		v.openInventory(playerInv);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				/*DecimalFormat dF = new DecimalFormat("#.##");
				DecimalFormat df = new DecimalFormat("#");
				int SPAD = Integer.valueOf(Main.getValue(p, "SPAD"));
				double ad = SPAD * Constants.ADPerSP * 100;
				ad = Double.valueOf(dF.format(ad));
				Main.msg(p, "");
				Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(p) + " &8/&f 100");
				Main.msg(p, "&8» &e&lEXP: &f" + df.format(main.getExp(p)) + " &8/&f " + df.format(main.getExpMax(p)));
				Main.msg(p, "&8» &b&lMANA: &f" + main.getManaMap().get(p.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(p.getUniqueId())) + "/s&8)");
				Main.msg(p, "&8» &4&lAD: &f+" + ad + "%" + "  &c&lHP: &f" + p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				Main.msg(p, "");*/
				sendInfoInv(p, p, false);
			} else if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					/*DecimalFormat dF = new DecimalFormat("#.##");
					DecimalFormat df = new DecimalFormat("#");
					int SPAD = Integer.valueOf(Main.getValue(t, "SPAD"));
					double ad = SPAD * Constants.ADPerSP * 100;
					ad = Double.valueOf(dF.format(ad));
					Main.msg(p, "");
					Main.msg(p, "&8» &e&lLEVEL: &f" + main.getLevel(t) + " &8/&f 100");
					Main.msg(p, "&8» &e&lEXP: &f" + df.format(main.getExp(t)) + " &8/&f " + df.format(main.getExpMax(t)));
					Main.msg(p, "&8» &b&lMANA: &f" + main.getManaMap().get(t.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(t.getUniqueId())) + "/s&8)");
					Main.msg(p, "&8» &4&lAD: &f+" + ad + "%" + "  &c&lHP: &f" + t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					Main.msg(p, "");*/
					sendInfoInv(p, t, true);
				} else {
					Main.msg(p, "&cInvalid Player.");
				}
			} else {
				Main.msg(p, "Usage: /info <player>");
			}
		} else {
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) instanceof Player) {
					Player t = Bukkit.getPlayer(args[0]);
					DecimalFormat dF = new DecimalFormat("#.##");
					DecimalFormat df = new DecimalFormat("#");
					int SPAD = Integer.valueOf(Main.getValue(t, "SPAD"));
					double ad = SPAD * Constants.ADPerSP * 100;
					ad = Double.valueOf(dF.format(ad));
					Main.so("");
					Main.so("&8» &e&lLEVEL: &f" + main.getLevel(t) + " &8/&f 100");
					Main.so("&8» &e&lEXP: &f" + df.format(main.getExp(t)) + " &8/&f " + df.format(main.getExpMax(t)));
					Main.so("&8» &b&lMANA: &f" + main.getManaMap().get(t.getUniqueId()) + " &8(&b" + (main.getManaRegenMap().get(t.getUniqueId())) + "/s&8)");
					Main.so("&8» &4&lAD: &f+" + ad + "%" + "  &c&lHP: &f" + t.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					Main.so("");
				} else {
					Main.so("&cInvalid Player");
				}
			} else {
				Main.so("Usage: /info <player>");
			}
		}
		return false;
	}
	
}
