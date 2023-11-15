package club.tesseract.extendedviewdistance.core.command;

import club.tesseract.extendedviewdistance.core.ChunkServer;
import club.tesseract.extendedviewdistance.core.data.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CommandSuggest implements TabCompleter {

  private final ChunkServer chunkServer;
  private final ConfigData configData;

  public CommandSuggest(ChunkServer chunkServer, ConfigData configData) {
    this.chunkServer = chunkServer;
    this.configData = configData;
  }

  @Override
  public List<String> onTabComplete(
          @NotNull CommandSender sender,
          @NotNull Command command,
          @NotNull String label,
          @NotNull String[] parameters
  ) {
    List<String> list = new ArrayList<>();

    if (!sender.hasPermission("command.viewdistance")) {
      return list;
    }

    if (parameters.length == 1) {
      addBaseCommands(list);
    } else if (parameters.length == 2) {
      processSecondParameter(parameters, list);
    } else if (parameters.length == 3) {
      processThirdParameter(parameters, list);
    }

    return list;
  }

  private void addBaseCommands(List<String> list) {
    list.add("start");
    list.add("stop");
    list.add("reload");
    list.add("report");
    list.add("permissionCheck");
    list.add("debug");
  }

  private void processSecondParameter(String[] parameters, List<String> list) {
    String command = parameters[0];
    switch (command) {
      case "report":
        list.addAll(List.of("server", "thread", "world", "player"));
        break;
      case "permissionCheck":
        Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
        break;
      case "debug":
        list.add("view");
        break;
    }
  }

  private void processThirdParameter(String[] parameters, List<String> list) {
    String command = parameters[0];
    if ("debug".equals(command)) {
      String debugType = parameters[1];
      if ("view".equals(debugType)) {
        Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
      }
    }
  }

}
