package net.codingarea.blocklog;

import net.codingarea.blocklog.commands.LogStickCommand;
import net.codingarea.blocklog.listeners.BlockListener;
import net.codingarea.engine.sql.LiteSQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockLogPlugin extends JavaPlugin {

	private static BlockLogPlugin instance;
	private LiteSQL sql;
	private BlockLogManager blockLogManager;

	@Override
	public void onLoad() {
		instance = this;
		connectLiteSQL();
		blockLogManager = new BlockLogManager(sql);
	}

	@Override
	public void onEnable() {
		getCommand("logstick").setExecutor(new LogStickCommand());
		Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
	}

	@Override
	public void onDisable() {
		sql.disconnectSafely();
	}

	private void connectLiteSQL() {
		try {
			sql = new LiteSQL("blocks.db");
			sql.executeUpdate("CREATE TABLE IF NOT EXISTS blocks(world VARCHAR, x INTEGER, y INTEGER, z INTEGER, actions VARCHAR);");
		} catch (Throwable throwables) {
			throwables.printStackTrace();
		}
	}

	@Nonnull
	@CheckReturnValue
	public BlockLogManager getBlockLogManager() {
		return blockLogManager;
	}

	@Nonnull
	@CheckReturnValue
	public static BlockLogPlugin getInstance() {
		return instance;
	}

}