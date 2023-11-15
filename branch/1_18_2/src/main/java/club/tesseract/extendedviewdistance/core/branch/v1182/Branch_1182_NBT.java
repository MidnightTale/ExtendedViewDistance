package club.tesseract.extendedviewdistance.core.branch.v1182;

import club.tesseract.extendedviewdistance.api.branch.BranchNBT;
import net.minecraft.nbt.CompoundTag;


public final class Branch_1182_NBT implements BranchNBT {

    protected CompoundTag tag;

    public Branch_1182_NBT() {
        this.tag = new CompoundTag();
    }

    public Branch_1182_NBT(CompoundTag tag) {
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
