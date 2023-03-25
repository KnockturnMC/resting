package com.danichef.rest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The player corpse event is called when a player is about to either become a corpse (lay down) or stop
 * being a corpse (waking up).
 * <p>
 * The event may be cancelled to prevent either of those actions.
 */
public class PlayerCorpseEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final boolean layingDown;
    private boolean cancelled;

    public PlayerCorpseEvent(@NotNull final Player who, final boolean layingDown) {
        super(who);
        this.layingDown = layingDown;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Provides whether this event was called for a player laying down or waking up.
     *
     * @return a boolean toggle flag, indicating what specific event this event was called for. {@code true} is returned
     *   if this event is fired for a player about to lay down, while {@code false} is returned for a player about to
     *   wake up.
     */
    public boolean layingDown() {
        return layingDown;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
