package io.github.techtastic.computerized_vehicles;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dan200.computercraft.impl.Peripherals;
import io.github.techtastic.computerized_vehicles.peripheral.RoadPeripheral;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.blocks.tileentities.components.ATileEntityBase;
import minecrafttransportsimulator.blocks.tileentities.instances.TileEntityRoad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

public class CVPeripheralProvider implements IPeripheralProvider {
    @Override
    public LazyOptional<IPeripheral> getPeripheral(Level level, BlockPos blockPos, Direction direction) {
        WrapperWorld world = WrapperWorld.getWrapperFor(level);
        Point3D pos = new Point3D(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        ATileEntityBase<?> te = world.getTileEntity(pos);
        BlockEntity be = level.getBlockEntity(blockPos);
        if (te instanceof TileEntityRoad)
            return LazyOptional.of(() -> new RoadPeripheral(world, pos));
        return LazyOptional.empty();
    }

    public static void register() {
        Peripherals.register(new CVPeripheralProvider());
    }
}
