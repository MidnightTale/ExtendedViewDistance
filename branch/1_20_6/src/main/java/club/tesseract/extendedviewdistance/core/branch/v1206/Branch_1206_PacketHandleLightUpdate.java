package club.tesseract.extendedviewdistance.core.branch.v1206;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.VarInt;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class Branch_1206_PacketHandleLightUpdate {

  public Branch_1206_PacketHandleLightUpdate() {}

  public void write(FriendlyByteBuf serializer, Branch_1206_ChunkLight light) {
    List<byte[]> dataSky = new ArrayList<>();
    List<byte[]> dataBlock = new ArrayList<>();
    BitSet notSkyEmpty = new BitSet();
    BitSet notBlockEmpty = new BitSet();
    BitSet isSkyEmpty = new BitSet();
    BitSet isBlockEmpty = new BitSet();

    for (int index = 0; index < light.getArrayLength(); ++index) {
      saveBitSet(light.getSkyLights(), index, notSkyEmpty, isSkyEmpty, dataSky);
      saveBitSet(
        light.getBlockLights(),
        index,
        notBlockEmpty,
        isBlockEmpty,
        dataBlock
      );
    }

    serializer.writeBitSet(notSkyEmpty);
    serializer.writeBitSet(notBlockEmpty);
    serializer.writeBitSet(isSkyEmpty);
    serializer.writeBitSet(isBlockEmpty);
    serializer.writeCollection(dataBlock, (buf, byteArray) -> {
            VarInt.write(buf, byteArray.length);
            buf.writeBytes(byteArray);
        });
    serializer.writeCollection(dataBlock, (buf, byteArray) -> {
            VarInt.write(buf, byteArray.length);
            buf.writeBytes(byteArray);
        });
    // serializer.writeCollection(dataSky, (buf, byteArray) -> buf.writeByteArray(byteArray));
    // serializer.writeCollection(dataBlock, (buf, byteArray) -> buf.writeByteArray(byteArray));
  }

  private static void saveBitSet(
    byte[][] nibbleArrays,
    int index,
    BitSet notEmpty,
    BitSet isEmpty,
    List<byte[]> list
  ) {
    byte[] nibbleArray = nibbleArrays[index];
    if (nibbleArray != Branch_1206_ChunkLight.EMPTY) {
      if (nibbleArray == null) {
        isEmpty.set(index);
      } else {
        notEmpty.set(index);
        list.add(nibbleArray);
      }
    }
  }
}
