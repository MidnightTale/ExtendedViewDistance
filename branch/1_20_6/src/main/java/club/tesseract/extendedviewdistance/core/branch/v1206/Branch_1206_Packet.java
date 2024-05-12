package club.tesseract.extendedviewdistance.core.branch.v1206;

import club.tesseract.extendedviewdistance.api.branch.BranchChunk;
import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import club.tesseract.extendedviewdistance.api.branch.BranchPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public final class Branch_1206_Packet implements BranchPacket {

  private final Branch_1206_PacketHandleChunk handleChunk = new Branch_1206_PacketHandleChunk();
  private final Branch_1206_PacketHandleLightUpdate handleLightUpdate = new Branch_1206_PacketHandleLightUpdate();

  public void sendPacket(Player player, Packet<?> packet) {
    try {
      ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
      ServerGamePacketListenerImpl conn = serverPlayer.connection;
      try {
        Connection networkManager = conn.connection;
        networkManager.send(packet);
      }catch (IllegalAccessError ignored) {
        try {
          Field field = ServerCommonPacketListenerImpl.class.getDeclaredField("c");
          field.setAccessible(true);
          Connection networkManager = (Connection) field.get(conn);
          networkManager.send(packet);
        } catch (Exception spigotException) {
          throw new RuntimeException("Failed to send packet to player, Invalid Version Supported?", spigotException);
        }
      }
    } catch (IllegalArgumentException ignored) {}
  }

  public void sendViewDistance(Player player, int viewDistance) {
    sendPacket(player, new ClientboundSetChunkCacheRadiusPacket(viewDistance));
  }

  public void sendUnloadChunk(Player player, int chunkX, int chunkZ) {
    ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
    sendPacket(player, new ClientboundForgetLevelChunkPacket(chunkPos));
  }

  public Consumer<Player> sendChunkAndLight(
    BranchChunk chunk,
    BranchChunkLight light,
    boolean needTile,
    Consumer<Integer> consumeTraffic
  ) {
    FriendlyByteBuf serializer = new FriendlyByteBuf(
      Unpooled.buffer().writerIndex(0)
    );
    serializer.writeInt(chunk.getX());
    serializer.writeInt(chunk.getZ());
    this.handleChunk.write(
        serializer,
        ((Branch_1206_Chunk) chunk).getLevelChunk(),
        needTile
      );
    this.handleLightUpdate.write(serializer, (Branch_1206_ChunkLight) light);
    consumeTraffic.accept(serializer.readableBytes());
    ClientboundLevelChunkWithLightPacket packet = new ClientboundLevelChunkWithLightPacket(
            ((Branch_1206_Chunk) chunk).getLevelChunk(), ((Branch_1206_Chunk) chunk).getLightEngine(), null, null, false);
    try {
      packet.setReady(true);
    } catch (NoSuchMethodError ignored) {}
    return player -> sendPacket(player, packet);
  }

  public void sendKeepAlive(Player player, long id) {
    sendPacket(player, new ClientboundKeepAlivePacket(id));
  }
}
