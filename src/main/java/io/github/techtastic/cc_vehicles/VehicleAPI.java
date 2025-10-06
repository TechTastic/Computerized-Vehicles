package io.github.techtastic.cc_vehicles;

import dan200.computercraft.api.lua.*;
import io.github.techtastic.cc_vehicles.util.LuaConversions;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.entities.instances.PartEngine;
import minecrafttransportsimulator.jsondefs.JSONAction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
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
    public Vehicle getVehicle(String uniqueId) throws LuaException {
        UUID vehicleId = UUID.fromString(uniqueId);
        WrapperWorld world = getWrapperWorld();
        if (!(world.getEntity(vehicleId) instanceof EntityVehicleF_Physics vehicle))
            throw new LuaException("Invalid Vehicle ID! No vehicle exits with this Unique ID!");
        return new Vehicle(vehicle);
    }

    public static class Vehicle {
        private final EntityVehicleF_Physics vehicle;
        private Vehicle(EntityVehicleF_Physics vehicle) {
            this.vehicle = vehicle;
        }

        @LuaFunction
        public String getUniqueId() {
            return this.vehicle.uniqueUUID.toString();
        }

        @LuaFunction
        public Map<String, Double> getMotion() {
            return LuaConversions.toLua(this.vehicle.motion);
        }

        @LuaFunction
        public Map<String, Double> getPosition() {
            return LuaConversions.toLua(this.vehicle.position);
        }

        @LuaFunction
        public Map<String, Double> getOrientation() {
            return LuaConversions.toLua(this.vehicle.orientation.angles);
        }
    }
}
