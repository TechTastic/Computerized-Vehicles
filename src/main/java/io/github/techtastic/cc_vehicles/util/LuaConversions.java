package io.github.techtastic.cc_vehicles.util;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaValues;
import mcinterface1201.BuilderItem;
import mcinterface1201.WrapperItemStack;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.baseclasses.Point3D;
import minecrafttransportsimulator.baseclasses.RotationMatrix;
import minecrafttransportsimulator.entities.components.AEntityA_Base;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.items.instances.ItemPartEffector;
import minecrafttransportsimulator.items.instances.ItemVehicle;
import minecrafttransportsimulator.jsondefs.AJSONItem;
import minecrafttransportsimulator.jsondefs.AJSONPartProvider;
import minecrafttransportsimulator.jsondefs.JSONDummyPartProvider;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.packloading.JSONParser;
import minecrafttransportsimulator.packloading.PackParser;
import net.minecraft.client.particle.AshParticle;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class LuaConversions {
    public static AItemPart getPart(IArguments args, int index) throws LuaException {
        String str = args.getString(index);
        ResourceLocation location = ResourceLocation.tryParse(str);
        if (location == null)
            throw LuaValues.badArgument(index, "item ID", str);

        Optional<Holder.Reference<Item>> optItem = ForgeRegistries.ITEMS.getDelegate(location);
        if (optItem.isEmpty())
            throw LuaValues.badArgument(index, "valid item ID", location.toString());

        Item item = optItem.get().value();
        if (!(item instanceof BuilderItem builderItem))
            throw LuaValues.badArgument(index, "valid IV/MTS item", location.toString());
        if (!(builderItem.getWrappedItem() instanceof AItemPart partItem))
            throw LuaValues.badArgument(index, "valid IV/MTS part item", location.toString());

        return partItem;
    }

    public static EntityVehicleF_Physics getVehicle(IArguments args, int index, WrapperWorld world) throws LuaException {
        String str = args.getString(index);
        try {
            UUID uniqueUUID = UUID.fromString(str);
            AEntityA_Base entity = world.getEntity(uniqueUUID);
            if (entity instanceof EntityVehicleF_Physics vehicle)
                return vehicle;
            throw LuaValues.badArgument(index, "unique UUID of vehicle", uniqueUUID.toString());
        } catch (IllegalArgumentException e) {
            ResourceLocation location = ResourceLocation.tryParse(str);
            if (location == null)
                throw LuaValues.badArgument(index, "item ID", str);

            Optional<Holder.Reference<Item>> optItem = ForgeRegistries.ITEMS.getDelegate(location);
            if (optItem.isEmpty())
                throw LuaValues.badArgument(index, "valid item ID", location.toString());

            Item item = optItem.get().value();
            if (!(item instanceof BuilderItem builderItem))
                throw LuaValues.badArgument(index, "valid IV/MTS item", location.toString());
            if (!(builderItem.getWrappedItem() instanceof ItemVehicle itemVehicle))
                throw LuaValues.badArgument(index, "valid IV/MTS vehicle item", location.toString());

            return new EntityVehicleF_Physics(world, null, itemVehicle, null);
        }
    }

    public static UUID getUUID(IArguments args, int index) throws LuaException {
        String str = args.getString(index);
        try {
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            throw LuaValues.badArgument(index, "UUID", str);
        }
    }

    public static Point3D optPoint(IArguments args, int index, Point3D def) throws LuaException {
        Optional<Map<?, ?>> optTable = args.optTable(index);
        if (optTable.isEmpty())
            return def;
        Map<?, ?> table = optTable.get();
        if (!table.containsKey("x") || !(table.get("x") instanceof Double x))
            throw LuaValues.badField("x", "number", table.get("x").getClass().toString());
        if (!table.containsKey("y") || !(table.get("y") instanceof Double y))
            throw LuaValues.badField("y", "number", table.get("y").getClass().toString());
        if (!table.containsKey("z") || !(table.get("z") instanceof Double z))
            throw LuaValues.badField("z", "number", table.get("z").getClass().toString());
        return new Point3D(x, y, z);
    }

    public static Map<String, Double> toLua(Point3D point) {
        return Map.of(
                "x", point.x,
                "y", point.y,
                "z", point.z
        );
    }

    public static Map<String, Object> toLuaAny(Object object) {
        Map<String, Object> table = new HashMap<>();

        Class<?> clazz = object.getClass();
        for (Field field : clazz.getFields()) {
            if (!field.canAccess(object) || field.isAnnotationPresent(Deprecated.class) || Modifier.isStatic(field.getModifiers()))
                continue;

            if (field.getType().isPrimitive()) {
                Object o = handlePrimitive(field, object);
                if (o == null)
                    continue;
                table.put(field.getName(), o);
            }
            try {
                table.put(field.getName(), handleNonPrimitive(field.get(object)));
            }  catch (Exception ignored) {}
        }

        return table;
    }

    private static Object handlePrimitive(Field field, Object o) {
        try {
            return field.getBoolean(o);
        } catch (Exception ignored) {}
        try {
            return field.getByte(o);
        } catch (Exception ignored) {}
        try {
            return field.getShort(o);
        } catch (Exception ignored) {}
        try {
            return field.getInt(o);
        } catch (Exception ignored) {}
        try {
            return field.getLong(o);
        } catch (Exception ignored) {}
        try {
            return field.getDouble(o);
        } catch (Exception ignored) {}
        try {
            return (double) field.getFloat(o);
        } catch (Exception ignored) {}
        try {
            return field.getChar(o);
        } catch (Exception ignored) {}
        return null;
    }

    private static Object convertObject(Object o) {
        if (o.getClass().isPrimitive())
            return handlePrimitive(o);
        return handleNonPrimitive(o);
    }

    private static Object handlePrimitive(Object o) {
        try {
            return (double) Float.parseFloat(o.toString());
        } catch (Exception ignored) {
            return o;
        }
    }

    private static Object handleNonPrimitive(Object o) {
        if (o instanceof String)
            return o;
        else if (o instanceof Map<?,?> map) {
            Map<String, Object> newMap = new HashMap<>();
            map.forEach((k, v) -> newMap.put(k.toString(), LuaConversions.convertObject(v)));
            return newMap;
        }
        else if (o instanceof List<?> list)
            return list.stream().map(LuaConversions::convertObject).toList();
        else if (o instanceof Object[] arr)
            return Arrays.stream(arr).map(LuaConversions::convertObject).toArray();
        else if (o instanceof Point3D point)
            return LuaConversions.toLua(point);
        else if (o instanceof RotationMatrix matrix)
            return LuaConversions.toLua(matrix.angles);
        else
            return toLuaAny(o);
    }
}
