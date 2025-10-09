package io.github.techtastic.cc_vehicles.mixin;

import io.github.techtastic.cc_vehicles.networking.packet.PacketEntityColorChangeComputer;
import minecrafttransportsimulator.mcinterface.InterfaceManager;
import minecrafttransportsimulator.packets.components.APacketBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(APacketBase.class)
public class MixinAPacketBase {
    @Inject(method = "initPackets", at = @At("TAIL"), remap = false)
    private static void cc_vehicles$addNewPackets(byte packetIndex, CallbackInfo ci) {
        InterfaceManager.packetInterface.registerPacket(packetIndex++, PacketEntityColorChangeComputer.class);
    }
}
