package io.github.techtastic.computerized_vehicles.api.misc;

import dan200.computercraft.api.lua.LuaFunction;
import minecrafttransportsimulator.blocks.tileentities.components.RoadLane;

import java.util.List;

public class LuaRoadLane {
    private final RoadLane lane;

    public LuaRoadLane(RoadLane lane) {
        this.lane = lane;
    }

    @LuaFunction
    public final int getLaneNumber() {
        return this.lane.laneNumber;
    }

    @LuaFunction
    public final int getSectorLaneNumber() {
        return this.lane.sectorLaneNumber;
    }

    @LuaFunction
    public final int getSectorNumber() {
        return this.lane.sectorNumber;
    }

    @LuaFunction
    public final List<LuaBezierCurve> getCurves() {
        return this.lane.curves.stream().map(LuaBezierCurve::new).toList();
    }
}
