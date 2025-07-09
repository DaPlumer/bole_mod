package net.daplumer.more_gems.entity.custom;

import net.daplumer.more_gems.MoreGems;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record BoleClientPacket(byte index, byte data,UUID boleUUID) implements CustomPayload {
    public static final CustomPayload.Id<BoleClientPacket> ID = new CustomPayload.Id<>(Identifier.of(MoreGems.MOD_ID, "bole_data"));
    public static final PacketCodec<RegistryByteBuf, BoleClientPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.BYTE, BoleClientPacket::index,
            PacketCodecs.BYTE, BoleClientPacket::data,
            Uuids.PACKET_CODEC,BoleClientPacket::boleUUID,
            BoleClientPacket::new
    );
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}