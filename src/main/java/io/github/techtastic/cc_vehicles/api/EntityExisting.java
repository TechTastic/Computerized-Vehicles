package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.cc_vehicles.util.LuaConversions;
import minecrafttransportsimulator.entities.components.AEntityB_Existing;

import java.util.Map;

public class EntityExisting extends EntityBase {
    private final AEntityB_Existing existing;

    protected EntityExisting(AEntityB_Existing existing) {
        super(existing);
        this.existing = existing;
    }

    @LuaFunction
    public final Map<String, Double> getMotion() {
        return LuaConversions.toLua(this.existing.motion);
    }

    @LuaFunction
    public final Map<String, Double> getPosition() {
        return LuaConversions.toLua(this.existing.position);
    }

    @LuaFunction
    public final Map<String, Double> getOrientation() {
        return LuaConversions.toLua(this.existing.orientation.angles);
    }
}
