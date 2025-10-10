package io.github.techtastic.computerized_vehicles.api.part;

import dan200.computercraft.api.lua.LuaException;
import io.github.techtastic.computerized_vehicles.api.base.EntityExisting;
import minecrafttransportsimulator.entities.instances.APart;

public class BasePart<T extends APart> extends EntityExisting {
    protected BasePart(T part) {
        super(part);
    }

    protected T getPart() throws LuaException {
        try {
            if (this.getEntity() instanceof APart part)
                return (T) part;
        } catch (ClassCastException ignored) {}
        throw new LuaException("the part with ID " + this.id + " no longer exists!");
    }
}
