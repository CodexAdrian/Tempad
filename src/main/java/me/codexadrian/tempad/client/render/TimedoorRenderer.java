package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import me.codexadrian.tempad.entity.TimedoorEntity;

public class TimedoorRenderer extends EntityRenderer<TimedoorEntity> {
    public TimedoorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    public float width = 1.4F;
    public float height = 2.3F;
    public float depth = .4F;
    public int animationLengthInMilli = 400;

    @Override
    public void render(TimedoorEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        animateOpening(entity.birthTime);
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(entity.getYRot()));
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

    public void animateClosing(long time) {
        int phaseLength = (animationLengthInMilli)/2;
        long animationTime = (Util.getMillis() - time);
        float phase1 = 1 - 1F/phaseLength * animationTime;
        float phase2 = 1 - 1F/phaseLength * (animationTime - phaseLength);

        if(animationTime <= phaseLength) {
            this.height = height * phase1;
            //width = width * widthPhase1;
        }

        if (animationTime > phaseLength && animationTime < animationLengthInMilli) {
            this.width = width * phase2;
            //depth = depth * phase2;
            this.height = .16F;
        }
    }


    public void animateOpening(long time) {
        int phaseLength = (animationLengthInMilli)/2;
        long animationTime = (Util.getMillis() - time);
        float phase1 = 1F/phaseLength * animationTime;
        float phase2 = 1F/phaseLength * (animationTime - phaseLength*1.2F);

        if (animationTime <= phaseLength) {
            this.width = width * phase1;
            //depth = depth * phase2;
            this.height = .2F;
        }

        if(animationTime > phaseLength && animationTime < animationLengthInMilli) {
            this.height = .2F + height * phase2;
            //width = width * widthPhase1;
        }

        if(animationTime > animationLengthInMilli) {
            this.width = 1.4F;
            this.height = 2.3F;
            this.depth = .4F;
        }

    }
}
