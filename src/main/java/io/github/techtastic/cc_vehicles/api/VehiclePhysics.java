package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.LuaValues;
import io.github.techtastic.cc_vehicles.networking.packet.PacketEntityColorChangeComputer;
import io.github.techtastic.cc_vehicles.util.LuaConversions;
import mcinterface1201.WrapperEntity;
import mcinterface1201.WrapperItemStack;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.entities.instances.PartSeat;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.IWrapperEntity;
import minecrafttransportsimulator.mcinterface.InterfaceManager;

import java.util.List;
import java.util.UUID;

public class VehiclePhysics extends EntityExisting {
    private final EntityVehicleF_Physics vehicle;

    protected VehiclePhysics(EntityVehicleF_Physics vehicle) {
        super(vehicle);
        this.vehicle = vehicle;

        // ae77c97f-4093-48f1-96fe-344079a59959
    }

    @LuaFunction
    public final boolean isBeingFueled() {
        return this.vehicle.beingFueled;
    }

    @LuaFunction
    public final FuelTank getFuelTank() {
        return new FuelTank(this.vehicle.fuelTank);
    }

    @LuaFunction
    public final Object getDefinition() {
        return LuaConversions.toLuaAny(this.vehicle.definition);
    }

    @LuaFunction
    public final List<Engine> getEngines() {
        return this.vehicle.engines.stream().map(Engine::new).toList();
    }

    @LuaFunction
    public final void getParts() {
        this.vehicle.parts.forEach(part -> {

        });
    }

    @LuaFunction
    public final void addPart(IArguments args, int slot) throws LuaException {
        AItemPart partItem = LuaConversions.getPart(args, 0);

        JSONPartDefinition newPartDef = this.vehicle.definition.parts.get(slot);
        if (!partItem.isPartValidForPackDef(newPartDef, this.vehicle.subDefinition, !newPartDef.bypassSlotMinMax))
            throw LuaValues.badArgument(0, "valid IV/MTS part for " + this.vehicle.definition.packID + " pack", partItem.getItemName());

        this.vehicle.addPartFromStack(partItem.getNewStack(null), null, slot, false, true);
    }

    @LuaFunction
    public final List<String> getVariants() {
        return this.vehicle.definition.definitions.stream().map(def -> def.subName).toList();
    }

    @LuaFunction
    public final void changeVariant(String variant) {
        this.vehicle.updateSubDefinition(variant);
        this.vehicle.parts.forEach(part -> part.updateTone(true));
        InterfaceManager.packetInterface.sendToAllClients(new PacketEntityColorChangeComputer(this.vehicle, variant));
    }

    @LuaFunction
    public final void seatEntity(String uuid) throws LuaException {
        UUID entityId = UUID.fromString(uuid);
        IWrapperEntity entity = this.vehicle.world.getExternalEntity(entityId);
        if (entity == null)
            throw LuaValues.badArgument(0, "valid Entity UUID", uuid);
        this.vehicle.setRider(entity, true);
    }
}
