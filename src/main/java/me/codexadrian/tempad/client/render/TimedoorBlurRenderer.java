package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.codexadrian.tempad.BlurReloader;
import me.codexadrian.tempad.TempadClient;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.Comparator;
import java.util.stream.StreamSupport;

public class TimedoorBlurRenderer {

    public static void renderBlur(float deltaTime, PoseStack poseStack, Camera camera) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget renderTexture = minecraft.getMainRenderTarget();
        RenderTarget blurRenderTarget = BlurReloader.INSTANCE.getRenderTarget();
        if (blurRenderTarget == null) return;

        //blurRenderTarget.clear(false);
        clear(blurRenderTarget);
        blurRenderTarget.copyDepthFrom(renderTexture);
        renderTexture.bindWrite(false);

        Vec3 position = camera.getPosition();
        double cameraX = position.x();
        double cameraY = position.y();
        double cameraZ = position.z();

        AbstractUniform inSize = TempadClient.timedoorShader.safeGetUniform("InSize");
        AbstractUniform viewMatUniform = TempadClient.timedoorShader.safeGetUniform("ViewMat");
        PoseStack viewMat = new PoseStack();
        viewMat.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        viewMat.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot() + 180.0F));

        viewMatUniform.set(viewMat.last().pose());
        inSize.set((float) renderTexture.width, (float) renderTexture.height);

        //renderTexture.unbindRead();
        assert minecraft.level != null;
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
        StreamSupport.stream(minecraft.level.entitiesForRendering().spliterator(), false).filter(TimedoorEntity.class::isInstance).sorted(Comparator.comparingDouble(value -> -value.distanceToSqr(minecraft.cameraEntity))).forEach(entity -> {
            double entityX = Mth.lerp(deltaTime, entity.xOld, entity.getX());
            double entityY = Mth.lerp(deltaTime, entity.yOld, entity.getY());
            double entityZ = Mth.lerp(deltaTime, entity.zOld, entity.getZ());
            float entityYaw = Mth.lerp(deltaTime, entity.yRotO, entity.getYRot());

            blurRenderTarget.bindWrite(false);
            minecraft.getEntityRenderDispatcher().render(entity, entityX - cameraX, entityY - cameraY, entityZ - cameraZ, entityYaw, deltaTime, poseStack, bufferSource, minecraft.getEntityRenderDispatcher().getPackedLightCoords(entity, deltaTime));
            bufferSource.endLastBatch();
            renderTexture.bindWrite(false);
        });
/*
        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, blurRenderTarget.frameBufferId);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, renderTexture.frameBufferId);
        GlStateManager._glBlitFrameBuffer(0, 0, renderTexture.width, renderTexture.height, 0, 0, renderTexture.width/4, renderTexture.height/4, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
*/

        BlurReloader.INSTANCE.getTimedoorBlur().process(deltaTime);
        renderTexture.bindWrite(false);
    }

    public static void bindAll(RenderTarget renderTarget) {
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, renderTarget.frameBufferId);
    }

    public static void clear(RenderTarget renderTarget) {
        bindAll(renderTarget);
        GlStateManager._clear(GL11.GL_COLOR_BUFFER_BIT, false);
        GlStateManager._clearColor(0, 0, 0, 0);
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }
}
