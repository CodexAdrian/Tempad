package me.codexadrian.tempad;

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
    public static final RenderType TIMEDOOR_LAYER = RenderType.create("timedoor", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> timedoorShader)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).createCompositeState(false));

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, TimedoorRenderer::new);
    }
}
