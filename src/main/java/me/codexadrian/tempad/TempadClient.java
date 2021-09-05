package me.codexadrian.tempad;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import me.codexadrian.tempad.client.render.TimedoorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

public class TempadClient implements ClientModInitializer {
    public static RenderTarget BLUR_RENDER_TARGET;
    public static ShaderInstance timedoorShader;
    public static ShaderInstance colorWheelShader;
    public static ShaderInstance colorTriangleShader;
    public static final RenderType colorWheelRendertype = RenderType.create("colorwheel", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> colorWheelShader)).createCompositeState(false));
    public static final RenderType colorTriangleRendertype = RenderType.create("colortriangle", DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLES, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> colorTriangleShader)).createCompositeState(false));

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, TimedoorRenderer::new);

    }
}
