package com.bobmowzie.mowziesmobs.common;

import com.bobmowzie.mowziesmobs.common.entity.EntitySunstrike;
import com.bobmowzie.mowziesmobs.common.property.WroughtAxeSwingHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.world.World;

public class ServerProxy
{
    public void init()
    {
        FMLCommonHandler.instance().bus().register(new WroughtAxeSwingHandler());
    }

    public void playSunstrikeSound(EntitySunstrike strike) {
    }

    public void spawnOrbFX(World world, double x, double y, double z, double targetX, double targetZ) {
    }

    public void spawnOrbFX(World world, double x, double y, double z, double targetX, double targetY, double targetZ, double speed) {
    }

    public ModelBiped getArmorModel(int i)
    {
        return null;
    }
}
