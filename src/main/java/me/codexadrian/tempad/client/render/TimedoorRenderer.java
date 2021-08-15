package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.Util;
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
        float width = 1.4F;
        float height = 2.3F;
        float depth = .4F;
        int animationLengthInMilli = 600;
        int stopValue = 200;
        int phaseLength = (animationLengthInMilli)/2;
        long animationTime = (Util.getMillis() - entity.birthTime) % animationLengthInMilli;
        float phase1 = 1 - 1F/phaseLength * animationTime;
        float phase2 = 1 - 1F/phaseLength * (animationTime - phaseLength);

        if(animationTime <= phaseLength) {
            height = height * phase1;
            //width = width * widthPhase1;
        }

        if (animationTime > phaseLength) {
            width = width * phase2;
            depth = depth * phase2;
            height = .16F;
        }

        poseStack.pushPose();
        poseStack.translate(0, 1.15F, 0);
        var model = poseStack.last().pose();
        makeBoxBasedOnPlayerBecauseAshSaidSo(model, multiBufferSource, width, height, depth, i);
        super.render(entity, f, g, poseStack, multiBufferSource, i);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TimedoorEntity entity) {
        return null;
    }

    public void makeBoxBasedOnPlayerBecauseAshSaidSo(Matrix4f model, MultiBufferSource multiBufferSource, float width, float height, float depth, int i) {
        float xBound = width * 0.5F;
        float yBound = height * 0.5F;
        float zBound = -(depth * 0.5F);
        var buffer = multiBufferSource.getBuffer(TempadClient.TIMEDOOR_LAYER);
        //Front
        float red = 0.0F;
        float green = 0.0F;
        float blue = 0.0F;
        float alpha = 1F;
        buffer.vertex(model, -xBound, yBound, -zBound).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, -yBound, -zBound).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, -yBound, -zBound).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, yBound, -zBound).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Back
        buffer.vertex(model, xBound, yBound, zBound).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, xBound, -yBound, zBound).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, -yBound, zBound).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, yBound, zBound).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Top
        buffer.vertex(model, -xBound, yBound, zBound).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, yBound, -zBound).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, yBound, -zBound).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, yBound, zBound).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Bottom
        buffer.vertex(model, -xBound, -yBound,-zBound).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, -yBound, zBound).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, -yBound, zBound).color(red, green, blue, alpha).uv(1, 1).uv2(i).endVertex();
        buffer.vertex(model, xBound, -yBound, -zBound).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Left
        buffer.vertex(model, -xBound, yBound, zBound).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, -xBound, -yBound, zBound).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, -yBound, -zBound).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, -xBound, yBound, -zBound).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();

        //Right
        buffer.vertex(model, xBound, yBound, -zBound).color(red, green, blue, alpha).uv(0,0).uv2(i).endVertex();
        buffer.vertex(model, xBound, -yBound, -zBound).color(red, green, blue, alpha).uv(0,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, -yBound, zBound).color(red, green, blue, alpha).uv(1,1).uv2(i).endVertex();
        buffer.vertex(model, xBound, yBound, zBound).color(red, green, blue, alpha).uv(1,0).uv2(i).endVertex();
    }
}
