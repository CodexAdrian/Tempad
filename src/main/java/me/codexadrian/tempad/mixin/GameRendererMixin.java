package me.codexadrian.tempad.mixin;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import me.codexadrian.tempad.TempadClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Map<String, ShaderInstance> shaders;

    @Inject(method = "reloadShaders", at = @At("TAIL"))
    private void reloadShaders(ResourceManager resourceManager, CallbackInfo ci) {
        ShaderInstance shader;
        try {
            shader = new ShaderInstance(resourceManager, "rendertype_timedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        } catch (Exception e) {
            throw new RuntimeException("could not reload Tempad shader", e);
        }

        this.shaders.put(shader.getName(), shader);
        TempadClient.timedoorShader = shader;
    }

    @Inject(method = "resize", at = @At("TAIL"))
    public void pain(int i, int j, CallbackInfo ci) {
        TempadClient.BLUR_RENDER_TARGET = new TextureTarget(i, j,true, Minecraft.ON_OSX);
    }
}
