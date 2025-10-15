package io.github.techtastic.computerized_vehicles.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.blocks.tileentities.components.ATileEntityBase;
import minecrafttransportsimulator.blocks.tileentities.instances.TileEntityRoad;
import org.jetbrains.annotations.Nullable;

public class TileEntityPeripheral<T extends ATileEntityBase<?>> implements IPeripheral {
    private final String type;
    private final WrapperWorld world;
    private final Point3D pos;
    private T road;

    public TileEntityPeripheral(WrapperWorld world, Point3D pos, String type) {
        this.type = type;
        this.world = world;
        this.pos = pos;
    }

    protected T getTileEntity() {
        if (this.road == null)
            this.road = this.world.getTileEntity(this.pos);
        return this.road;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return iPeripheral instanceof TileEntityPeripheral<?> t && t.getTileEntity().uniqueUUID.equals(this.getTileEntity().uniqueUUID);
    }
}
