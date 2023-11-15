package club.tesseract.extendedviewdistance.core.branch.v1171;

import club.tesseract.extendedviewdistance.api.branch.packet.PacketKeepAliveEvent;
import club.tesseract.extendedviewdistance.api.branch.packet.PacketMapChunkEvent;
import club.tesseract.extendedviewdistance.api.branch.packet.PacketUnloadChunkEvent;
import club.tesseract.extendedviewdistance.api.branch.packet.PacketViewDistanceEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public final class Branch_1171_ProxyPlayerConnection {
    public static boolean read(Player player, Packet<?> packet) {
        if (packet instanceof ClientboundKeepAlivePacket) {
            PacketKeepAliveEvent event = new PacketKeepAliveEvent(player, ((ClientboundKeepAlivePacket) packet).getId());
            Bukkit.getPluginManager().callEvent(event);
            return !event.isCancelled();
        } else {
            return true;
        }
    }


    private static Field field_PacketPlayOutUnloadChunk_chunkX;
    private static Field field_PacketPlayOutUnloadChunk_chunkZ;
    private static Field field_PacketPlayOutViewDistance_distance;
    private static Field field_PacketPlayOutMapChunk_chunkX;
    private static Field field_PacketPlayOutMapChunk_chunkZ;
    static {
        try {
            field_PacketPlayOutUnloadChunk_chunkX = ClientboundForgetLevelChunkPacket.class.getDeclaredField("a");
            field_PacketPlayOutUnloadChunk_chunkZ = ClientboundForgetLevelChunkPacket.class.getDeclaredField("b");
            field_PacketPlayOutViewDistance_distance = ClientboundSetChunkCacheRadiusPacket.class.getDeclaredField("a");
            field_PacketPlayOutMapChunk_chunkX = ClientboundLevelChunkPacket.class.getDeclaredField("a");
            field_PacketPlayOutMapChunk_chunkZ = ClientboundLevelChunkPacket.class.getDeclaredField("b");
            field_PacketPlayOutUnloadChunk_chunkX.setAccessible(true);
            field_PacketPlayOutUnloadChunk_chunkZ.setAccessible(true);
            field_PacketPlayOutViewDistance_distance.setAccessible(true);
            field_PacketPlayOutMapChunk_chunkX.setAccessible(true);
            field_PacketPlayOutMapChunk_chunkZ.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static boolean write(Player player, Packet<?> packet) {
        try {
            if (packet instanceof ClientboundForgetLevelChunkPacket) {
                PacketUnloadChunkEvent event = new PacketUnloadChunkEvent(player, field_PacketPlayOutUnloadChunk_chunkX.getInt(packet), field_PacketPlayOutUnloadChunk_chunkZ.getInt(packet));
                Bukkit.getPluginManager().callEvent(event);
                return !event.isCancelled();
            } else if (packet instanceof ClientboundSetChunkCacheRadiusPacket) {
                PacketViewDistanceEvent event = new PacketViewDistanceEvent(player, field_PacketPlayOutViewDistance_distance.getInt(packet));
                Bukkit.getPluginManager().callEvent(event);
                return !event.isCancelled();
            } else if (packet instanceof ClientboundLevelChunkPacket) {
                PacketMapChunkEvent event = new PacketMapChunkEvent(player, field_PacketPlayOutMapChunk_chunkX.getInt(packet), field_PacketPlayOutMapChunk_chunkZ.getInt(packet));
                Bukkit.getPluginManager().callEvent(event);
                return !event.isCancelled();
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
