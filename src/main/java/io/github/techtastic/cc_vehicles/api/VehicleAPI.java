package io.github.techtastic.cc_vehicles.api;

import dan200.computercraft.api.lua.*;
import io.github.techtastic.cc_vehicles.util.LuaConversions;
import mcinterface1201.BuilderItem;
import mcinterface1201.WrapperAABBCollective;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.BoundingBox;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import minecrafttransportsimulator.entities.components.AEntityB_Existing;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.items.instances.ItemVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class VehicleAPI implements ILuaAPI {
    private final IComputerSystem system;

    public VehicleAPI(IComputerSystem system) {
        this.system = system;
    }

    private ServerLevel getLevel() {
        return this.system.getLevel();
    }

    private BlockPos getPosition() {
        return this.system.getPosition();
    }

    private WrapperWorld getWrapperWorld() {
        return WrapperWorld.getWrapperFor(this.getLevel());
    }

    @Override
    public String[] getNames() {
        return new String[] {"vehicles"};
    }

    @LuaFunction
    public VehiclePhysics getVehicle(IArguments args) throws LuaException {
        return new VehiclePhysics(LuaConversions.getVehicle(args, 0, getWrapperWorld()));
    }

    @LuaFunction
    public List<String> getAllPossibleVehicles() {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BuilderItem item && item.getWrappedItem() instanceof ItemVehicle)
                .map(entry -> entry.getKey().location().toString()).toList();
    }

    @LuaFunction
    public List<String> getAllPossibleParts() {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BuilderItem item && item.getWrappedItem() instanceof AItemPart)
                .map(entry -> entry.getKey().location().toString()).toList();
    }

    @LuaFunction
    public List<VehiclePhysics> getAllVehiclesInArea(IArguments args) throws LuaException {
        Point3D min = LuaConversions.optPoint(args, 0, new Point3D());
        Point3D max = LuaConversions.optPoint(args, 1, new Point3D());
        BoundingBox area = new BoundingBox(min, max);
        return getWrapperWorld().getEntitiesExtendingType(EntityVehicleF_Physics.class).stream()
                .filter(phys -> area.isPointInside(phys.position, new Point3D())).map(VehiclePhysics::new).toList();
    }

    @LuaFunction
    public final VehiclePhysics spawnVehicle(IArguments args) throws LuaException {
        WrapperWorld world = getWrapperWorld();
        EntityVehicleF_Physics vehicle = LuaConversions.getVehicle(args, 0, world);

        Point3D pos = LuaConversions.optPoint(args, 1, new Point3D(this.getPosition().getX(), this.getPosition().getY() + 1, this.getPosition().getZ()));
        Point3D angles = LuaConversions.optPoint(args, 2, new Point3D());
        Point3D motion = LuaConversions.optPoint(args, 3, new Point3D());
        vehicle.position.set(pos);
        vehicle.prevPosition.set(new Point3D());
        vehicle.orientation.setToAngles(angles);
        vehicle.prevOrientation.setToAngles(new Point3D());
        vehicle.motion.set(motion);
        vehicle.prevMotion.set(new Point3D());
        world.spawnEntity(vehicle);

        return new VehiclePhysics(vehicle);
    }

    @LuaFunction
    public final void deleteVehicle(IArguments args) throws LuaException {
        WrapperWorld world = getWrapperWorld();
        UUID uniqueUUID = LuaConversions.getUUID(args, 0);
        AEntityA_Base entity = world.getEntity(uniqueUUID);
        if (entity == null)
            throw LuaValues.badArgument(0, "IV/MTS placed entity", "nil");
        if (!(entity instanceof AEntityB_Existing existing))
            throw LuaValues.badArgument(0, "IV/MTS placed entity", uniqueUUID.toString());
        world.getEntitiesWithin(existing.boundingBox).stream()
                .filter(wrapper ->
                        wrapper.getData().hasKey("uniqueUUID") && wrapper.getData().getUUID("uniqueUUID") == existing.uniqueUUID)
                .map(wrapper -> this.getLevel().getEntity(wrapper.getID()))
                .filter(Objects::nonNull)
                .forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
        world.removeEntity(existing);
    }
}
