package io.github.techtastic.computerized_vehicles.api.misc;

import dan200.computercraft.api.lua.LuaFunction;
import io.github.techtastic.computerized_vehicles.util.LuaConversions;
import minecrafttransportsimulator.baseclasses.BezierCurve;

import java.util.Map;

public class LuaBezierCurve {
    private final BezierCurve curve;

    public LuaBezierCurve(BezierCurve curve) {
        this.curve = curve;
    }

    @LuaFunction
    public final Map<String, Double> getStartPosition() {
        return LuaConversions.toLua(this.curve.startPos);
    }

    @LuaFunction
    public final Map<String, Double> getEndPosition() {
        return LuaConversions.toLua(this.curve.endPos);
    }

    @LuaFunction
    public final Map<String, Double> getStartRotation() {
        return LuaConversions.toLua(this.curve.startRotation.angles);
    }

    @LuaFunction
    public final Map<String, Double> getEndRotation() {
        return LuaConversions.toLua(this.curve.endRotation.angles);
    }

    @LuaFunction(value = {"__len", "getPathLength"})
    public final double getPathLength() {
        return this.curve.pathLength;
    }
}
