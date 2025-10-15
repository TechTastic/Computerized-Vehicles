package io.github.techtastic.computerized_vehicles.util;

import mcinterface1201.WrapperPlayer;
import mcinterface1201.WrapperWorld;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ReflectionUtils {
    @Nullable
    public static Level getLevel(WrapperWorld world) {
        try {
            Class<WrapperWorld> clazz = WrapperWorld.class;
            Field field = clazz.getDeclaredField("world");
            return (Level) field.get(world);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    @Nullable
    public static Player getPlayer(WrapperPlayer player) {
        try {
            Class<WrapperPlayer> clazz = WrapperPlayer.class;
            Field field = clazz.getDeclaredField("player");
            return (Player) field.get(player);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
