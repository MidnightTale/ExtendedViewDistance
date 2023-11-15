package club.tesseract.extendedviewdistance.api;

import club.tesseract.extendedviewdistance.api.data.PlayerView;
import club.tesseract.extendedviewdistance.core.ChunkServer;
import club.tesseract.extendedviewdistance.core.data.PlayerChunkView;
import org.bukkit.entity.Player;

public final class ViewDistance {
    private ViewDistance() {
    }


    public static PlayerView getPlayerView(ChunkServer chunkServer, Player player) {
        PlayerChunkView view = chunkServer.getView(player);
        return view != null ? view.viewAPI : null;
    }
}
