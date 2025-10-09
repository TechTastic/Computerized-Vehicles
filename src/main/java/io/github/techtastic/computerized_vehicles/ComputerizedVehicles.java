package io.github.techtastic.computerized_vehicles;

import com.mojang.logging.LogUtils;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.AdminComputer;
import dan200.computercraft.api.component.ComputerComponents;
import io.github.techtastic.computerized_vehicles.api.VehicleAPI;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ComputerizedVehicles.MODID)
public class ComputerizedVehicles {
    public static final String MODID = "computerized_vehicles";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ComputerizedVehicles(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        ComputerCraftAPI.registerAPIFactory(system -> {
            AdminComputer admin = system.getComponent(ComputerComponents.ADMIN_COMPUTER);
            if (admin != null)
                return new VehicleAPI(system);
            return null;
        });
    }
}
