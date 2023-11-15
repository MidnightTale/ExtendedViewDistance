package club.tesseract.extendedviewdistance.core.branch.v1201;

import club.tesseract.extendedviewdistance.api.branch.BranchChunk;
import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import club.tesseract.extendedviewdistance.api.branch.BranchMinecraft;
import club.tesseract.extendedviewdistance.api.branch.BranchNBT;
import io.netty.channel.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("resource")
public final class Branch_1201_Minecraft implements BranchMinecraft {

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchNBT getChunkNBTFromDisk(World world, int chunkX, int chunkZ)
    throws IOException {
    CompoundTag nbt = null;
    try {
      CompletableFuture<Optional<CompoundTag>> futureNBT =
        (((CraftWorld) world).getHandle()).getChunkSource()
          .chunkMap.read(new ChunkPos(chunkX, chunkZ));
      Optional<CompoundTag> optionalNBT = futureNBT.get();
      nbt = optionalNBT.orElse(null);
    } catch (InterruptedException | ExecutionException ignored) {}
    return nbt != null ? new Branch_1201_NBT(nbt) : null;
  }

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchChunk getChunkFromMemoryCache(
    World world,
    int chunkX,
    int chunkZ
  ) {
    try {
      // 適用於 paper
      ServerLevel level = ((CraftWorld) world).getHandle();
      ChunkHolder playerChunk = level
        .getChunkSource()
        .chunkMap.getVisibleChunkIfPresent(
          (long) chunkZ << 32 | (long) chunkX & 4294967295L
        );
      if (playerChunk != null) {
        ChunkAccess chunk = playerChunk.getAvailableChunkNow();
        if (
          chunk != null &&
          !(chunk instanceof EmptyLevelChunk) &&
          chunk instanceof LevelChunk
        ) {
          return new Branch_1201_Chunk(level, (LevelChunk) chunk);
        }
      }
      return null;
    } catch (NoSuchMethodError ignored) {
      return null;
    }
  }

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchChunk fromChunk(
    World world,
    int chunkX,
    int chunkZ,
    BranchNBT nbt,
    boolean integralHeightmap
  ) {
    return Branch_1201_ChunkRegionLoader.loadChunk(
      ((CraftWorld) world).getHandle(),
      chunkX,
      chunkZ,
      ((Branch_1201_NBT) nbt).getNMSTag(),
      integralHeightmap
    );
  }

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchChunkLight fromLight(World world, BranchNBT nbt) {
    return Branch_1201_ChunkRegionLoader.loadLight(
      ((CraftWorld) world).getHandle(),
      ((Branch_1201_NBT) nbt).getNMSTag()
    );
  }

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchChunkLight fromLight(World world) {
    return new Branch_1201_ChunkLight(((CraftWorld) world).getHandle());
  }

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchChunk.Status fromStatus(BranchNBT nbt) {
    return Branch_1201_ChunkRegionLoader.loadStatus(
      ((Branch_1201_NBT) nbt).getNMSTag()
    );
  }

  /**
   * 參考 XuanCatAPI.CodeExtendWorld
   */
  public BranchChunk fromChunk(World world, org.bukkit.Chunk chunk) {
    return new Branch_1201_Chunk(
      ((CraftChunk) chunk).getCraftWorld().getHandle(),
      (LevelChunk) ((CraftChunk) chunk).getHandle(ChunkStatus.FULL)
    );
  }

  public void injectPlayer(Player player) {
    ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    ServerGamePacketListenerImpl connection = entityPlayer.connection;

    Connection rawConnection;
    try {
      rawConnection = connection.connection;
    }catch (IllegalAccessError ignored) {
      try {
        Field field = ServerGamePacketListenerImpl.class.getDeclaredField("c");
        field.setAccessible(true);
        rawConnection = (Connection) field.get(connection);
      }catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException("Cannot inject player", e);
      }
    }

    final Channel channel = rawConnection.channel;
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addAfter(
      "packet_handler",
      "farther_view_distance_write",
      new ChannelDuplexHandler() {
        @Override
        public void write(
          ChannelHandlerContext ctx,
          Object msg,
          ChannelPromise promise
        ) throws Exception {
          if (msg instanceof Packet) {
            if (
              !Branch_1201_ProxyPlayerConnection.write(player, (Packet<?>) msg)
            ) return;
          }
          super.write(ctx, msg, promise);
        }
      }
    );
    pipeline.addAfter(
      "encoder",
      "farther_view_distance_read",
      new ChannelInboundHandlerAdapter() {
        @Override
        public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg)
          throws Exception {
          if (msg instanceof Packet) {
            if (
              !Branch_1201_ProxyPlayerConnection.read(player, (Packet<?>) msg)
            ) return;
          }
          super.channelRead(ctx, msg);
        }
      }
    );
  }
}
