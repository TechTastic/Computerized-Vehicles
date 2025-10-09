package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.APart;

public class BasePart<T extends APart> {
    private final T part;

    protected BasePart(T part) {
        this.part = part;
    }

    protected T getPart() {
        return this.part;
    }

    @LuaFunction
    public final String getUniqueId() {
        return this.part.uniqueUUID.toString();
    }
}
