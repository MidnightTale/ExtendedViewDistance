package club.tesseract.extendedviewdistance.core.branch.v1194;

import club.tesseract.extendedviewdistance.api.branch.BranchChunkLight;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;

import java.util.Arrays;

public final class Branch_1194_ChunkLight implements BranchChunkLight {
    public static final byte[] EMPTY = new byte[0];

    private final ServerLevel worldServer;
    private final byte[][] blockLights;
    private final byte[][] skyLights;

    public Branch_1194_ChunkLight(World world) {
        this(((CraftWorld) world).getHandle());
    }
    public Branch_1194_ChunkLight(ServerLevel worldServer) {
        this(worldServer, new byte[worldServer.getSectionsCount() + 2][], new byte[worldServer.getSectionsCount() + 2][]);
    }
    public Branch_1194_ChunkLight(ServerLevel worldServer, byte[][] blockLights, byte[][] skyLights) {
        this.worldServer = worldServer;
        this.blockLights = blockLights;
        this.skyLights = skyLights;
        Arrays.fill(blockLights, EMPTY);
        Arrays.fill(skyLights, EMPTY);
    }

    public ServerLevel getWorldServer() {
        return worldServer;
    }

    public int getArrayLength() {
        return blockLights.length;
    }

    public static int indexFromSectionY(ServerLevel worldServer, int sectionY) {
        return sectionY - worldServer.getMinSection() + 1;
    }

    public void setBlockLight(int sectionY, byte[] blockLight) {
        blockLights[indexFromSectionY(worldServer, sectionY)] = blockLight;
    }
    public void setSkyLight(int sectionY, byte[] skyLight) {
        skyLights[indexFromSectionY(worldServer, sectionY)] = skyLight;
    }

    public byte[] getBlockLight(int sectionY) {
        return blockLights[indexFromSectionY(worldServer, sectionY)];
    }
    public byte[] getSkyLight(int sectionY) {
        return skyLights[indexFromSectionY(worldServer, sectionY)];
    }

    public byte[][] getBlockLights() {
        return blockLights;
    }
    public byte[][] getSkyLights() {
        return skyLights;
    }
}
