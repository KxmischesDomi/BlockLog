package net.codingarea.blocklog.utils;

import net.codingarea.engine.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockLogUtils {

	public static JSONArray jsonArrayFromString(@Nonnull String string) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		return (JSONArray) jsonParser.parse(string);
	}

	public static OffsetDateTime getCurrentTime() {
		return OffsetDateTime.now(ZoneOffset.UTC);
	}

	public static OffsetDateTime getTimeFromString(@Nonnull String s) {
		return OffsetDateTime.parse(s);
	}

	public static String formatBlockData(@Nonnull ResultSet rs) throws SQLException, ParseException {

		String world = rs.getString("world");
		int x = rs.getInt("x");
		int y = rs.getInt("y");
		int z = rs.getInt("z");
		JSONArray jsonArray = jsonArrayFromString(rs.getString("actions"));

		String formattedString = "§7Data of the block at {§8" + world + ", " + x + ", " + y + ", " + z + "§7}\n\n";

		for (Object o : jsonArray) {
			JSONObject jsonObject = ((JSONObject) o);
			String material = Utils.getEnumName((String) jsonObject.get("material"));
			String type = ((String) jsonObject.get("type")).toLowerCase();
			OffsetDateTime time = BlockLogUtils.getTimeFromString((String) jsonObject.get("time"));
			OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString((String) jsonObject.get("player")));
			ChatColor chatColor = getChatColorForAction(type);

			formattedString += "§7" + formatTime(time) + " - " +  player.getName() + " " + chatColor + type + "" + " §7" + material + "\n";
		}
		return formattedString + "\n";
	}

	public static ChatColor getChatColorForAction(String action) {

		switch (action) {

			case "destroyed": {
				return ChatColor.RED;
			}
			case "placed": {
				return ChatColor.GREEN;
			}
			default: {
				return ChatColor.GRAY;
			}

		}

	}

	public static String formatTime(@Nonnull OffsetDateTime time) {
		return time.getDayOfMonth() + "-" + time.getMonthValue() + "-" + time.getYear() + " " + time.getHour() + ":" + time.getMinute();

	}

}
