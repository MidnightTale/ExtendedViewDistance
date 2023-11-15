package club.tesseract.extendedviewdistance.core.branch.v1171;

import club.tesseract.extendedviewdistance.api.branch.BranchChunk;
import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import club.tesseract.extendedviewdistance.api.branch.BranchMinecraft;
import club.tesseract.extendedviewdistance.api.branch.BranchNBT;
import io.netty.channel.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;

public final class Branch_17_Minecraft implements BranchMinecraft {
    public BranchNBT getChunkNBTFromDisk(World world, int chunkX, int chunkZ) throws IOException {
        ServerLevel worldServer = ((CraftWorld) world).getHandle();
        ServerChunkCache providerServer  = worldServer.chunkSource;
        ChunkMap chunkMap = providerServer.chunkMap;
        CompoundTag nbtTagCompound = chunkMap.read(new ChunkPos(chunkX, chunkZ));
        return nbtTagCompound != null ? new Branch_1171_NBT(nbtTagCompound) : null;
    }

    public BranchChunk getChunkFromMemoryCache(World world, int chunkX, int chunkZ) {
        WorldServer         worldServer     = ((CraftWorld) world).getHandle();
        ChunkProviderServer providerServer  = worldServer.getChunkProvider();
        PlayerChunkMap      playerChunkMap  = providerServer.a;
        PlayerChunk         playerChunk     = playerChunkMap.getVisibleChunk(ChunkCoordIntPair.pair(chunkX, chunkZ));
        if (playerChunk != null) {
            ChunkAccess        chunk           = playerChunk.f();
            if (chunk != null && !(chunk instanceof EmptyLevelChunk) && chunk instanceof Chunk)
                return new Branch_1171_Chunk(worldServer, chunk);
        }
        return null;
    }

    public BranchChunk fromChunk(World world, int chunkX, int chunkZ, BranchNBT nbt, boolean integralHeightmap) {
        return Branch_17_ChunkRegionLoader.loadChunk(((CraftWorld) world).getHandle(), new ChunkPos(chunkX, chunkZ), ((Branch_1171_NBT) nbt).getNMSTag(), integralHeightmap);
    }

    public BranchChunkLight fromLight(World world, BranchNBT nbt) {
        return Branch_17_ChunkRegionLoader.loadLight(((CraftWorld) world).getHandle(), ((Branch_1171_NBT) nbt).getNMSTag());
    }
    public BranchChunkLight fromLight(World world) {
        return new Branch_1171_ChunkLight(((CraftWorld) world).getHandle());
    }

    public BranchChunk.Status fromStatus(BranchNBT nbt) {
        return Branch_17_ChunkRegionLoader.loadStatus(((Branch_1171_NBT) nbt).getNMSTag());
    }

    public BranchChunk fromChunk(World world, org.bukkit.Chunk chunk) {
        return new Branch_1171_Chunk(((CraftChunk) chunk).getCraftWorld().getHandle(), ((CraftChunk) chunk).getHandle());
    }

    public int getPlayerPing(Player player) {
        return  ((CraftPlayer) player).getHandle().e;
    }

    public void injectPlayer(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        ServerGamePacketListenerImpl connection = entityPlayer.connection;
        Connection network = connection.connection;
        Channel channel = network.channel;
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addAfter("packet_handler", "farther_view_distance_write", new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_1171_ProxyPlayerConnection.write(player, (Packet<?>) msg))
                        return;
                }
                super.write(ctx, msg, promise);
            }
        });
        pipeline.addAfter("encoder", "farther_view_distance_read", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof Packet) {
                    if (!Branch_1171_ProxyPlayerConnection.read(player, (Packet<?>) msg))
                        return;
                }
                super.channelRead(ctx, msg);
            }
        });
    }
}
