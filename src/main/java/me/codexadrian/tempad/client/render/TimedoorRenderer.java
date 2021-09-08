package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.codexadrian.tempad.TempadClient;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.awt.*;


public class TimedoorRenderer extends EntityRenderer<TimedoorEntity> {

    public TimedoorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TimedoorEntity entity, float yaw, float deltaTime, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        float width = 1.4F;
        float height = 2.3F;
        float depth = .4F;
        int closingTime = entity.getClosingTime();
        int tickLength = TimedoorEntity.ANIMATION_LENGTH;
        int phaseLength = (tickLength)/2;
        int ticks = entity.tickCount;
        float animation = (ticks + deltaTime) / tickLength;

        if (ticks < phaseLength) {
            width = Mth.lerp(animation * 2, 0, width);
            height = .2F;
        }

        if(ticks >= phaseLength && ticks < tickLength) {
            height = Mth.lerp((animation - 0.5F) * 2, .2F, height);
        }

        if(closingTime != -1) {
            if (ticks > closingTime && ticks < closingTime + phaseLength) {
                height = Mth.lerp(1 - (animation - (float) closingTime / tickLength) * 2, .2F, height);
            }

            if (ticks >= closingTime + phaseLength) {
                width = Mth.lerp(1 - (animation - (float) closingTime / tickLength - 0.5F) * 2, 0, width);
                height = .2F;
            }
        } else if (ticks > tickLength){
            width = 1.4F;
            height = 2.3F;
            depth = .4F;
        }

        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(entity.getYRot()));
        poseStack.translate(0, 1.15F, 0);
        var model = poseStack.last().pose();
        makeBoxBasedOnPlayerBecauseAshSaidSo(model, multiBufferSource, width, height, depth, light, Color.getColor("timedoorColor", entity.getColor()));
        super.render(entity, yaw, deltaTime, poseStack, multiBufferSource, light);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TimedoorEntity entity) {
        return null;
    }

    public void makeBoxBasedOnPlayerBecauseAshSaidSo(Matrix4f model, MultiBufferSource multiBufferSource, float width, float height, float depth, int i, Color color) {
        float xBound = width * 0.5F;
        float yBound = height * 0.5F - .01F;
        float zBound = -(depth * 0.5F);
        var buffer = multiBufferSource.getBuffer(TempadClient.timedoorBlurRenderType);
        //Front
        float red = color.getRed()/255F;
        float green = color.getGreen()/255F;
        float blue = color.getBlue()/255F;
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

    @Override
    public boolean shouldRender(TimedoorEntity entity, Frustum frustum, double d, double e, double f) {
        return false;
    }
}
