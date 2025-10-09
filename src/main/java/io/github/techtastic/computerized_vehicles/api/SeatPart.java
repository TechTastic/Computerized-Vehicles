package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import mcinterface1201.WrapperEntity;
import minecrafttransportsimulator.entities.instances.PartSeat;
import minecrafttransportsimulator.mcinterface.IWrapperEntity;

import java.util.List;
import java.util.UUID;

public class SeatPart extends BasePart<PartSeat> {
    protected SeatPart(PartSeat part) {
        super(part);
    }

    @LuaFunction
    public final String getRider() {
        return this.getPart().rider instanceof WrapperEntity entity ? entity.getID().toString() : null;
    }

    @LuaFunction
    public final void removeRider() {
        this.getPart().removeRider();
    }

    @LuaFunction
    public final boolean setRider(String uuid, IArguments args) throws LuaException {
        PartSeat seat = this.getPart();
        IWrapperEntity entity = seat.world.getEntity(UUID.fromString(uuid));
        return seat.setRider(entity, args.optBoolean(1, true));
    }

    @LuaFunction
    public final List<GunPart> getGuns() {
        return this.getPart().gunGroups.get(this.getPart().activeGunItem).stream().map(GunPart::new).toList();
    }
}
