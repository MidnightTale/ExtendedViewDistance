package club.tesseract.extendedviewdistance.core.branch.v1171;

import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

import java.util.Arrays;

public final class Branch_1171_ChunkLight implements BranchChunkLight {
    public static final byte[] EMPTY = new byte[0];

    private final ServerLevel serverLevel;
    private final byte[][] blockLights;
    private final byte[][] skyLights;

    public Branch_1171_ChunkLight(World world) {
        this(((CraftWorld) world).getHandle());
    }
    public Branch_1171_ChunkLight(ServerLevel worldServer) {
        this(worldServer, new byte[worldServer.getSectionsCount() + 2][], new byte[worldServer.getSectionsCount() + 2][]);
    }
    public Branch_1171_ChunkLight(ServerLevel worldServer, byte[][] blockLights, byte[][] skyLights) {
        this.serverLevel = worldServer;
        this.blockLights = blockLights;
        this.skyLights = skyLights;
        Arrays.fill(blockLights, EMPTY);
        Arrays.fill(skyLights, EMPTY);
    }

    public ServerLevel getServerLevel() {
        return serverLevel;
    }

    public int getArrayLength() {
        return blockLights.length;
    }

    public static int indexFromSectionY(ServerLevel worldServer, int sectionY) {
        return sectionY - worldServer.getMinSection() + 1;
    }

    public void setBlockLight(int sectionY, byte[] blockLight) {
        blockLights[indexFromSectionY(serverLevel, sectionY)] = blockLight;
    }
    public void setSkyLight(int sectionY, byte[] skyLight) {
        skyLights[indexFromSectionY(serverLevel, sectionY)] = skyLight;
    }

    public byte[] getBlockLight(int sectionY) {
        return blockLights[indexFromSectionY(serverLevel, sectionY)];
    }
    public byte[] getSkyLight(int sectionY) {
        return skyLights[indexFromSectionY(serverLevel, sectionY)];
    }

    public byte[][] getBlockLights() {
        return blockLights;
    }
    public byte[][] getSkyLights() {
        return skyLights;
    }
}