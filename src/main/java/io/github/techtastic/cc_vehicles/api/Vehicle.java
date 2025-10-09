package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.LuaValues;
import io.github.techtastic.cc_vehicles.networking.packet.PacketEntityColorChangeComputer;
import io.github.techtastic.cc_vehicles.util.LuaConversions;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.entities.instances.PartGun;
import minecrafttransportsimulator.entities.instances.PartSeat;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.InterfaceManager;

import java.util.List;
import java.util.UUID;

public class Vehicle extends EntityExisting {
    private final WrapperWorld world;
    private final UUID vehicleId;

    protected Vehicle(WrapperWorld world, UUID vehicleId) {
        super(world.getEntity(vehicleId));
        this.world = world;
        this.vehicleId = vehicleId;
    }

    private EntityVehicleF_Physics getVehicle() throws LuaException {
        if (this.world.getEntity(this.vehicleId) instanceof EntityVehicleF_Physics vehicle)
            return vehicle;
        throw new LuaException("the vehicle with ID " + this.vehicleId + " no longer exists!");
    }

    @LuaFunction
    public final boolean isBeingFueled() throws LuaException {
        return this.getVehicle().beingFueled;
    }

    @LuaFunction
    public final FuelTank getFuelTank() throws LuaException {
        return new FuelTank(this.getVehicle().fuelTank);
    }

    @LuaFunction
    public final Object getDefinition() throws LuaException {
        return LuaConversions.toLuaAny(this.getVehicle().definition);
    }

    @LuaFunction
    public final List<Engine> getEngines() throws LuaException {
        return this.getVehicle().engines.stream().map(Engine::new).toList();
    }

    @LuaFunction
    public final List<SeatPart> getAllSeats() throws LuaException {
        return this.getVehicle().partsInSlots.stream()
                .filter(part -> part instanceof PartSeat)
                .map(part -> new SeatPart((PartSeat) part))
                .toList();
    }

    @LuaFunction
    public final List<GunPart> getAllGuns() throws LuaException {
        return this.getVehicle().partsInSlots.stream()
                .filter(part -> part instanceof PartGun)
                .map(part -> new GunPart((PartGun) part))
                .toList();
    }

    @LuaFunction
    public final void addPart(IArguments args, int slot) throws LuaException {
        AItemPart partItem = LuaConversions.getPart(args, 0);

        EntityVehicleF_Physics vehicle = this.getVehicle();
        JSONPartDefinition newPartDef = vehicle.definition.parts.get(slot);
        if (!partItem.isPartValidForPackDef(newPartDef, vehicle.subDefinition, !newPartDef.bypassSlotMinMax))
            throw LuaValues.badArgument(0, "valid IV/MTS part for " + vehicle.definition.packID + " pack", partItem.getItemName());

        vehicle.addPartFromStack(partItem.getNewStack(null), null, slot, false, true);
    }

    @LuaFunction
    public final void removePart(int slot) throws LuaException {
        EntityVehicleF_Physics vehicle = this.getVehicle();
        APart part = vehicle.partsInSlots.get(slot);
        vehicle.removePart(part, true, true);
    }

    @LuaFunction
    public final List<String> getVariants() throws LuaException {
        return this.getVehicle().definition.definitions.stream().map(def -> def.subName).toList();
    }

    @LuaFunction
    public final void changeVariant(String variant) throws LuaException {
        EntityVehicleF_Physics vehicle = this.getVehicle();
        vehicle.updateSubDefinition(variant);
        vehicle.parts.forEach(part -> part.updateTone(true));
        InterfaceManager.packetInterface.sendToAllClients(new PacketEntityColorChangeComputer(vehicle, variant));
    }

    @LuaFunction
    public final void setMudAccumulation(double mud) throws LuaException {
        EntityVehicleF_Physics vehicle = this.getVehicle();
        if (vehicle.containsVariable("mudAcc"))
            vehicle.getOrCreateVariable("mudAcc").setTo(mud, true);
        throw new LuaException("This vehicle does not handle mud!");
    }
}
