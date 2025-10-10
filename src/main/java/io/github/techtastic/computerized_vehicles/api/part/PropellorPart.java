package io.github.techtastic.computerized_vehicles.api.part;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.PartPropeller;

public class PropellorPart extends BasePart<PartPropeller> {
    public PropellorPart(PartPropeller part) {
        super(part);
    }

    @LuaFunction
    public final double getAirstreamLinearVelocity() throws LuaException {
        return this.getPart().airstreamLinearVelocity;
    }
}
