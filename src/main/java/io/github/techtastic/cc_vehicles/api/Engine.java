package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.packets.instances.PacketPartEngine;

public class Engine extends EntityExisting {
    private final PartEngine engine;

    protected Engine(PartEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @LuaFunction
    public final boolean isInLiquid() {
        return this.engine.isInLiquid();
    }

    @LuaFunction
    public final void startEngine() {
        this.engine.startEngine();
    }

    @LuaFunction
    public final void handStartEngine() {
        this.engine.handStartEngine();
    }

    @LuaFunction
    public final void autoStartEngine() {
        this.engine.autoStartEngine();
    }

    @LuaFunction
    public final void stallEngine(IArguments args) throws LuaException {
        PacketPartEngine.Signal signal = args.getEnum(0, PacketPartEngine.Signal.class);
        this.engine.stallEngine(signal);
    }

    @LuaFunction
    public final void backfireEngine() {
        this.engine.backfireEngine();
    }

    @LuaFunction
    public final void badshiftEngine() {
        this.engine.backfireEngine();
    }
}
