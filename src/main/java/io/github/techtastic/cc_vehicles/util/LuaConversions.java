package io.github.techtastic.cc_vehicles.util;

import minecrafttransportsimulator.baseclasses.Point3D;

import java.util.Map;

public class LuaConversions {
    public static Map<String, Double> toLua(Point3D point) {
        return Map.of(
                "x", point.x,
                "y", point.y,
                "z", point.z
        );
    }
}
