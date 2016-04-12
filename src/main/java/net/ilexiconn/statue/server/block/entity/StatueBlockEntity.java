package net.ilexiconn.statue.server.block.entity;

import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.nbt.NBTTagCompound;

public class StatueBlockEntity extends BlockEntity {
    @NBTProperty
    public String currentModel;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.saveNBTData(this, compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.loadNBTData(this, compound);
    }
}
