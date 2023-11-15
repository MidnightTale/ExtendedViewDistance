package club.tesseract.extendedviewdistance.core.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Language Files
 */
public final class LangFiles {

  // All language files.
  private final Map<Locale, JsonObject> fileMap = new ConcurrentHashMap<>();
  // Default language file.
  private final JsonObject defaultMap = loadLang(Locale.ENGLISH);

  /**
   * @param sender sender to infer language
   * @param key configKey
   * @return language entry
   */
  @SuppressWarnings("deprecation")
  public String get(CommandSender sender, String key) {
    if (sender instanceof Player) {
      try {
        // 1.16 or greater
        Player player = (Player) sender;
        Method localMethod = Player.class.getMethod("locale");

        Locale result = (Locale) localMethod.invoke(player);
        return get(result, key);
      } catch (NoSuchMethodError | NoSuchMethodException | IllegalAccessException | InvocationTargetException noSuchMethodError) {
        return get(parseLocale(((Player) sender).getLocale()), key);
      } catch (Exception exception) {
        Bukkit.getLogger().severe("Failed to get locale from player. Report to developers: " + exception.getMessage());
        return get(Locale.ENGLISH, key);
      }
    } else {
      return get(Locale.ENGLISH, key);
    }
  }

  private static Locale parseLocale(String string) {
    String[] segments = string.split("_", 3);
    int length = segments.length;
    return switch (length) {
        case 1 -> new Locale(string);
        case 2 -> new Locale(segments[0], segments[1]);
        case 3 -> new Locale(segments[0], segments[1], segments[2]);
        default -> null;
    };
  }

  /**
   * @param locale language type
   * @param key configKey
   * @return language entry
   */
  public String get(Locale locale, String key) {
    JsonObject lang = fileMap.computeIfAbsent(locale, v -> loadLang(locale));
    JsonElement element = lang.get(key);
    if (element != null && !element.isJsonNull()) {
      return element.getAsString();
    } else {
      return defaultMap.get(key).getAsString();
    }
  }

  /**
   * @param locale language type
   * @return language file
   */
  private JsonObject loadLang(Locale locale) {
    URL url = getClass()
      .getClassLoader()
      .getResource(
        "lang/" + locale.toString().toLowerCase(Locale.ROOT) + ".json"
      );
    if (url == null) return new JsonObject();
    try {
      URLConnection connection = url.openConnection();
      connection.setUseCaches(true);
      return new Gson()
        .fromJson(
          new InputStreamReader(
            connection.getInputStream(),
            StandardCharsets.UTF_8
          ),
          JsonObject.class
        );
    } catch (IOException exception) {
      return new JsonObject();
    }
  }
}
