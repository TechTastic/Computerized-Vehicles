package io.github.techtastic.computerized_vehicles.api.base;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.computerized_vehicles.util.LuaConversions;
import minecrafttransportsimulator.entities.components.AEntityB_Existing;

import java.util.Map;

public class EntityExisting extends EntityBase {
    protected EntityExisting(AEntityB_Existing existing) {
        super(existing);
    }

    protected AEntityB_Existing getEntity() throws LuaException {
        if (this.getBase() instanceof AEntityB_Existing existing)
            return existing;
        throw new LuaException("the IV/MTS entity with ID " + this.id + " no longer exists!");
    }

    @LuaFunction
    public final Map<String, Double> getMotion() throws LuaException {
        return LuaConversions.toLua(this.getEntity().motion);
    }

    @LuaFunction
    public final Map<String, Double> getPosition() throws LuaException {
        return LuaConversions.toLua(this.getEntity().position);
    }

    @LuaFunction
    public final Map<String, Double> getOrientation() throws LuaException {
        return LuaConversions.toLua(this.getEntity().orientation.angles);
    }
}
