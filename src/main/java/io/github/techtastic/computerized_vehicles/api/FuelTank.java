package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.computerized_vehicles.api.base.EntityBase;
import minecrafttransportsimulator.entities.instances.EntityFluidTank;

import java.util.Objects;

public class FuelTank extends EntityBase {
    public FuelTank(EntityFluidTank tank) {
        super(tank);
    }

    protected EntityFluidTank getTank() throws LuaException {
        if (this.getBase() instanceof EntityFluidTank tank)
            return tank;
        throw new LuaException("the fuel tank with ID " + this.id + " no longer exists!");
    }

    private String[] optFluid(IArguments args, int index) throws LuaException {
        String str = args.optString(index, "wildcard:");
        if (Objects.equals(str, "wildcard:"))
            return new String[] {"wildcard", ""};
        return str.split(":");
    }

    @LuaFunction
    public final double getFluidLevel() throws LuaException {
        return this.getTank().getFluidLevel();
    }

    @LuaFunction
    public final double getMaxLevel() throws LuaException {
        return this.getTank().getMaxLevel();
    }

    @LuaFunction
    public final String getFluid() throws LuaException {
        if (this.getTank().getFluid().isEmpty())
            return null;
        return this.getTank().getFluidMod() + ":" + this.getTank().getFluid();
    }

    @LuaFunction
    public final void manuallySet(IArguments args) throws LuaException {
        String[] fluid = optFluid(args, 0);
        double setLevel = args.optDouble(1, this.getTank().getMaxLevel());

        this.getTank().manuallySet(fluid[1], fluid[0], setLevel);
    }

    @LuaFunction
    public final double fill(IArguments args) throws LuaException {
        String[] fluid = optFluid(args, 0);
        double maxAmount = args.optDouble(1, this.getTank().getMaxLevel() - this.getTank().getFluidLevel());
        boolean doFill = args.optBoolean(2, true);

        return this.getTank().fill(fluid[1], fluid[0], maxAmount, doFill);
    }

    @LuaFunction
    public final double drain(IArguments args) throws LuaException {
        String[] fluid = optFluid(args, 0);
        double maxAmount = args.optDouble(1, this.getTank().getFluidLevel());
        boolean doDrain = args.optBoolean(2, true);

        return this.getTank().fill(fluid[1], fluid[0], maxAmount, doDrain);
    }

    @LuaFunction
    public final void clear() throws LuaException {
        this.getTank().clear();
    }

    @LuaFunction
    public final double getExplosiveness() throws LuaException {
        return this.getTank().getExplosiveness();
    }
}
