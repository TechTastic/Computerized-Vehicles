package io.github.techtastic.computerized_vehicles.mixin;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.util.ComponentMap;
import dan200.computercraft.shared.util.IDAssigner;
import io.github.techtastic.computerized_vehicles.ComputerizedVehicles;
import io.github.techtastic.computerized_vehicles.util.ReflectionUtils;
import mcinterface1201.WrapperWorld;
import minecrafttransportsimulator.entities.components.AEntityF_Multipart;
import minecrafttransportsimulator.entities.instances.PartInteractable;
import minecrafttransportsimulator.items.instances.ItemPartInteractable;
import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.IWrapperNBT;
import minecrafttransportsimulator.mcinterface.IWrapperPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PartInteractable.class)
public abstract class MixinPartInteractable {
    @Unique
    private int computerId = -1;
    @Unique
    private UUID instanceId = null;
    @Unique
    private ServerComputer computer;

    @Unique
    private PartInteractable getPart() {
        return ((PartInteractable) ((Object) this));
    }

    @Unique
    private int getComputerID() {
        return this.computerId;
    }

    @Unique
    private UUID getInstanceId() {
        return this.instanceId;
    }

    @Unique
    private void updateComputerData(IWrapperNBT data) {
        if (data.hasKey("computerId"))
            computerId = data.getInteger("computerId");
        if (data.hasKey("instanceId"))
            instanceId = data.getUUID("instanceId");
    }

    @Unique
    private ServerComputer createOrUpkeepComputer(ServerLevel level) {
        ServerContext context = ServerContext.get(level.getServer());
        ServerComputer computer = context.registry().get(getInstanceId());
        if (computer == null) {
            if (getComputerID() < 0) {
                computerId = ComputerCraftAPI.createUniqueNumberedSaveDir(level.getServer(), IDAssigner.COMPUTER);
            }

            computer = new ServerComputer(
                    level, BlockPos.containing(getPart().position.x, getPart().position.y, getPart().position.z), getComputerID(), getPart().uniqueUUID.toString(),
                    ComputerFamily.ADVANCED, Config.computerTermWidth, Config.computerTermHeight, ComponentMap.empty()
            );

            instanceId = computer.register();

            // Add API here

            computer.turnOn();
        }

        return computer;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void computerized_vehicles$addComputer(AEntityF_Multipart<?> entityOn, IWrapperPlayer placingPlayer, JSONPartDefinition placementDefinition, ItemPartInteractable item, IWrapperNBT data, CallbackInfo ci) {
        boolean isComputer = getPart().definition.interactable.interactionType == JSONPart.InteractableComponentType.valueOf("COMPUTER");
        if (isComputer) {
            ComputerizedVehicles.LOGGER.info("Computer Initialized!");

            updateComputerData(data);

            if (ReflectionUtils.getLevel((WrapperWorld) entityOn.world) instanceof ServerLevel level) {
                createOrUpkeepComputer(level);
            }
        }
    }

    // interact - Send Packet, Handle GUI in packet?
    @Inject(method = "interact", at = @At("RETURN"), remap = false)
    private void computerized_vehicles$openComputerGUI(IWrapperPlayer player, CallbackInfoReturnable<Boolean> cir) {
        ComputerizedVehicles.LOGGER.info("Computer Interacted!");
    }

    @Inject(method = "update", at = @At("TAIL"), remap = false)
    private void computerized_vehicles$updateComputer(CallbackInfo ci) {
        ComputerizedVehicles.LOGGER.info("Computer Updated!");
        if (ReflectionUtils.getLevel((WrapperWorld) getPart().world) instanceof ServerLevel level) {
            ServerComputer computer = createOrUpkeepComputer(level);
            computer.keepAlive();
        }
    }

    @Inject(method = "save", at = @At("TAIL"), remap = false)
    private void computerized_vehicles$saveComputerInfo(IWrapperNBT data, CallbackInfoReturnable<IWrapperNBT> cir) {
        if (computerId >= 0)
            data.setInteger("computerId", computerId);
        if (instanceId != null)
            data.setUUID("instanceId", instanceId);
    }
}
