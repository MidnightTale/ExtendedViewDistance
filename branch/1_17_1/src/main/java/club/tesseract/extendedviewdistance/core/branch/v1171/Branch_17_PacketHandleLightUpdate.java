package club.tesseract.extendedviewdistance.core.branch.v1171;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;

public final class Branch_17_PacketHandleLightUpdate {
    private Field field_cleaner1            = null;
    private Field field_cleaner2            = null;


    public Branch_17_PacketHandleLightUpdate() {
        Class<ClientboundLightUpdatePacket> atClass = ClientboundLightUpdatePacket.class;

        try {
            field_cleaner1          = atClass.getDeclaredField("cleaner1");
            field_cleaner2          = atClass.getDeclaredField("cleaner2");

            field_cleaner1          .setAccessible(true);
            field_cleaner2          .setAccessible(true);
        } catch (Exception ignored) {
        }
    }


    public ClientboundLightUpdatePacket createLightUpdatePacket(int chunkX, int chunkZ, Branch_1171_ChunkLight light, boolean trustEdges, Consumer<Integer> consumeTraffic) {
        FriendlyByteBuf serializer = new FriendlyByteBuf(Unpooled.buffer().writerIndex(0));


        List<byte[]>                dataSky         = new ArrayList<>();
        List<byte[]>                dataBlock       = new ArrayList<>();
        BitSet                      notSkyEmpty     = new BitSet();
        BitSet                      notBlockEmpty   = new BitSet();
        BitSet                      isSkyEmpty      = new BitSet();
        BitSet                      isBlockEmpty    = new BitSet();

        for(int index = 0; index < light.getArrayLength() ; ++index) {
            saveBitSet(light.getSkyLights(), index, notSkyEmpty, isSkyEmpty, dataSky);
            saveBitSet(light.getBlockLights(), index, notBlockEmpty, isBlockEmpty, dataBlock);
        }


        serializer.writeVarInt(chunkX);
        serializer.writeVarInt(chunkZ);
        serializer.writeBoolean(trustEdges);
        serializer.writeBitSet(notSkyEmpty);
        serializer.writeBitSet(notBlockEmpty);
        serializer.writeBitSet(isSkyEmpty);
        serializer.writeBitSet(isBlockEmpty);

        serializer.writeIntIdList(dataSky, new IntList());
        serializer.writeIntIdList(dataBlock, PacketDataSerializer::a);

        consumeTraffic.accept(serializer.readableBytes());

        PacketPlayOutLightUpdate    packet          = new PacketPlayOutLightUpdate(serializer);

        try {
            if (field_cleaner1 != null)
                field_cleaner1.set(packet, (Runnable) () -> { });
            if (field_cleaner2 != null)
                field_cleaner2.set(packet, (Runnable) () -> { });
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return packet;
    }


    private static void saveBitSet(byte[][] nibbleArrays, int index, BitSet notEmpty, BitSet isEmpty, List<byte[]> list) {
        byte[] nibbleArray = nibbleArrays[index];
        if (nibbleArray != Branch_1171_ChunkLight.EMPTY) {
            if (nibbleArray == null) {
                isEmpty.set(index);
            } else {
                notEmpty.set(index);
                list.add(nibbleArray);
            }
        }
    }
}
