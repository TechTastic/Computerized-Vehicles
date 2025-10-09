package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.components.AEntityA_Base;

public class EntityBase {
    private AEntityA_Base base;

    protected EntityBase(AEntityA_Base base) {
        this.base = base;
    }

    @LuaFunction
    public final String getUniqueId() {
        return this.base.uniqueUUID.toString();
    }

    @LuaFunction
    public final double getMass() {
        return this.base.getMass();
    }
}
