package club.tesseract.extendedviewdistance.core.branch.v1201;

import club.tesseract.extendedviewdistance.api.branch.BranchChunk;
import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import club.tesseract.extendedviewdistance.api.branch.BranchPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class Branch_1201_Packet implements BranchPacket {

  private final Branch_1201_PacketHandleChunk handleChunk = new Branch_1201_PacketHandleChunk();
  private final Branch_1201_PacketHandleLightUpdate handleLightUpdate = new Branch_1201_PacketHandleLightUpdate();

  public void sendPacket(Player player, Packet<?> packet) {
    try {
      ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
      ServerGamePacketListenerImpl conn = serverPlayer.connection;
      conn.send(packet);
    } catch (IllegalArgumentException ignored) {}
  }

  public void sendViewDistance(Player player, int viewDistance) {
    sendPacket(player, new ClientboundSetChunkCacheRadiusPacket(viewDistance));
  }

  public void sendUnloadChunk(Player player, int chunkX, int chunkZ) {
    sendPacket(player, new ClientboundForgetLevelChunkPacket(chunkX, chunkZ));
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
        ((Branch_1201_Chunk) chunk).getLevelChunk(),
        needTile
      );
    this.handleLightUpdate.write(serializer, (Branch_1201_ChunkLight) light);
    consumeTraffic.accept(serializer.readableBytes());
    ClientboundLevelChunkWithLightPacket packet = new ClientboundLevelChunkWithLightPacket(
      serializer
    );
    try {
      // 適用於 paper
      packet.setReady(true);
    } catch (NoSuchMethodError noSuchMethodError) {
      // 適用於 spigot (不推薦)
    }
    return player -> sendPacket(player, packet);
  }

  public void sendKeepAlive(Player player, long id) {
    sendPacket(player, new ClientboundKeepAlivePacket(id));
  }
}
