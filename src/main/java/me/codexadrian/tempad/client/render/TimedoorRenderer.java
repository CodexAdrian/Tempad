package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.TimedoorEntity;

public class TimedoorRenderer extends EntityRenderer<TimedoorEntity> {
    public TimedoorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TimedoorEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        var model = poseStack.last().pose();
        makeBoxBasedOnPlayerBecauseAshSaidSo(model, multiBufferSource, 1.4F, 2.3F, .4F, i);
        poseStack.popPose();
        super.render(entity, f, g, poseStack, multiBufferSource, i);

    }

    @Override
    public ResourceLocation getTextureLocation(TimedoorEntity entity) {
        return null;
    }

    public void makeBoxBasedOnPlayerBecauseAshSaidSo(Matrix4f model, MultiBufferSource multiBufferSource, float width, float height, float depth, int i) {
        float xBound = width/2f;
        depth = -depth;
        var buffer = multiBufferSource.getBuffer(TempadClient.TIMEDOOR_LAYER);
        //Front
        float red = 0.0F;
        float green = 0.0F;
        float blue = 0.0F;
        float alpha = 1F;
        buffer.vertex(model, -xBound, height, 0).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, 0, 0).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, 0, 0).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, height, 0).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Back
        buffer.vertex(model, xBound, height, depth).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, xBound, 0, depth).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, 0, depth).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, height, depth).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Top
        buffer.vertex(model, -xBound, height, depth).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, height, 0).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, height, 0).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, height, depth).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Bottom
        buffer.vertex(model, -xBound, 0,0).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, 0, depth).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, 0, depth).color(red, green, blue, alpha).uv(1, 1).uv2(i).endVertex();
        buffer.vertex(model, xBound, 0, 0).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Left
        buffer.vertex(model, -xBound, height, depth).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, 0, depth).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, 0, 0).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, height, 0).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Right
        buffer.vertex(model, xBound, height, 0).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, xBound, 0, 0).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, 0, depth).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, height, depth).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();
    }
}
