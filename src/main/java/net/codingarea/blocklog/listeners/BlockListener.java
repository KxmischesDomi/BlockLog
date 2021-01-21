package net.codingarea.blocklog.listeners;

import net.codingarea.blocklog.BlockActionType;
import net.codingarea.blocklog.BlockLogManager;
import net.codingarea.blocklog.utils.BlockLogUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static net.codingarea.blocklog.BlockLogPlugin.getInstance;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) throws SQLException, ParseException {
		Player player = event.getPlayer();
		if (player.getInventory().getItemInMainHand().isSimilar(getBlockLogManager().getLogStick())) {
			event.setCancelled(true);
			ResultSet rs = getBlockLogManager().getBlockData(event.getBlock().getLocation());
			if (rs == null || rs.isClosed()) {
				player.sendMessage("§cNo Data about this block");
				return;
			}
			player.sendMessage(BlockLogUtils.formatBlockData(rs));
			rs.close();
		} else {
			getBlockLogManager().saveBlockAction(event.getPlayer().getUniqueId(), BlockActionType.DESTROYED, event.getBlock().getType(), event.getBlock().getLocation());
		}

	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) throws SQLException, ParseException {
		Player player = event.getPlayer();
		getBlockLogManager().saveBlockAction(event.getPlayer().getUniqueId(), BlockActionType.PLACED, event.getBlock().getType(), event.getBlock().getLocation());
	}

	private BlockLogManager getBlockLogManager() {
		return getInstance().getBlockLogManager();
	}

}
