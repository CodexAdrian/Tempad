package me.codexadrian.tempad;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import me.codexadrian.tempad.client.render.TimedoorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.PackType;

public class TempadClient implements ClientModInitializer {
    public static ShaderInstance timedoorShader;
    public static ShaderInstance timedoorWhiteShader;
    public static ShaderInstance colorWheelShader;
    public static ShaderInstance colorTriangleShader;
    public static final RenderType timedoorBlurRenderType = RenderType.create("timedoorBlur", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setCullState(new RenderStateShard.CullStateShard(false)).setShaderState(new RenderStateShard.ShaderStateShard(() -> timedoorWhiteShader)).createCompositeState(false));
    //public static final RenderType colorWheelRendertype = RenderType.create("colorwheel", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> colorWheelShader)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).createCompositeState(false));
    //public static final RenderType colorTriangleRendertype = RenderType.create("colortriangle", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.TRIANGLES, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> colorTriangleShader)).createCompositeState(false));

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Tempad.TIMEDOOR_ENTITY_ENTITY_TYPE, TimedoorRenderer::new);
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(BlurReloader.INSTANCE);
    }
}
