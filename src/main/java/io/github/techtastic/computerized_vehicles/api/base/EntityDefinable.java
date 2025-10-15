package io.github.techtastic.computerized_vehicles.api.base;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.computerized_vehicles.api.misc.LuaComputedVariable;
import io.github.techtastic.computerized_vehicles.util.LuaConversions;
import minecrafttransportsimulator.baseclasses.ComputedVariable;
import minecrafttransportsimulator.entities.components.AEntityD_Definable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityDefinable extends EntityExisting {
    protected EntityDefinable(AEntityD_Definable<?> definable) {
        super(definable);
    }

    protected AEntityD_Definable<?> getDefinable() throws LuaException {
        if (this.getBase() instanceof AEntityD_Definable<?> definable)
            return definable;
        throw new LuaException("the IV/MTS entity with ID " + this.id + " no longer exists!");
    }

    @LuaFunction
    public final Object getDefinition() throws LuaException {
        return LuaConversions.toLuaAny(this.getDefinable().definition);
    }

    @LuaFunction
    public final LuaComputedVariable getCustomVariable(String variableName) throws LuaException {
        return new LuaComputedVariable(this.getDefinable().getOrCreateVariable(variableName));
    }

    @LuaFunction
    public final Map<String, LuaComputedVariable> getAllVariables() throws LuaException {
        Map<String, LuaComputedVariable> table = new HashMap<>();
        try {
            Class<AEntityD_Definable> clazz = AEntityD_Definable.class;
            Field field = clazz.getDeclaredField("computedVariables");
            Map<String, ComputedVariable> vars = (Map<String, ComputedVariable>) field.get(this.getDefinable());
            vars.keySet().forEach(key -> {
                table.put(key, new LuaComputedVariable(vars.get(key)));
            });
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException | LuaException e) {
            if (e instanceof LuaException lua)
                throw lua;
            return null;
        }
        return table;
    }
}
