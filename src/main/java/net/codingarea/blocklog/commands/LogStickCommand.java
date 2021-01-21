package net.codingarea.blocklog.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.codingarea.blocklog.BlockLogPlugin.getInstance;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class LogStickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = ((Player) sender);

		if (player.getInventory().firstEmpty() == -1) {
			player.sendMessage("§cDein Inventar ist voll");
			return false;
		}

		player.getInventory().addItem(getInstance().getBlockLogManager().getLogStick().clone());
		player.sendMessage("§aBlock Log Stick erhalten");

		return false;
	}

}