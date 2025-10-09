package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.packets.instances.PacketPartEngine;

public class EnginePart extends BasePart<PartEngine> {
    protected EnginePart(PartEngine engine) {
        super(engine);
    }

    @LuaFunction
    public final boolean isMagnetoActive() {
        return this.getPart().magnetoVar.isActive;
    }

    @LuaFunction
    public final void setMagnetoActive(IArguments args) throws LuaException {
        boolean oldState = this.getPart().magnetoVar.isActive;
        boolean newState = args.optBoolean(0, !oldState);

        if (oldState != newState)
            this.getPart().magnetoVar.setActive(newState, true);
    }

    @LuaFunction
    public final boolean isInLiquid() {
        return this.getPart().isInLiquid();
    }

    @LuaFunction
    public final void startEngine() {
        this.getPart().startEngine();
    }

    @LuaFunction
    public final void handStartEngine() {
        this.getPart().handStartEngine();
    }

    @LuaFunction
    public final void autoStartEngine() {
        this.getPart().autoStartEngine();
    }

    @LuaFunction
    public final void stallEngine(IArguments args) throws LuaException {
        PacketPartEngine.Signal signal = args.getEnum(0, PacketPartEngine.Signal.class);
        this.getPart().stallEngine(signal);
    }

    @LuaFunction
    public final void backfireEngine() {
        this.getPart().backfireEngine();
    }

    @LuaFunction
    public final void badshiftEngine() {
        this.getPart().backfireEngine();
    }

    @LuaFunction
    public final boolean shiftUp() {
        return this.getPart().shiftUp();
    }

    @LuaFunction
    public final boolean shiftDown() {
        return this.getPart().shiftDown();
    }

    @LuaFunction
    public final void shiftNeutral() {
        this.getPart().shiftNeutral();
    }

    @LuaFunction
    public final double getCurrentGear() {
        return this.getPart().currentGearVar.getValue();
    }
}
