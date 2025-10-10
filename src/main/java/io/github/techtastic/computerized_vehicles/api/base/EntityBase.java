package io.github.techtastic.computerized_vehicles.api.base;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import minecrafttransportsimulator.mcinterface.AWrapperWorld;

import java.util.UUID;

public class EntityBase {
    private final AWrapperWorld world;
    protected final UUID id;

    protected EntityBase(AEntityA_Base base) {
        this.world = base.world;
        this.id = base.uniqueUUID;
    }

    protected AEntityA_Base getBase() throws LuaException {
        AEntityA_Base base = this.world.getEntity(this.id);
        if (base != null)
            return base;
        throw new LuaException("the IV/MTS entity with ID " + this.id + " no longer exists!");
    }

    @LuaFunction
    public final String getUniqueId() throws LuaException {
        return this.getBase().uniqueUUID.toString();
    }

    @LuaFunction
    public final double getMass() throws LuaException {
        return this.getBase().getMass();
    }
}
