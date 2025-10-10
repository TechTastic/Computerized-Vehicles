package io.github.techtastic.computerized_vehicles.api;

import dan200.computercraft.api.lua.*;
import io.github.techtastic.computerized_vehicles.util.LuaConversions;
import mcinterface1201.*;
import minecrafttransportsimulator.baseclasses.BoundingBox;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import minecrafttransportsimulator.entities.components.AEntityB_Existing;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.items.instances.ItemBullet;
import minecrafttransportsimulator.items.instances.ItemVehicle;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
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
    public final Vehicle getVehicle(IArguments args) throws LuaException {
        return new Vehicle(LuaConversions.getVehicle(args, 0, getWrapperWorld()));
    }

    @LuaFunction
    public final List<String> getAllPossibleVehicles() {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BuilderItem item && item.getWrappedItem() instanceof ItemVehicle)
                .map(entry -> entry.getKey().location().toString()).toList();
    }

    @LuaFunction
    public final List<String> getAllPossibleParts() {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BuilderItem item && item.getWrappedItem() instanceof AItemPart)
                .map(entry -> entry.getKey().location().toString()).toList();
    }

    @LuaFunction
    public final List<String> getAllPossibleBullets() {
        return ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BuilderItem item && item.getWrappedItem() instanceof ItemBullet)
                .map(entry -> entry.getKey().location().toString()).toList();
    }

    @LuaFunction
    public final List<Vehicle> getAllVehiclesInArea(IArguments args) throws LuaException {
        Point3D min = LuaConversions.optPoint(args, 0, new Point3D());
        Point3D max = LuaConversions.optPoint(args, 1, new Point3D());
        BoundingBox area = new BoundingBox(min, max);
        return getWrapperWorld().getEntitiesExtendingType(EntityVehicleF_Physics.class).stream()
                .filter(phys -> area.isPointInside(phys.position, new Point3D())).map(Vehicle::new).toList();
    }

    @LuaFunction
    public final Vehicle spawnVehicle(IArguments args) throws LuaException {
        WrapperWorld world = getWrapperWorld();

        IWrapperNBT nbt;
        try {
            Class<?> clazz = ClassLoader.getPlatformClassLoader().loadClass("mcinterface1201.WrapperNBT");
            nbt = (IWrapperNBT) clazz.newInstance();
        } catch (Exception ignored) {
            nbt = null;
        }

        EntityVehicleF_Physics vehicle = new EntityVehicleF_Physics(world, null, LuaConversions.getMTSItem(args, 0, ItemVehicle.class), nbt);
        Direction facing = this.getLevel().getBlockState(this.getPosition()).getValue(BlockStateProperties.HORIZONTAL_FACING);
        Vec3 vec = this.getPosition().getCenter().relative(Direction.UP, vehicle.boundingBox.heightRadius + .5).relative(facing, 0.5);
        Point3D pos = LuaConversions.optPoint(args, 1, new Point3D(vec.x, vec.y, vec.z));
        Point3D angles = LuaConversions.optPoint(args, 2, new Point3D(0, facing.toYRot(), 0));
        Point3D motion = LuaConversions.optPoint(args, 3, new Point3D());
        vehicle.position.set(pos);
        vehicle.prevPosition.set(new Point3D());
        vehicle.orientation.setToAngles(angles);
        vehicle.prevOrientation.setToAngles(new Point3D());
        vehicle.motion.set(motion);
        vehicle.prevMotion.set(new Point3D());
        if (vehicle.containsVariable("mudAcc"))
            vehicle.getOrCreateVariable("mudAcc").setTo(args.optDouble(4, 0), false);
        world.spawnEntity(vehicle);
        vehicle.addPartsPostAddition(null, nbt);

        return new Vehicle(vehicle);
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
        this.getLevel().getEntitiesOfClass(BuilderEntityExisting.class, AABB.ofSize(new Vec3(
                existing.boundingBox.globalCenter.x, existing.boundingBox.globalCenter.y, existing.boundingBox.globalCenter.z),
                existing.boundingBox.widthRadius * 2, existing.boundingBox.heightRadius * 2, existing.boundingBox.depthRadius * 2)
        ).stream().filter(e -> {
            WrapperEntity wrapper = world.getExternalEntity(e.getUUID());
            if (wrapper == null)
                return false;
            IWrapperNBT data = wrapper.getData();
            return data.hasKey("uniqueUUID") && data.getUUID("uniqueUUID").equals(existing.uniqueUUID);
        }).forEach(e -> e.remove(Entity.RemovalReason.DISCARDED));
    }
}
