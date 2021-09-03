package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.codexadrian.tempad.TempadClient;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;

public class TimeDoorBlurRenderer {

    public static void renderBlur(float deltaTime, PoseStack poseStack, Camera camera, Matrix4f matrix) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget renderTexture = TempadClient.BLUR_RENDER_TARGET;
        RenderTarget mainScreen = minecraft.getMainRenderTarget();
        Window window = Minecraft.getInstance().getWindow();
        mainScreen.bindRead();
        renderTexture.bindWrite(false);
        renderTexture.blitToScreen(window.getWidth(), window.getHeight());
        renderTexture.unbindWrite();
        mainScreen.unbindRead();
        mainScreen.bindWrite(false);
        RenderSystem.setProjectionMatrix(matrix);

        Vec3 position = camera.getPosition();
        double cameraX = position.x();
        double cameraY = position.y();
        double cameraZ = position.z();

        assert minecraft.level != null;
        TimedoorRenderer.whichTime = true;
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity instanceof TimedoorEntity) {
                double entityX = Mth.lerp(deltaTime, entity.xOld, entity.getX());
                double entityY = Mth.lerp(deltaTime, entity.yOld, entity.getY());
                double entityZ = Mth.lerp(deltaTime, entity.zOld, entity.getZ());
                float entityYaw = Mth.lerp(deltaTime, entity.yRotO, entity.getYRot());

                minecraft.getEntityRenderDispatcher().render(entity, entityX - cameraX, entityY - cameraY, entityZ - cameraZ, entityYaw, deltaTime, poseStack, bufferSource, minecraft.getEntityRenderDispatcher().getPackedLightCoords(entity, deltaTime));
                bufferSource.endLastBatch();
            }
        }
        TimedoorRenderer.whichTime = false;
    }
}
