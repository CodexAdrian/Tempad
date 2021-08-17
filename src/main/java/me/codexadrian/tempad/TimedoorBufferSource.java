package me.codexadrian.tempad;
 /*
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.Optional;

public class TimedoorBufferSource implements MultiBufferSource {

    private final MultiBufferSource.BufferSource bufferSource;
    private final MultiBufferSource.BufferSource outlineBufferSource = MultiBufferSource.immediate(new BufferBuilder(256));
    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        VertexConsumer vertexConsumer2;
        if (renderType.isOutline()) {
            vertexConsumer2 = this.outlineBufferSource.getBuffer(renderType);
            return new TimedoorBufferSource.EntityOutlineGenerator(vertexConsumer2, this.teamR, this.teamG, this.teamB, this.teamA);
        } else {
            vertexConsumer2 = this.bufferSource.getBuffer(renderType);
            Optional<RenderType> optional = renderType.outline();
            if (optional.isPresent()) {
                VertexConsumer vertexConsumer3 = this.outlineBufferSource.getBuffer((RenderType)optional.get());
                OutlineBufferSource.EntityOutlineGenerator entityOutlineGenerator = new OutlineBufferSource.EntityOutlineGenerator(vertexConsumer3, this.teamR, this.teamG, this.teamB, this.teamA);
                return VertexMultiConsumer.create(entityOutlineGenerator, vertexConsumer2);
            } else {
                return vertexConsumer2;
            }
        }
    }


}
        */