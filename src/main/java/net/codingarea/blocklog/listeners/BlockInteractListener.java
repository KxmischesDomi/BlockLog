package net.codingarea.blocklog.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockInteractListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {

		if (event.getClickedBlock() == null || !event.getAction().name().contains("RIGHT")) {
			return;
		}

		// TODO ADD CHEST OPEN CHECK

	}

}