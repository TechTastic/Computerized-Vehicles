package io.github.techtastic.computerized_vehicles.api.part;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.packets.instances.PacketPartEngine;

public class EnginePart extends BasePart<PartEngine> {
    public EnginePart(PartEngine engine) {
        super(engine);
    }

    @LuaFunction
    public final boolean isMagnetoActive() throws LuaException {
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
    public final boolean isInLiquid() throws LuaException {
        return this.getPart().isInLiquid();
    }

    @LuaFunction
    public final void startEngine() throws LuaException {
        this.getPart().startEngine();
    }

    @LuaFunction
    public final void handStartEngine() throws LuaException {
        this.getPart().handStartEngine();
    }

    @LuaFunction
    public final void autoStartEngine() throws LuaException {
        this.getPart().autoStartEngine();
    }

    @LuaFunction
    public final void stallEngine(IArguments args) throws LuaException {
        PacketPartEngine.Signal signal = args.getEnum(0, PacketPartEngine.Signal.class);
        this.getPart().stallEngine(signal);
    }

    @LuaFunction
    public final void backfireEngine() throws LuaException {
        this.getPart().backfireEngine();
    }

    @LuaFunction
    public final void badshiftEngine() throws LuaException {
        this.getPart().backfireEngine();
    }

    @LuaFunction
    public final boolean shiftUp() throws LuaException {
        return this.getPart().shiftUp();
    }

    @LuaFunction
    public final boolean shiftDown() throws LuaException {
        return this.getPart().shiftDown();
    }

    @LuaFunction
    public final void shiftNeutral() throws LuaException {
        this.getPart().shiftNeutral();
    }

    @LuaFunction
    public final double getCurrentGear() throws LuaException {
        return this.getPart().currentGearVar.getValue();
    }
}
