package com.danichef.rest;

import com.danichef.rest.corpses.PlayerCorpses;
import com.danichef.rest.seats.PlayerSit;
import com.google.common.base.Suppliers;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The resting api interface acts as the main entry point to the resting api plugin api.
 */
public interface RestingAPI {

    /**
     * Represents static level access to an instance of the resting api via {@link org.bukkit.plugin.ServicesManager}.
     */
    @NotNull
    Supplier<RestingAPI> ACCESS = Suppliers.memoize(() -> Bukkit.getServicesManager().load(RestingAPI.class));

    /**
     * Provides a singleton like instance of the resting api that offers access to modules and logic exposed
     * by the api module.
     *
     * @return the api instance.
     */
    @NotNull
    static RestingAPI instance() {
        return ACCESS.get();
    }

    /**
     * Provides the corpse manager of resting.
     * The corpse manager is responsible for faking a player to lay down, acting like a corpse.
     *
     * @return the manager instance.
     */
    @NotNull PlayerCorpses corpseManager();

    /**
     * Provides the sit manager of resting.
     * The sit manager is responsible for having a player sit down or stand back up.
     *
     * @return the manager instance.
     */
    @NotNull PlayerSit sitManager();

}
