package club.tesseract.extendedviewdistance.core.command;

import club.tesseract.extendedviewdistance.core.ChunkServer;
import club.tesseract.extendedviewdistance.core.data.ConfigData;
import club.tesseract.extendedviewdistance.core.data.CumulativeReport;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Command implements CommandExecutor {

  private final ChunkServer chunkServer;
  private final ConfigData configData;

  public Command(ChunkServer chunkServer, ConfigData configData) {
    this.chunkServer = chunkServer;
    this.configData = configData;
  }

  @Override
  public boolean onCommand(
          CommandSender sender,
          org.bukkit.command.@NotNull Command command,
          @NotNull String message,
          String[] parameters
  ) {
    if (!sender.hasPermission("command.viewdistance")) {
      sendMessage(sender, "command.no_permission", net.md_5.bungee.api.ChatColor.RED);
    } else if (parameters.length < 1) {
      sendMessage(sender, "command.missing_parameters", net.md_5.bungee.api.ChatColor.RED);
    } else {
      handleCommand(sender, parameters);
    }
    return true;
  }

  private void sendMessage(CommandSender sender, String key, net.md_5.bungee.api.ChatColor colour) {
    ComponentBuilder builder = new ComponentBuilder();
    builder.append(this.chunkServer.lang.get(sender, key));
    builder.color(colour);
    try {
      sender.sendMessage(builder.create());
    }catch (NoSuchMethodError ignored){
      try {
        sender.spigot().sendMessage(builder.create());
      }catch (java.lang.NoSuchMethodError e){
        throw new RuntimeException("Unsupported version", e);
      }
    }
  }

  private void handleCommand(CommandSender sender, String[] parameters) {
    String commandType = parameters[0];

    switch (commandType) {
      case "reload":
        handleReloadCommand(sender);
        break;
      case "report":
        handleReportCommand(sender, parameters);
        break;
      case "start":
        handleStartCommand(sender);
        break;
      case "stop":
        handleStopCommand(sender);
        break;
      case "permissionCheck":
        handlePermissionCheckCommand(sender, parameters);
        break;
      case "debug":
        handleDebugCommand(sender, parameters);
        break;
      default:
        sendMessage(sender, "command.unknown_parameter_type", net.md_5.bungee.api.ChatColor.RED);
    }
  }

  private void handleDebugCommand(CommandSender sender, String[] args){
    if(args.length < 2){
      sendMessage(sender, "command.missing_parameters", net.md_5.bungee.api.ChatColor.RED);
      return;
    }
    String debugType = args[1];
    if(debugType.equals("view")){
      if(args.length < 3){
        sendMessage(sender, "command.missing_parameters", net.md_5.bungee.api.ChatColor.RED);
        return;
      }
      Player player = Bukkit.getPlayer(args[2]);
      if(player == null){
        sendMessage(sender, "command.players_do_not_exist", net.md_5.bungee.api.ChatColor.RED);
        return;
      }
      chunkServer.getView(player).getMap().debug(sender);

      return;
    }

    sendMessage(sender, "command.unknown_parameter_type", net.md_5.bungee.api.ChatColor.RED);
  }

  private void handlePermissionCheckCommand(CommandSender sender, String[] args){
    if(args.length < 2){
      sendMessage(sender, "command.missing_parameters", net.md_5.bungee.api.ChatColor.RED);
      return;
    }
    Player player = Bukkit.getPlayer(args[1]);
    if(player == null){
      sendMessage(sender, "command.players_do_not_exist", net.md_5.bungee.api.ChatColor.RED);
      return;
    }

    this.chunkServer.getView(player).permissionsNeed = true;
    sendMessage(sender, "command.rechecked_player_permissions", net.md_5.bungee.api.ChatColor.YELLOW);
  }

  private void handleStopCommand(CommandSender sender){
    this.chunkServer.globalPause = true;
    sendMessage(sender, "command.suspension_execution", net.md_5.bungee.api.ChatColor.YELLOW);
  }

  private void handleStartCommand(CommandSender sender){
    this.chunkServer.globalPause = false;
    sendMessage(sender, "command.continue_execution", net.md_5.bungee.api.ChatColor.YELLOW);
  }

  private void handleReloadCommand(CommandSender sender) {
    try {
      this.configData.reload();
      chunkServer.reloadMultithreaded();
      sendMessage(sender, "command.reread_configuration_successfully", net.md_5.bungee.api.ChatColor.RED);
    } catch (Exception e) {
      e.printStackTrace();
      sendMessage(sender, "command.reread_configuration_error", net.md_5.bungee.api.ChatColor.RED);
    }
  }

  private void handleReportCommand(CommandSender sender, String[] parameters) {
    if (parameters.length < 2) {
      sendMessage(sender, "command.missing_parameters", net.md_5.bungee.api.ChatColor.RED);
      return;
    }

    String reportType = parameters[1];

    switch (reportType) {
      case "server":
        sendReportHead(sender);
        sendReport(sender, "*SERVER", this.chunkServer.serverCumulativeReport);
        break;
      case "thread":
        sendReportHead(sender);
        chunkServer.threadsCumulativeReport.forEach( (threadNumber, cumulativeReport) -> {
          sendReport(sender, "*THREAD#" + threadNumber, cumulativeReport);
        });
        break;
      case "world":
        sendReportHead(sender);
        chunkServer.worldsCumulativeReport.forEach( (worldName, cumulativeReport) -> {
          sendReport(sender, worldName.getName(), cumulativeReport);
        });
        break;
      case "player":
        sendReportHead(sender);
        chunkServer.playersViewMap.forEach( (player, playerView) -> {
          sendReport(sender, player.getName(), playerView.cumulativeReport);
        });
        break;
      default:
        sendMessage(sender, "command.unknown_parameter_type", net.md_5.bungee.api.ChatColor.RED);
    }
  }

  private void sendReportHead(CommandSender sender) {
    String timeSegment =
            this.chunkServer.lang.get(sender, "command.report.5s") +
                    "/" +
                    this.chunkServer.lang.get(sender, "command.report.1m") +
                    "/" +
                    this.chunkServer.lang.get(sender, "command.report.5m");
    sender.sendMessage(
            ChatColor.YELLOW +
                    this.chunkServer.lang.get(sender, "command.report.source") +
                    ChatColor.WHITE +
                    " | " +
                    ChatColor.GREEN +
                    this.chunkServer.lang.get(sender, "command.report.fast") +
                    " " +
                    timeSegment +
                    ChatColor.WHITE +
                    " | " +
                    ChatColor.RED +
                    this.chunkServer.lang.get(sender, "command.report.slow") +
                    " " +
                    timeSegment +
                    ChatColor.WHITE +
                    " | " +
                    ChatColor.GOLD +
                    this.chunkServer.lang.get(sender, "command.report.flow") +
                    " " +
                    timeSegment
    );
  }

  private void sendReport(
          CommandSender sender,
          String source,
          CumulativeReport cumulativeReport
  ) {
    sender.sendMessage(
            ChatColor.YELLOW + source +
                    ChatColor.WHITE + " | " +
                    ChatColor.GREEN + cumulativeReport.reportLoadFast5s() +
                    "/" + cumulativeReport.reportLoadFast1m() +
                    "/" + cumulativeReport.reportLoadFast5m() +
                    ChatColor.WHITE + " | " +
                    ChatColor.RED + cumulativeReport.reportLoadSlow5s() +
                    "/" + cumulativeReport.reportLoadSlow1m() +
                    "/" + cumulativeReport.reportLoadSlow5m() +
                    ChatColor.WHITE + " | " +
                    ChatColor.GOLD + cumulativeReport.reportConsume5s() +
                    "/" + cumulativeReport.reportConsume1m() +
                    "/" + cumulativeReport.reportConsume5m()
    );
  }
}
