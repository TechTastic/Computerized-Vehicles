package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.entities.instances.EntityFluidTank;

import java.util.Objects;

public class FuelTank extends EntityBase {
    private EntityFluidTank tank;

    protected FuelTank(EntityFluidTank tank) {
        super(tank);
        this.tank = tank;
    }

    private String[] optFluid(IArguments args, int index) throws LuaException {
        String str = args.optString(index, "wildcard:");
        if (Objects.equals(str, "wildcard:"))
            return new String[] {"wildcard", ""};
        return str.split(":");
    }

    @LuaFunction
    public final double getFluidLevel() {
        return this.tank.getFluidLevel();
    }

    @LuaFunction
    public final double getMaxLevel() {
        return this.tank.getMaxLevel();
    }

    @LuaFunction
    public final String getFluid() {
        if (this.tank.getFluid().isEmpty())
            return null;
        return this.tank.getFluidMod() + ":" + this.tank.getFluid();
    }

    @LuaFunction
    public final void manuallySet(IArguments args) throws LuaException {
        String[] fluid = optFluid(args, 0);
        double setLevel = args.optDouble(1, this.tank.getMaxLevel());

        this.tank.manuallySet(fluid[1], fluid[0], setLevel);
    }

    @LuaFunction
    public final double fill(IArguments args) throws LuaException {
        String[] fluid = optFluid(args, 0);
        double maxAmount = args.optDouble(1, this.tank.getMaxLevel() - this.tank.getFluidLevel());
        boolean doFill = args.optBoolean(2, true);

        return this.tank.fill(fluid[1], fluid[0], maxAmount, doFill);
    }

    @LuaFunction
    public final double drain(IArguments args) throws LuaException {
        String[] fluid = optFluid(args, 0);
        double maxAmount = args.optDouble(1, this.tank.getFluidLevel());
        boolean doDrain = args.optBoolean(2, true);

        return this.tank.fill(fluid[1], fluid[0], maxAmount, doDrain);
    }

    @LuaFunction
    public final void clear() {
        this.tank.clear();
    }

    @LuaFunction
    public final double getExplosiveness() {
        return this.tank.getExplosiveness();
    }
}
