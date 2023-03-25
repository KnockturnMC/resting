package com.danichef.rest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The player sit event is called when a player is about to either sit down or stop sitting down.
 * <p>
 * The event may be cancelled to prevent either of those actions.
 */
public class PlayerSitEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final boolean layingDown;
    private boolean cancelled;

    public PlayerSitEvent(@NotNull final Player who, final boolean layingDown) {
        super(who);
        this.layingDown = layingDown;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Provides whether this event was called for a player sitting down or standing up.
     *
     * @return a boolean toggle flag, indicating what specific event this event was called for. {@code true} is returned
     *   if this event is fired for a player about to sit down, while {@code false} is returned for a player about to
     *   stand up.
     */
    public boolean sittingDown() {
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
