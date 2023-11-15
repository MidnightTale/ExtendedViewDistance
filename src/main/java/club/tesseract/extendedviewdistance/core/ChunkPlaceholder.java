package club.tesseract.extendedviewdistance.core;

import club.tesseract.extendedviewdistance.api.ViewDistance;
import club.tesseract.extendedviewdistance.api.data.PlayerView;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public final class ChunkPlaceholder extends PlaceholderExpansion {

  private static ChunkPlaceholder imp;

  private final ChunkServer chunkServer;

  public ChunkPlaceholder(ChunkServer chunkServer) {
    super();
    this.chunkServer = chunkServer;
  }

  public static void registerPlaceholder(ChunkServer chunkServer) {
    (ChunkPlaceholder.imp = new ChunkPlaceholder(chunkServer)).register();
  }

  public static void unregisterPlaceholder() {
    if (imp != null) {
      ChunkPlaceholder.imp.unregister();
    }
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public @NotNull String getIdentifier() {
    return "viewdistance";
  }

  @Override
  public @NotNull String getAuthor() {
    return "TropicalShadow";
  }

  @Override
  public @NotNull String getVersion() {
    return "1.0.0";
  }

  @Override
  public String onRequest(
    final OfflinePlayer offlinePlayer,
    final @NotNull String params
  ) {
    if (!(offlinePlayer instanceof Player player)) return "-";
    final PlayerView playerView = ViewDistance.getPlayerView(chunkServer, player);
    if (playerView == null) return "-";
    return switch (params.toLowerCase(Locale.ROOT)) {
          case "delay" -> String.valueOf(playerView.getDelay());
          case "forcibly_max_distance" -> String.valueOf(playerView.getForciblyMaxDistance());
          case "max_extend_view_distance" -> String.valueOf(playerView.getMaxExtendViewDistance());
          case "now_extend_view_distance" -> String.valueOf(playerView.getNowExtendViewDistance());
          case "now_server_view_distance" -> String.valueOf(playerView.getNowServerViewDistance());
          case "forcibly_send_second_max_bytes" -> String.valueOf(playerView.getForciblySendSecondMaxBytes());
          case "network_speed_avg" -> String.valueOf(playerView.getNetworkSpeedAVG());
          case "network_report_load_fast_5s" -> String.valueOf(playerView.getNetworkReportLoadFast5s());
          case "network_report_load_fast_1m" -> String.valueOf(playerView.getNetworkReportLoadFast1m());
          case "network_report_load_fast_5m" -> String.valueOf(playerView.getNetworkReportLoadFast5m());
          case "network_report_load_slow_5s" -> String.valueOf(playerView.getNetworkReportLoadSlow5s());
          case "network_report_load_slow_1m" -> String.valueOf(playerView.getNetworkReportLoadSlow1m());
          case "network_report_load_slow_5m" -> String.valueOf(playerView.getNetworkReportLoadSlow5m());
          case "network_report_consume_5s" -> String.valueOf(playerView.getNetworkReportConsume5s());
          case "network_report_consume_1m" -> String.valueOf(playerView.getNetworkReportConsume1m());
          case "network_report_consume_5m" -> String.valueOf(playerView.getNetworkReportConsume5m());
          default -> null;
      };
  }
}
