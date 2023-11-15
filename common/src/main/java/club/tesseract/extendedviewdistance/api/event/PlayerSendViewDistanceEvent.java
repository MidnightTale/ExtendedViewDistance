package club.tesseract.extendedviewdistance.api.event;

import club.tesseract.extendedviewdistance.api.data.PlayerView;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public final class PlayerSendViewDistanceEvent extends ExtendChunkEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel  = false;
    private int distance;


    public PlayerSendViewDistanceEvent(PlayerView view, int distance) {
        super(view);
        this.distance   = distance;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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
