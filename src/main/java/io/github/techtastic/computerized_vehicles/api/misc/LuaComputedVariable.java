package io.github.techtastic.computerized_vehicles.api.misc;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.baseclasses.ComputedVariable;

public class LuaComputedVariable {
    private final ComputedVariable variable;

    public LuaComputedVariable(ComputedVariable variable) {
        this.variable = variable;
    }

    @LuaFunction
    public final String getVariableKey() {
        return this.variable.variableKey;
    }

    @LuaFunction
    public final boolean isActive() {
        return this.variable.isActive;
    }

    @LuaFunction
    public final void setActive(IArguments args) throws LuaException {
        boolean active = args.optBoolean(0, !this.variable.isActive);
        if (this.variable.isActive != active)
            this.variable.setActive(active, true);
    }

    @LuaFunction
    public final double getValue() {
        return this.variable.getValue();
    }

    @LuaFunction
    public final void setValue(double value) {
        if (this.variable.getValue() != value)
            this.variable.setTo(value, true);
    }
}
