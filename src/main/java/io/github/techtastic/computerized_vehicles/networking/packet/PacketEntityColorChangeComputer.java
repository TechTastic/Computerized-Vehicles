package io.github.techtastic.computerized_vehicles.networking.packet;

import io.netty.buffer.ByteBuf;
import minecrafttransportsimulator.entities.components.AEntityD_Definable;
import minecrafttransportsimulator.entities.instances.EntityVehicleF_Physics;
import minecrafttransportsimulator.mcinterface.AWrapperWorld;
import minecrafttransportsimulator.packets.components.APacketEntity;
import java.nio.charset.StandardCharsets;

public class PacketEntityColorChangeComputer extends APacketEntity<AEntityD_Definable<?>> {
    private final String subName;

    public PacketEntityColorChangeComputer(AEntityD_Definable<?> entity, String subName) {
        super(entity);
        this.subName = subName;
    }

    public PacketEntityColorChangeComputer(ByteBuf buf) {
        super(buf);
        this.subName = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    public void writeToBuffer(ByteBuf buf) {
        super.writeToBuffer(buf);
        buf.writeInt(this.subName.length());
        buf.writeCharSequence(this.subName.subSequence(0, this.subName.length()), StandardCharsets.UTF_8);
    }

    @Override
    protected boolean handle(AWrapperWorld world, AEntityD_Definable<?> entity) {
        if (world.isClient() && entity instanceof EntityVehicleF_Physics vehicle) {
            vehicle.updateSubDefinition(this.subName);
            vehicle.parts.forEach(part -> part.updateTone(true));
        }
        return true;
    }
}
