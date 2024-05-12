package club.tesseract.extendedviewdistance.core;

import club.tesseract.extendedviewdistance.api.branch.BranchMinecraft;
import club.tesseract.extendedviewdistance.api.branch.BranchPacket;
// import club.tesseract.extendedviewdistance.core.branch.v1182.Branch_1182_Minecraft;
// import club.tesseract.extendedviewdistance.core.branch.v1182.Branch_1182_Packet;
//import club.tesseract.extendedviewdistance.core.branch.v1194.Branch_1194_Minecraft;
//import club.tesseract.extendedviewdistance.core.branch.v1194.Branch_1194_Packet;
//import club.tesseract.extendedviewdistance.core.branch.v1201.Branch_1201_Minecraft;
//import club.tesseract.extendedviewdistance.core.branch.v1201.Branch_1201_Packet;
//import club.tesseract.extendedviewdistance.core.branch.v1202.Branch_1202_Minecraft;
//import club.tesseract.extendedviewdistance.core.branch.v1202.Branch_1202_Packet;
//import club.tesseract.extendedviewdistance.core.branch.v1204.Branch_1204_Packet;
//import club.tesseract.extendedviewdistance.core.branch.v1204.Branch_1204_Minecraft;
import club.tesseract.extendedviewdistance.core.branch.v1206.Branch_1206_Packet;
import club.tesseract.extendedviewdistance.core.branch.v1206.Branch_1206_Minecraft;
import club.tesseract.extendedviewdistance.core.command.Command;
import club.tesseract.extendedviewdistance.core.command.CommandSuggest;
import club.tesseract.extendedviewdistance.core.data.ConfigData;
import club.tesseract.extendedviewdistance.core.data.viewmap.ViewShape;
import club.tesseract.extendedviewdistance.core.metric.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkIndex extends JavaPlugin {
  private static Plugin plugin;
  private static ChunkServer chunkServer;
  private static ConfigData configData;
  private static BranchPacket branchPacket;
  private static BranchMinecraft branchMinecraft;

  private Metrics metrics;

  public void onEnable() {

    (ChunkIndex.plugin = this).saveDefaultConfig();
    configData = new ConfigData(this, getConfig());
    metrics = new Metrics(this, 20288);


    // Version check, and initialize the server
    String bukkitVersionOld = Bukkit.getBukkitVersion();
    String bukkitVersion = Bukkit
            .getServer()
            .getClass()
            .getPackage()
            .getName()
            .replace("org.bukkit.craftbukkit", "")
            .replace(".", "");
    getLogger().info("Bukkit version: " + bukkitVersion + " (" + bukkitVersionOld + ")");

    // TODO - implement 1.14 -> 1.17
    // 1.14    bukkitVersionOld.matches("^1\\.14[^0-9].*$")
    // 1.15    bukkitVersionOld.matches("^1\\.15\\D.*$")
    // 1.16    bukkitVersionOld.matches("^1\\.16\\D.*$")
    // 1.17    bukkitVersionOld.matches("^1\\.17\\D.*$")
    // if (bukkitVersion.equals("v1_18_R1")) {
    //   // 1.18.2
    //   branchPacket = new Branch_1182_Packet();
    //   branchMinecraft = new Branch_1182_Minecraft();
    //   chunkServer =
    //     new ChunkServer(
    //       ChunkIndex.configData,
    //       this,
    //       ViewShape.ROUND,
    //       ChunkIndex.branchMinecraft,
    //       ChunkIndex.branchPacket
    //     );
//    // } else
//    if (bukkitVersionOld.matches("^1\\.19\\D.*$")) {
//      // 1.19.4
//      branchPacket = new Branch_1194_Packet();
//      branchMinecraft = new Branch_1194_Minecraft();
//      chunkServer =
//        new ChunkServer(
//          ChunkIndex.configData,
//          this,
//          ViewShape.ROUND,
//          ChunkIndex.branchMinecraft,
//          ChunkIndex.branchPacket
//        );
//    } else if (bukkitVersion.equals("v1_20_R1")) {
//      // 1.20.1
//      branchPacket = new Branch_1201_Packet();
//      branchMinecraft = new Branch_1201_Minecraft();
//      chunkServer =
//              new ChunkServer(
//                      ChunkIndex.configData,
//                      this,
//                      ViewShape.ROUND,
//                      ChunkIndex.branchMinecraft,
//                      ChunkIndex.branchPacket
//              );
//    } else if(bukkitVersion.equals("v1_20_R2")) {
//      branchPacket = new Branch_1202_Packet();
//      branchMinecraft = new Branch_1202_Minecraft();
//      chunkServer =
//              new ChunkServer(
//                      ChunkIndex.configData,
//                      this,
//                      ViewShape.ROUND,
//                      ChunkIndex.branchMinecraft,
//                      ChunkIndex.branchPacket
//              );
//    } else if(bukkitVersion.equals("v1_20_R3")){
//      branchPacket = new Branch_1204_Packet();
//      branchMinecraft = new Branch_1204_Minecraft();
//      chunkServer =
//              new ChunkServer(
//                      ChunkIndex.configData,
//                      this,
//                      ViewShape.ROUND,
//                      ChunkIndex.branchMinecraft,
//                      ChunkIndex.branchPacket
//              );
//      } else
        if (bukkitVersionOld.contains("1.20.6-R0.1")) {
      branchPacket = new Branch_1206_Packet();
      branchMinecraft = new Branch_1206_Minecraft();
      chunkServer =
              new ChunkServer(
                      ChunkIndex.configData,
                      this,
                      ViewShape.ROUND,
                      ChunkIndex.branchMinecraft,
                      ChunkIndex.branchPacket
              );
    } else {
      this.getServer().getPluginManager().disablePlugin(this);
      throw new IllegalArgumentException("Unsupported MC version: " + bukkitVersion + " if you think this is a bug, please report it to discord: RealName#2570");
    }

    // Initialize the view for all players and worlds
    for (final Player player : Bukkit.getOnlinePlayers()) {
      ChunkIndex.chunkServer.initView(player);
    }
    for (final World world : Bukkit.getWorlds()) {
      ChunkIndex.chunkServer.initWorld(world);
    }

    Bukkit
            .getPluginManager()
            .registerEvents(
                    new ChunkEvent(
                            ChunkIndex.chunkServer,
                            ChunkIndex.branchPacket,
                            ChunkIndex.branchMinecraft
                    ), this
            );

    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      ChunkPlaceholder.registerPlaceholder(chunkServer);
    }

    // Commands
    final PluginCommand command = getCommand("viewdistance");
    if (command != null) {
      command.setExecutor(
              new Command(
                      ChunkIndex.chunkServer,
                      ChunkIndex.configData
              )
      );
      command.setTabCompleter(
              new CommandSuggest(
                      ChunkIndex.chunkServer,
                      ChunkIndex.configData
              )
      );
    }
  }

  public void onDisable() {
    if (chunkServer != null) chunkServer.close();
    metrics.shutdown();
  }

  public ChunkIndex() {
    super();
  }

  public static ChunkServer getChunkServer() {
    return ChunkIndex.chunkServer;
  }

  public static ConfigData getConfigData() {
    return ChunkIndex.configData;
  }

  public static Plugin getPlugin() {
    return ChunkIndex.plugin;
  }
}
