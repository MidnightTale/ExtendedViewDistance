package club.tesseract.extendedviewdistance.api.event;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import club.tesseract.extendedviewdistance.api.data.PlayerView;
import club.tesseract.extendedviewdistance.api.branch.BranchChunk;

/**
 * PlayerSendExtendChunkEvent
 * <p>
 *     Called when a player is about to send a chunk to the client.
 * </p>
 */
public final class PlayerSendExtendChunkEvent extends ExtendChunkEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel  = false;
    private final BranchChunk chunk;
    private final World world;


    public PlayerSendExtendChunkEvent(PlayerView view, BranchChunk chunk, World world) {
        super(view);
        this.chunk  = chunk;
        this.world  = world;
    }


    public BranchChunk getChunk() {
        return chunk;
    }

    public World getWorld() {
        return world;
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
