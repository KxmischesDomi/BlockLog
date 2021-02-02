package net.codingarea.blocklog;

import net.codingarea.blocklog.utils.BlockLogUtils;
import net.codingarea.engine.sql.LiteSQL;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockLogManager {

	private final LiteSQL sql;
	private final ItemStack logStick = new ItemBuilder(Material.STICK).setDisplayName("§cLog-Stick").setLore("§cLeft click block to see its log").setUnbreakable().build();
	private final ItemStack interactLogStick = new ItemBuilder(Material.STICK).setDisplayName("§eInteract-Log-Stick").setLore("§cLeft click block to see interaction log").setUnbreakable().build();

	public BlockLogManager(@Nonnull LiteSQL sql) {
		this.sql = sql;
	}

	public void saveBlockAction(@Nonnull UUID player, @Nonnull BlockActionType actionType, @Nonnull Material material, @Nonnull World world, int x, int y, int z)
			throws SQLException, ParseException {
		System.out.println("Saves data about block at {" + world.getName() + ", " + x + ", " + y + ", " + z + " " + "}");

		ResultSet resultSet = getBlockData(world, x, y, z);

		if (resultSet == null || resultSet.isClosed()) {
			createEntry(world, x, y, z);
			resultSet = getBlockData(world, x, y, z);
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("player", player.toString());
		jsonObject.put("type", actionType.name());
		jsonObject.put("material", material.name());
		jsonObject.put("time", BlockLogUtils.getCurrentTime().toString());

		JSONArray jsonArray = BlockLogUtils.jsonArrayFromString(resultSet.getString("actions"));
		jsonArray.add(jsonObject);

		getSQL().prepare("UPDATE blocks SET actions = ? WHERE world = ? AND x = ? AND y = ? AND z = ?", jsonArray.toJSONString(), world.getName(), x, y, z).executeUpdate();
		System.out.println("Finished Saving Data");
	}

	public void saveBlockAction(@Nonnull UUID player, @Nonnull BlockActionType actionType, @Nonnull Material material, @Nonnull Location location) throws SQLException, ParseException {
		if (location.getWorld() == null) {
			System.out.println("World is null (SAVE)");
			return;
		}
		saveBlockAction(player, actionType, material, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	@Nullable
	@CheckReturnValue
	public ResultSet getBlockData(@Nonnull World world, int x, int y, int z) throws SQLException {
		return getSQL().prepare("SELECT * FROM blocks WHERE world = ? AND x = ? AND y = ? AND z = ?", world.getName(), x, y, z).executeQuery();
	}

	@Nullable
	@CheckReturnValue
	public ResultSet getBlockData(@Nonnull Location location) throws SQLException {
		if (location.getWorld() == null) {
			System.out.println("World is null (GET)");
			return null;
		}
		return getBlockData(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}


	/**
	 * Creates a default entry for a block.
	 * Only use this method when there isn't already an entry for the block.
	 */
	@CheckReturnValue
	public void createEntry(@Nonnull World world, int x, int y, int z) throws SQLException {
		getSQL().prepare("INSERT INTO blocks(world, x, y, z, actions) VALUES(?, ?, ?, ?, ?)", world.getName(), x, y, z, new JSONArray().toJSONString()).executeUpdate();
	}

	@Nonnull
	@CheckReturnValue
	public ItemStack getLogStick() {
		return logStick;
	}

	@Nonnull
	@CheckReturnValue
	public ItemStack getInteractLogStick() {
		return interactLogStick;
	}

	@Nonnull
	@CheckReturnValue
	private SQL getSQL() {
		return sql;
	}

}