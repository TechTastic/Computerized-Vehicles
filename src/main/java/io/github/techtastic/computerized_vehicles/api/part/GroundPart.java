package io.github.techtastic.computerized_vehicles.api.part;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.PartGroundDevice;

public class GroundPart extends BasePart<PartGroundDevice> {
    public GroundPart(PartGroundDevice part) {
        super(part);
    }

    @LuaFunction
    public final boolean isFlat() throws LuaException {
        return this.getPart().flatVar.isActive;
    }

    @LuaFunction
    public final void setFlat(boolean flat) throws LuaException {
        this.getPart().setFlatState(flat);
    }

    @LuaFunction
    public final double getAngularVelocity() throws LuaException {
        return this.getPart().angularVelocity;
    }

    @LuaFunction
    public final double getAngularPosition() throws LuaException {
        return this.getPart().angularPosition;
    }

    @LuaFunction
    public final double getHeight() throws LuaException {
        return this.getPart().getHeight();
    }

    @LuaFunction
    public final double getWidth() throws LuaException {
        return this.getPart().getWidth();
    }
}
