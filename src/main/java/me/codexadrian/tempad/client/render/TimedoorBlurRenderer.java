package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
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
import org.lwjgl.system.CallbackI;

public class TimedoorBlurRenderer {

    public static void renderBlur(float deltaTime, PoseStack poseStack, Camera camera) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget renderTexture = minecraft.getMainRenderTarget();
        RenderTarget blurRenderTarget = TempadClient.BLUR_RENDER_TARGET;
        blurRenderTarget.copyDepthFrom(renderTexture);

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
        inSize.set((float)renderTexture.width, (float)renderTexture.height);


        

        assert minecraft.level != null;
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
    }
}
