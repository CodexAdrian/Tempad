package me.codexadrian.tempad;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import me.codexadrian.tempad.client.render.TimedoorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

public class TempadClient implements ClientModInitializer {
    public static ShaderInstance timedoorShader;
    public static final RenderType TIMEDOOR_LAYER = RenderType.create("timedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> timedoorShader)).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).createCompositeState(false));
    public static RenderTarget BLUR_RENDER_TARGET;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, TimedoorRenderer::new);
    }
}
