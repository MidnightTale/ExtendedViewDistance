package club.tesseract.extendedviewdistance.core.branch.v1194;

import club.tesseract.extendedviewdistance.api.branch.BranchNBT;
import net.minecraft.nbt.CompoundTag;


public final class Branch_1194_NBT implements BranchNBT {

    protected CompoundTag tag;

    public Branch_1194_NBT() {
        this.tag = new CompoundTag();
    }

    public Branch_1194_NBT(CompoundTag tag) {
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
