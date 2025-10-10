package io.github.techtastic.computerized_vehicles.api.part;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.computerized_vehicles.api.FuelTank;
import minecrafttransportsimulator.entities.instances.PartInteractable;

public class InteractablePart extends BasePart<PartInteractable> {
    public InteractablePart(PartInteractable part) {
        super(part);
    }

    @LuaFunction
    public final FuelTank getFuelTank() throws LuaException {
        return new FuelTank(this.getPart().tank);
    }
}
