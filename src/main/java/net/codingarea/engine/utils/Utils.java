package net.codingarea.engine.utils;

import sun.reflect.CallerSensitive;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class Utils {

	private Utils() { }

	public static String getEnumName(Enum<?> enun) {
		return getEnumName(enun.name());
	}

	public static String getEnumName(String enumName) {

		if (enumName == null) return "";

		StringBuilder builder = new StringBuilder();
		String[] chars = enumName.split("");
		chars[0] = chars[0].toUpperCase();
		boolean nextUp = true;
		for (String currentChar : chars) {
			if (currentChar.equals("_")) {
				nextUp = true;
				builder.append(" ");
				continue;
			}
			if (nextUp) {
				builder.append(currentChar.toUpperCase());
				nextUp = false;
			} else {
				builder.append(currentChar.toLowerCase());
			}
		}

		return builder.toString()
				.replace(" And ", " and ")
				.replace(" The ", " the ")
				.replace(" Or ", " or ")
				.replace(" Of " , " of")
				.replace(" In ", " in ")
				.replace(" On " , " on ")
				.replace(" Off ", " off ");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter yearTimeDateTime() {
		return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter yearDateTime() {
		return DateTimeFormatter.ofPattern("dd.MM.yyyy");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter dayDateTime() {
		return DateTimeFormatter.ofPattern("dd.MM");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter minuteDateTime() {
		return DateTimeFormatter.ofPattern("HH:mm");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter secondDateTime() {
		return DateTimeFormatter.ofPattern("HH:mm:ss");
	}

	@Nonnull
	@CheckReturnValue
	public static OffsetDateTime centralEuropeTime() {
		return OffsetDateTime.now(ZoneId.of("Europe/Paris"));
	}

	@Nonnull
	@CheckReturnValue
	public static ZoneId centralEuropeZoneId() {
		return ZoneId.of("Europe/Paris");
	}

	@Nonnull
	@CallerSensitive
	@CheckReturnValue
	public static Class<?>[] callerContext() {
		return new SecManager().callerContext();
	}

	@CallerSensitive
	@CheckReturnValue
	public static Class<?> caller(int index) {
		try {
			return callerContext()[index];
		} catch (Exception ignored) {
			return null;
		}
	}

	@CallerSensitive
	@CheckReturnValue
	public static Class<?> caller() {
		try {
			return caller(2);
		} catch (Exception ignored) {
			return null;
		}
	}

	static class SecManager extends SecurityManager {
		public Class<?>[] callerContext() {
			return getClassContext();
		}
	}

	public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		return methods;
	}

	public static String exceptionToString(@Nonnull Throwable ex) {
		StringBuilderPrintWriter writer = new StringBuilderPrintWriter();
		ex.printStackTrace(writer);
		return writer.toString();
	}

	@Nonnull
	@CheckReturnValue
	public static String arrayToString(@Nonnull String[] array) {
		return arrayToString(array, 0, array.length);
	}

	@Nonnull
	@CheckReturnValue
	public static String arrayToString(@Nonnull String[] array, int start) {
		return arrayToString(array, start, array.length);
	}

	@Nonnull
	@CheckReturnValue
	public static String arrayToString(@Nonnull String[] array, int start, int end) {
		StringBuilder builder = new StringBuilder();
		for (int i = start; i < end; i++) {
			builder.append(array[i] + " ");
		}
		return builder.toString().trim();
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> T[] array(T... content) {
		return content;
	}

	@CheckReturnValue
	public static long parseTime(@Nonnull String string) {
		long current = 0;
		long seconds = 0;
		for (String c : string.split("")) {
			try {

				int i = Integer.parseInt(c);
				current *= 10;
				current += i;

			} catch (Exception ignored) {

				int multiplier = 1;
				switch (c.toLowerCase()) {
					case "m":
						multiplier = 60;
						break;
					case "h":
						multiplier = 60*60;
						break;
					case "d":
						multiplier = 24*60*60;
						break;
					case "w":
						multiplier = 7*24*60*60;
						break;
					case "y":
						multiplier = 365*24*60*60;
						break;
				}

				seconds += current * multiplier;
				current = 0;

			}
		}
		seconds += current;
		return seconds;
	}

	public static boolean arrayContainsIgnoreCase(final @Nonnull String[] array, final @Nullable String search) {
		if (search == null) return false;
		for (String string : array) {
			if (search.equalsIgnoreCase(string))
				return true;
		}
		return false;
	}

}
