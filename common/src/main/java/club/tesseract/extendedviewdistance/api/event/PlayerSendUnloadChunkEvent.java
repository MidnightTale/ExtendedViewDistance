package club.tesseract.extendedviewdistance.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import club.tesseract.extendedviewdistance.api.data.PlayerView;

/**
 * PlayerSendUnloadChunkEvent
 * Called when a player sends a chunk unload packet.
 */
public final class PlayerSendUnloadChunkEvent extends ExtendChunkEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel  = false;
    private final int chunkX;
    private final int chunkZ;


    public PlayerSendUnloadChunkEvent(PlayerView view, int chunkX, int chunkZ) {
        super(view);
        this.chunkX  = chunkX;
        this.chunkZ  = chunkZ;
    }


    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
