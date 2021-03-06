package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.property.MowzieLivingProperties;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.event.PlayerModelEvent;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoMask;
import com.bobmowzie.mowziesmobs.server.item.ItemBarakoaMask;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtAxe;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.bobmowzie.mowziesmobs.server.property.MowziePlayerProperties;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public enum ClientEventHandler {
    INSTANCE;

    private static final ResourceLocation MARIO = new ResourceLocation(MowziesMobs.MODID, "textures/gui/mario.png");
    private static final ResourceLocation FROZEN_BLUR = new ResourceLocation(MowziesMobs.MODID, "textures/gui/frozenblur.png");

    long startWroughtnautHitTime;

    long lastWroughtnautHitTime;

    @SubscribeEvent
    public void onFrameRender(RenderItemInFrameEvent event) {
        if (event.getItem().getItem() instanceof ItemWroughtAxe) {
            GlStateManager.translate(0.325f, 0.4f, -0.05f);
            GlStateManager.scale(-0.65f, -0.65f, 0.65f);
            GlStateManager.rotate(45f, 0f, -1f, 0f);
            GlStateManager.rotate(45f, -1f, 0f, -1f);
        } else if (event.getItem().getItem() instanceof ItemWroughtHelm) {
            GlStateManager.translate(0.19f, -0.37f, -0.25f);
        } else if (event.getItem().getItem() instanceof ItemBarakoaMask) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.translate(0f, -0.2f, 0.1f);
        } else if (event.getItem().getItem() instanceof ItemBarakoMask) {
            GlStateManager.scale(0.85f, 0.85f, 0.85f);
            GlStateManager.translate(0.32f, -0.4f, -0.25f);
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPre(PlayerModelEvent.SetRotationAngles event) {
        if (event.getEntityPlayer() == null) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        player.getHeldItem(EnumHand.MAIN_HAND);
        MowziePlayerProperties propertyPlayer = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
        float delta = LLibrary.PROXY.getPartialTicks();
        if (propertyPlayer != null && propertyPlayer.geomancy.tunneling) {
            event.getModel().isSneak = false;
            Vec3d moveVec = new Vec3d(player.motionX, player.motionY, player.motionZ);
            moveVec = moveVec.normalize();
            GlStateManager.rotate(45 - 45 * (float)moveVec.y, 1.0F, 0.0F, 0.0F);

            /*toDefaultBiped(event.getModel());

            float spin = 1f * (player.ticksExisted + delta);
            event.getModel().bipedHead.rotateAngleX = 1.57f * Math.min(0f, (float)moveVec.y);
            event.getModel().bipedHead.rotateAngleY = spin;
            event.getModel().bipedHead.rotateAngleZ = 0;

            event.getModel().bipedHeadwear.rotateAngleX = 1.57f * Math.min(0f, (float)moveVec.y);
            event.getModel().bipedHeadwear.rotateAngleY = spin;
            event.getModel().bipedHeadwear.rotateAngleZ = 0;

            event.getModel().bipedBody.rotateAngleX = 0;
            event.getModel().bipedBody.rotateAngleY = spin;
            event.getModel().bipedBody.rotateAngleZ = 0;

            event.getModel().bipedLeftArm.rotateAngleX = -3.14f;
            event.getModel().bipedLeftArm.rotateAngleY = spin;
            event.getModel().bipedLeftArm.rotateAngleZ = 0;
            event.getModel().bipedLeftArm.setRotationPoint(5 * (float)Math.sin(spin + Math.PI/2), 2, 5 * (float)Math.cos(spin + Math.PI/2));

            event.getModel().bipedRightArm.rotateAngleX = -3.14f;
            event.getModel().bipedRightArm.rotateAngleY = spin;
            event.getModel().bipedRightArm.rotateAngleZ = 0;
            event.getModel().bipedRightArm.setRotationPoint(-5 * (float)Math.sin(spin + Math.PI/2), 2, -5 * (float)Math.cos(spin + Math.PI/2));

            event.getModel().bipedLeftLeg.rotateAngleX = 0;
            event.getModel().bipedLeftLeg.rotateAngleY = spin;
            event.getModel().bipedLeftLeg.rotateAngleZ = 0;
            event.getModel().bipedLeftLeg.setRotationPoint(1.9F * (float)Math.sin(spin + Math.PI/2), 12.0F, 1.9F * (float)Math.cos(spin + Math.PI/2));

            event.getModel().bipedRightLeg.rotateAngleX = 0;
            event.getModel().bipedRightLeg.rotateAngleY = spin;
            event.getModel().bipedRightLeg.rotateAngleZ = 0;
            event.getModel().bipedRightLeg.setRotationPoint(-1.9F * (float)Math.sin(spin + Math.PI/2), 12.0F, -1.9f * (float)Math.cos(spin + Math.PI/2));*/
        }
    }

    private void toDefaultBiped(ModelBiped model) {
        model.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        model.bipedHead.rotateAngleX = 0;
        model.bipedHead.rotateAngleY = 0;
        model.bipedHead.rotateAngleZ = 0;
        model.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        model.bipedHeadwear.rotateAngleX = 0;
        model.bipedHeadwear.rotateAngleY = 0;
        model.bipedHeadwear.rotateAngleZ = 0;
        model.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        model.bipedBody.rotateAngleX = 0;
        model.bipedBody.rotateAngleY = 0;
        model.bipedBody.rotateAngleZ = 0;
        model.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        model.bipedRightArm.rotateAngleX = 0;
        model.bipedRightArm.rotateAngleY = 0;
        model.bipedRightArm.rotateAngleZ = 0;
        model.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        model.bipedLeftArm.rotateAngleX = 0;
        model.bipedLeftArm.rotateAngleY = 0;
        model.bipedLeftArm.rotateAngleZ = 0;
        model.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        model.bipedRightLeg.rotateAngleX = 0;
        model.bipedRightLeg.rotateAngleY = 0;
        model.bipedRightLeg.rotateAngleZ = 0;
        model.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        model.bipedLeftLeg.rotateAngleX = 0;
        model.bipedLeftLeg.rotateAngleY = 0;
        model.bipedLeftLeg.rotateAngleZ = 0;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            MowziePlayerProperties propertyPlayer = EntityPropertiesHandler.INSTANCE.getProperties(player, MowziePlayerProperties.class);
            if (propertyPlayer != null && propertyPlayer.geomancy.canUse(player) && propertyPlayer.geomancy.isSpawningBoulder() && propertyPlayer.geomancy.getSpawnBoulderCharge() > 2) {
                Vec3d lookPos = propertyPlayer.geomancy.getLookPos();
                Vec3d playerEyes = player.getPositionEyes(LLibrary.PROXY.getPartialTicks());
                Vec3d vec = playerEyes.subtract(lookPos).normalize();
                float yaw = (float) Math.atan2(vec.z, vec.x);
                float pitch = (float) Math.asin(vec.y);
                float dYaw = ((float) (yaw * 180/Math.PI + 90) - player.rotationYaw)/2f;
                float dPitch = ((float)(pitch * 180/Math.PI) - player.rotationPitch)/2f;
                player.rotationYaw += dYaw;
                player.rotationPitch += dPitch;
                stopMouseMove();
            }
            MowzieLivingProperties propertyLiving = EntityPropertiesHandler.INSTANCE.getProperties(player, MowzieLivingProperties.class);
            if (player.isPotionActive(PotionHandler.FROZEN)) {
                player.rotationYaw = propertyLiving.frozenYaw;
                player.rotationPitch = propertyLiving.frozenPitch;
                stopMouseMove();
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre event) {
        EntityLivingBase entity = event.getEntity();
        MowzieLivingProperties property = EntityPropertiesHandler.INSTANCE.getProperties(entity, MowzieLivingProperties.class);
        if (entity.isPotionActive(PotionHandler.FROZEN)) {
//            entity.motionX = 0;
//            entity.motionZ = 0;
//            entity.posX = entity.prevPosX;
//            entity.posZ = entity.prevPosZ;
            entity.rotationYaw = property.frozenYaw;
            entity.rotationPitch = property.frozenPitch;
            entity.rotationYawHead = property.frozenYawHead;
            entity.renderYawOffset = property.frozenRenderYawOffset;
            entity.swingProgress = property.frozenSwingProgress;
            entity.limbSwingAmount = property.frozenLimbSwingAmount;
            entity.setSneaking(false);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post e) {
        final int startTime = 210;
        final int pointStart = 1200;
        final int timePerMillis = 22;
        if (e.getType() == ElementType.POTION_ICONS) {
            long now = System.currentTimeMillis();
            if (now - lastWroughtnautHitTime < 500) {
                int t = (int) (now - startWroughtnautHitTime);
                int progress = t / timePerMillis;
                int time = startTime - progress;
                if (time < 0) {
                    startWroughtnautHitTime = now;
                    progress = 0;
                    time = startTime;
                }
                int points = pointStart + progress * 50;
                Minecraft.getMinecraft().getTextureManager().bindTexture(MARIO);
                ScaledResolution res = e.getResolution();
                int offsetY = 16;
                int col = res.getScaledWidth() / 4;
                // MARIO
                int marioOffsetX = col / 2 - 18;
                Gui.drawModalRectWithCustomSizedTexture(marioOffsetX, offsetY, 0, 16, 39, 7, 64, 64);
                // points
                drawMarioNumber(marioOffsetX, offsetY + 8, points, 6);
                // Coin
                int coinOffsetX = col + col / 2 - 15;
                int coinU = 40 + ((int) (Math.max(0, MathHelper.sin(t * 0.005F)) * 2 + 0.5F)) * 6;
                Gui.drawModalRectWithCustomSizedTexture(coinOffsetX, offsetY + 8, coinU, 8, 5, 8, 64, 64);
                // x02
                Gui.drawModalRectWithCustomSizedTexture(coinOffsetX + 9, offsetY + 8, 16, 8, 23, 7, 64, 64);
                // WORLD 1-1
                Gui.drawModalRectWithCustomSizedTexture(col * 2 + col / 2 - 19, offsetY, 0, 24, 39, 15, 64, 64);
                // TIME
                int timeOffsetX = col * 3 + col / 2 - 15;
                Gui.drawModalRectWithCustomSizedTexture(timeOffsetX, offsetY, 0, 40, 30, 7, 64, 64);
                // Time
                drawMarioNumber(timeOffsetX + 8, offsetY + 8, time, 3);
            }
            if (Minecraft.getMinecraft().player.isPotionActive(PotionHandler.FROZEN) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(FROZEN_BLUR);
                ScaledResolution res = e.getResolution();
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, res.getScaledWidth(), res.getScaledHeight(), res.getScaledWidth(), res.getScaledHeight());
            }
        }
    }

    // Remove frozen overlay
    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent.Pre event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null && player.isRiding()) {
            if (player.getRidingEntity() instanceof EntityFrozenController) {
                if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTHMOUNT)) {
                    event.setCanceled(true);
                }
                if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
                    Minecraft.getMinecraft().ingameGUI.setOverlayMessage("", false);
                }
            }
        }
    }

    private static void drawMarioNumber(int x, int y, int value, int length) {
        for (int n = 0; n < length; n++, value /= 10) {
            int digit = value % 10;
            Gui.drawModalRectWithCustomSizedTexture(x + 8 * (length - n - 1), y, digit * 8 % 64, digit / 8 * 8, 8, 7, 64, 64);
        }
    }

    public static void stopMouseMove() {
        Mouse.getDX();
        Mouse.getDY();
        Minecraft.getMinecraft().mouseHelper.deltaX = Minecraft.getMinecraft().mouseHelper.deltaY = 0;
    }
}
