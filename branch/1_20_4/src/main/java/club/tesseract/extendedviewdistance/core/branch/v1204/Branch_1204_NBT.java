package club.tesseract.extendedviewdistance.core.branch.v1204;

import net.minecraft.nbt.CompoundTag;
import club.tesseract.extendedviewdistance.api.branch.BranchNBT;


public final class Branch_1204_NBT implements BranchNBT {

  protected CompoundTag tag;

  public Branch_1204_NBT() {
    this.tag = new CompoundTag();
  }

  public Branch_1204_NBT(CompoundTag tag) {
    this.tag = tag;
  }

  public CompoundTag getNMSTag() {
    return tag;
  }

  @Override
  public String toString() {
    return tag.toString();
  }
}
