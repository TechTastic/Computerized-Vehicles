package io.github.techtastic.computerized_vehicles.peripheral;

import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.computerized_vehicles.api.misc.LuaBezierCurve;
import io.github.techtastic.computerized_vehicles.api.misc.LuaRoadLane;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.blocks.tileentities.instances.TileEntityRoad;

import java.util.List;

public class RoadPeripheral extends TileEntityPeripheral<TileEntityRoad> {
    public RoadPeripheral(WrapperWorld world, Point3D pos) {
        super(world, pos, "road");
    }

    @LuaFunction
    public final List<LuaRoadLane> getLanes() {
        return this.getTileEntity().lanes.stream().map(LuaRoadLane::new).toList();
    }

    @LuaFunction
    public final LuaBezierCurve getDynamicCurve() {
        return new LuaBezierCurve(this.getTileEntity().dynamicCurve);
    }
}
