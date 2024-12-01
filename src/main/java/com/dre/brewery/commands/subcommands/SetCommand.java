package com.dre.brewery.commands.subcommands;

import com.dre.brewery.BPlayer;
import com.dre.brewery.BreweryPlugin;
import com.dre.brewery.commands.SubCommand;
import com.dre.brewery.configuration.files.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SetCommand implements SubCommand {

	@Override
	public void execute(BreweryPlugin breweryPlugin, Lang lang, CommandSender sender, String label, String[] args) {

		if (args.length < 3) {
			lang.sendEntry(sender, "Etc_Usage");
			lang.sendEntry(sender, "Help_Set");
			return;
		}

		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			lang.sendEntry(sender, "Error_NoPlayer", args[1]);
		} else {

			int drunkenness = 0;
			try {
				drunkenness = Integer.parseInt(args[2]);
				if (drunkenness > 100) drunkenness = 100;
				if (drunkenness < 0) drunkenness = 0;
			} catch (NumberFormatException e) {
				//lang.sendEntry(sender, "Error_InvalidDrunkenness"));
			}

			int quality = 10;
			if (args.length > 3) {
				try {
					quality = Integer.parseInt(args[3]);
					if (quality > 10) quality = 10;
					if (quality < 0) quality = 0;
				} catch (NumberFormatException e) {
					//lang.sendEntry(sender, "Error_InvalidQuality"));
				}
			}

			BPlayer bPlayer = BPlayer.get(Bukkit.getOfflinePlayer(target.getUniqueId()));
			if (bPlayer == null) bPlayer = BPlayer.addPlayer(Bukkit.getOfflinePlayer(target.getUniqueId()));

			bPlayer.setDrunkeness(drunkenness);
			bPlayer.setQuality(quality * drunkenness);

			lang.sendEntry(sender, "CMD_Set", args[1], String.valueOf(drunkenness), String.valueOf(quality));

			// Stop long nausea effects when drunkenness is 0
			if (drunkenness == 0) target.removePotionEffect(PotionEffectType.CONFUSION);

		}

	}

	@Override
	public List<String> tabComplete(BreweryPlugin breweryPlugin, CommandSender sender, String label, String[] args) {
		return null;
	}

	@Override
	public String permission() {
		return "brewery.cmd.set";
	}

	@Override
	public boolean playerOnly() {
		return false;
	}

}
