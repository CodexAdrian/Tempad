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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class TimedoorBlurRenderer {

    public static void renderBlur(float deltaTime, PoseStack poseStack, Camera camera, GameRenderer gameRenderer) {
        PoseStack poseStack2 = new PoseStack();
        poseStack2.mulPoseMatrix(gameRenderer.getProjectionMatrix(gameRenderer.getFov(camera, deltaTime, true)));
        var matrix = poseStack2.last().pose();
        RenderTarget frameBuffer = TempadClient.BLUR_RENDER_TARGET;
        Minecraft minecraft = Minecraft.getInstance().gameRenderer.getMinecraft().gameRenderer.getMinecraft();
        RenderTarget renderTarget = minecraft.getMainRenderTarget();
        Window window = Minecraft.getInstance().getWindow();
          /*
        frameBuffer.bindWrite(false);
        //ash said no
        renderTarget.bindRead();
        frameBuffer.blitToScreen(window.getWidth(), window.getHeight());
        renderTarget.unbindRead();
        frameBuffer.unbindWrite();
           */
        //gameRenderer.resetProjectionMatrix(matrix);
        //renderTarget.bindWrite(false);
        assert minecraft.level != null;
        TimedoorRenderer.whichTime = true;
        //poseStack.pushPose();
        for (Entity entity : minecraft.level.entitiesForRendering()) {
            if (entity instanceof TimedoorEntity) {
                Vec3 position = camera.getPosition();
                double cameraEntityX = position.x();
                double cameraEntityY = position.y();
                double cameraEntityZ = position.z();

                double h = Mth.lerp(deltaTime, entity.xOld, entity.getX());
                double i = Mth.lerp(deltaTime, entity.yOld, entity.getY());
                double j = Mth.lerp(deltaTime, entity.zOld, entity.getZ());
                float k = Mth.lerp(deltaTime, entity.yRotO, entity.getYRot());

                minecraft.getEntityRenderDispatcher().render(entity, h - cameraEntityX, i - cameraEntityY, j - cameraEntityZ, k, deltaTime, poseStack, minecraft.renderBuffers().bufferSource(), minecraft.getEntityRenderDispatcher().getPackedLightCoords(entity, deltaTime));
            }
        }
        //poseStack.popPose();
        TimedoorRenderer.whichTime = false;
    }
}
