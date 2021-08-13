package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.projectile.TimedoorEntity;
import net.minecraft.resources.ResourceLocation;

public class TimedoorRenderer extends EntityRenderer<TimedoorEntity> {
    public TimedoorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TimedoorEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        var model = poseStack.last().pose();
        makeBoxBasedOnPlayerBecauseAshSaidSo(model, multiBufferSource, 1.4F, 2.3F, .4F);
        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);

    }

    @Override
    public ResourceLocation getTextureLocation(TimedoorEntity entity) {
        return null;
    }

    public void makeBoxBasedOnPlayerBecauseAshSaidSo(Matrix4f model, MultiBufferSource multiBufferSource, float width, float height, float depth) {
        float xBound = width/2f;
        depth = -depth;
        var buffer = multiBufferSource.getBuffer(TempadClient.TIMEDOOR_LAYER);
        //Front
        buffer.vertex(model, -xBound, height, 0f).uv(0,0).endVertex();
        buffer.vertex(model, -xBound, 0, 0).uv(0,1).endVertex();
        buffer.vertex(model, xBound, 0, 0).uv(1,1).endVertex();
        buffer.vertex(model, xBound, height, 0).uv(1,0).endVertex();

        //Back
        buffer.vertex(model, xBound, height, depth).uv(0,0).endVertex();
        buffer.vertex(model, xBound, 0, depth).uv(0,1).endVertex();
        buffer.vertex(model, -xBound, 0, depth).uv(1,1).endVertex();
        buffer.vertex(model, -xBound, height, depth).uv(1,0).endVertex();

        //Top
        buffer.vertex(model, -xBound, height, depth).uv(0,0).endVertex();
        buffer.vertex(model, -xBound, height, 0).uv(0,1).endVertex();
        buffer.vertex(model, xBound, height, 0).uv(1,1).endVertex();
        buffer.vertex(model, xBound, height, depth).uv(1,0).endVertex();

        //Bottom
        buffer.vertex(model, -xBound, 0,0).uv(0,0).endVertex();
        buffer.vertex(model, -xBound, 0, depth).uv(0,1).endVertex();
        buffer.vertex(model, xBound, 0, depth).uv(1, 1).endVertex();
        buffer.vertex(model, xBound, 0, 0).uv(1,0).endVertex();

        //Left
        buffer.vertex(model, -xBound, height, depth).uv(0,0).endVertex();
        buffer.vertex(model, -xBound, 0, depth).uv(0,1).endVertex();
        buffer.vertex(model, -xBound, 0, 0).uv(1,1).endVertex();
        buffer.vertex(model, -xBound, height, 0).uv(1,0).endVertex();

        //Right
        buffer.vertex(model, xBound, height, 0).uv(0,0).endVertex();
        buffer.vertex(model, xBound, 0, 0).uv(0,1).endVertex();
        buffer.vertex(model, xBound, 0, depth).uv(1,1).endVertex();
        buffer.vertex(model, xBound, height, depth).uv(1,0).endVertex();
    }
}
