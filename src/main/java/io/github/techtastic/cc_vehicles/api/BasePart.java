package io.github.techtastic.cc_vehicles.api;

import minecrafttransportsimulator.entities.instances.APart;

public class BasePart<T extends APart> extends EntityExisting {
    private final T part;

    protected BasePart(T part) {
        super(part);
        this.part = part;
    }

    protected T getPart() {
        return this.part;
    }
}
