package me.codexadrian.tempad.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.codexadrian.tempad.Tempad;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class BetterTextureStateShard extends RenderStateShard.EmptyTextureStateShard {

    private static Int2ObjectMap<RenderType> s = new Int2ObjectOpenHashMap<>();

    public BetterTextureStateShard(int resourceInteger) {
        super(() -> {
            RenderSystem.enableTexture();
            RenderSystem.setShaderTexture(0, resourceInteger);
        }, () -> {
        });
    }

    public static RenderType somethingToGetTheRenderTypePerInteger(int i) {
        RenderType returnS;
        do {
            returnS = s.get(i);
            break;
        } while (true);
        //TODO make not bad
        if(returnS == null) {
            returnS = RenderType.create("timedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> TempadClient.timedoorShader)).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setTextureState(new BetterTextureStateShard(i)).createCompositeState(false));
            s.put(i, returnS);
        }
        return returnS;
    }

    @Override
    protected Optional<ResourceLocation> cutoutTexture() {
        return Optional.empty();
    }
}