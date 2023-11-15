package club.tesseract.extendedviewdistance.core.branch.v1182;

import club.tesseract.extendedviewdistance.api.branch.BranchChunk;
import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import club.tesseract.extendedviewdistance.api.branch.BranchMinecraft;
import club.tesseract.extendedviewdistance.api.branch.BranchNBT;
import io.netty.channel.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;

public final class Branch_1182_Minecraft implements BranchMinecraft {
    /**
     * 參考 XuanCatAPI.CodeExtendWorld
     */
    public BranchNBT getChunkNBTFromDisk(World world, int chunkX, int chunkZ) throws IOException {
        CompoundTag nbtTagCompound = ((CraftWorld) world).getHandle().getChunkSource().chunkMap.read(new ChunkPos(chunkX, chunkZ));
        return nbtTagCompound != null ? new Branch_1182_NBT(nbtTagCompound) : null;
    }

    /**
     * 參考 XuanCatAPI.CodeExtendWorld
     */
    public BranchChunk getChunkFromMemoryCache(World world, int chunkX, int chunkZ) {
        try {
            // 適用於 paper
            ServerLevel level = ((CraftWorld) world).getHandle();
            ChunkHolder playerChunk = level.getChunkSource().chunkMap.getVisibleChunkIfPresent((long) chunkZ << 32 | (long) chunkX & 4294967295L);
            if (playerChunk != null) {
                ChunkAccess chunk = playerChunk.getAvailableChunkNow();
                if (chunk != null && !(chunk instanceof EmptyLevelChunk) && chunk instanceof LevelChunk) {
                    return new Branch_1182_Chunk(level, chunk);
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
    public BranchChunk fromChunk(World world, int chunkX, int chunkZ, BranchNBT nbt, boolean integralHeightmap) {
        return Branch_1182_ChunkRegionLoader.loadChunk(((CraftWorld) world).getHandle(), chunkX, chunkZ, ((Branch_1182_NBT) nbt).getNMSTag(), integralHeightmap);
    }

    /**
     * 參考 XuanCatAPI.CodeExtendWorld
     */
    public BranchChunkLight fromLight(World world, BranchNBT nbt) {
        return Branch_1182_ChunkRegionLoader.loadLight(((CraftWorld) world).getHandle(), ((Branch_1182_NBT) nbt).getNMSTag());
    }
    /**
     * 參考 XuanCatAPI.CodeExtendWorld
     */
    public BranchChunkLight fromLight(World world) {
        return new Branch_1182_ChunkLight(((CraftWorld) world).getHandle());
    }

    /**
     * 參考 XuanCatAPI.CodeExtendWorld
     */
    public BranchChunk.Status fromStatus(BranchNBT nbt) {
        return Branch_1182_ChunkRegionLoader.loadStatus(((Branch_1182_NBT) nbt).getNMSTag());
    }

    /**
     * 參考 XuanCatAPI.CodeExtendWorld
     */
    public BranchChunk fromChunk(World world, org.bukkit.Chunk chunk) {
        return new Branch_1182_Chunk(((CraftChunk) chunk).getCraftWorld().getHandle(), ((CraftChunk) chunk).getHandle());
    }

    public void injectPlayer(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        ServerGamePacketListenerImpl connection = entityPlayer.connection;
        Channel channel = connection.connection.channel;
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("packet_handler", "farther_view_distance_write", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_1182_ProxyPlayerConnection.write(player, (Packet<?>) msg))
                        return;
                }
                super.write(ctx, msg, promise);
            }
        });
        pipeline.addAfter("encoder", "farther_view_distance_read", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_1182_ProxyPlayerConnection.read(player, (Packet<?>) msg))
                        return;
                }
                super.channelRead(ctx, msg);
            }
        });
    }
}
