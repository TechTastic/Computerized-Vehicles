package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.LuaValues;
import io.github.techtastic.computerized_vehicles.networking.packet.PacketEntityColorChangeComputer;
import io.github.techtastic.computerized_vehicles.util.LuaConversions;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.ComputedVariable;
import minecrafttransportsimulator.entities.instances.*;
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

    private void toggleActive(ComputedVariable variable, IArguments args, int index) throws LuaException {
        boolean oldState = variable.isActive;
        boolean newState = args.optBoolean(index, !oldState);

        if (oldState != newState)
            variable.setActive(newState, true);
    }

    // General

    @LuaFunction
    public final Object getDefinition() throws LuaException {
        return LuaConversions.toLuaAny(this.getVehicle().definition);
    }

    @LuaFunction
    public final double getSpeed() throws LuaException {
        return this.getVehicle().indicatedSpeed;
    }

    // Fuel

    @LuaFunction
    public final boolean isBeingFueled() throws LuaException {
        return this.getVehicle().beingFueled;
    }

    @LuaFunction
    public final FuelTank getFuelTank() throws LuaException {
        return new FuelTank(this.getVehicle().fuelTank);
    }

    // Parts

    @LuaFunction
    public final List<EnginePart> getEngines() throws LuaException {
        return this.getVehicle().engines.stream().map(EnginePart::new).toList();
    }

    @LuaFunction
    public final List<SeatPart> getSeats() throws LuaException {
        return this.getVehicle().partsInSlots.stream()
                .filter(part -> part instanceof PartSeat)
                .map(part -> new SeatPart((PartSeat) part))
                .toList();
    }

    @LuaFunction
    public final List<GunPart> getGuns() throws LuaException {
        return this.getVehicle().partsInSlots.stream()
                .filter(part -> part instanceof PartGun)
                .map(part -> new GunPart((PartGun) part))
                .toList();
    }

    @LuaFunction
    public final void addPart(IArguments args) throws LuaException {
        AItemPart partItem = LuaConversions.getPart(args, 0);
        int slot = args.getInt(1);

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

    // Variants and Mud

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

    // Controls

    @LuaFunction
    public final double getThrottle() throws LuaException {
        return this.getVehicle().throttleVar.getValue();
    }

    @LuaFunction
    public final void setThrottle(double throttle) throws LuaException {
        this.getVehicle().throttleVar.setTo(Math.max(0, Math.min(1, throttle)), true);
    }

    @LuaFunction
    public final double getBrakeLevel() throws LuaException {
        return this.getVehicle().brakeVar.getValue();
    }

    @LuaFunction
    public final void setBrakeLevel(double brakeLevel) throws LuaException {
        this.getVehicle().brakeVar.setTo(Math.max(0, Math.min(1, brakeLevel)), true);
    }

    @LuaFunction
    public final boolean isParkingBrakeActive() throws LuaException {
        return this.getVehicle().parkingBrakeVar.isActive;
    }

    @LuaFunction
    public final void setParkingBrakeActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().parkingBrakeVar, args, 0);
    }

    @LuaFunction
    public final double getTurningInput() throws LuaException {
        return this.getVehicle().rudderInputVar.getValue();
    }

    @LuaFunction
    public final void setTurningInput(double angle) throws LuaException {
        this.getVehicle().rudderInputVar.setTo(angle, true);
    }

    // Horn

    @LuaFunction
    public final boolean isHornActive() throws LuaException {
        return this.getVehicle().hornVar.isActive;
    }

    @LuaFunction
    public final void setHornActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().hornVar, args, 0);
    }

    // Lights

    @LuaFunction
    public final boolean isTurnSignalActive(boolean left) throws LuaException {
        if (left)
            return this.getVehicle().leftTurnLightVar.isActive;
        return this.getVehicle().rightTurnLightVar.isActive;
    }

    @LuaFunction
    public final void setTurnSignalActive(boolean left, IArguments args) throws LuaException {
        if (left)
            toggleActive(this.getVehicle().leftTurnLightVar, args, 1);
        else
            toggleActive(this.getVehicle().rightTurnLightVar, args, 1);
    }

    @LuaFunction
    public final boolean getHeadLightActive() throws LuaException {
        return this.getVehicle().headLightVar.isActive;
    }

    @LuaFunction
    public final void setHeadLightActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().headLightVar, args, 0);
    }

    @LuaFunction
    public final boolean getLandingLightActive() throws LuaException {
        return this.getVehicle().landingLightVar.isActive;
    }

    @LuaFunction
    public final void setLandingLightActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().landingLightVar, args, 0);
    }

    @LuaFunction
    public final boolean getNavLightsActive() throws LuaException {
        return this.getVehicle().navigationLightVar.isActive;
    }

    @LuaFunction
    public final void setNavLightsActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().navigationLightVar, args, 0);
    }

    @LuaFunction
    public final boolean getRunningLightActive() throws LuaException {
        return this.getVehicle().runningLightVar.isActive;
    }

    @LuaFunction
    public final void setRunningLightActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().runningLightVar, args, 0);
    }

    @LuaFunction
    public final boolean getStrobeLightActive() throws LuaException {
        return this.getVehicle().strobeLightVar.isActive;
    }

    @LuaFunction
    public final void setStrobeLightActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().strobeLightVar, args, 0);
    }

    @LuaFunction
    public final boolean getTaxiLightActive() throws LuaException {
        return this.getVehicle().taxiLightVar.isActive;
    }

    @LuaFunction
    public final void setTaxiLightActive(IArguments args) throws LuaException {
        toggleActive(this.getVehicle().taxiLightVar, args, 0);
    }
}
