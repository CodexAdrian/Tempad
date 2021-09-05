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

import static me.codexadrian.tempad.TempadClient.*;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Map<String, ShaderInstance> shaders;

    @Inject(method = "reloadShaders", at = @At("TAIL"))
    private void reloadShaders(ResourceManager resourceManager, CallbackInfo ci) {
        try {
            timedoorShader = new ShaderInstance(resourceManager, "rendertype_timedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
            colorWheelShader = new ShaderInstance(resourceManager, "rendertype_colorwheel", DefaultVertexFormat.POSITION);
            colorTriangleShader = new ShaderInstance(resourceManager, "rendertype_colortriangle", DefaultVertexFormat.POSITION);
        } catch (Exception e) {
            if (timedoorShader != null) {
                timedoorShader.close();
                timedoorShader = null;
            }

            if(colorWheelShader != null) {
                colorWheelShader.close();
                colorWheelShader = null;
            }

            throw new RuntimeException("could not reload Tempad shaders", e);
        }

        this.shaders.put(timedoorShader.getName(), timedoorShader);
        this.shaders.put(colorWheelShader.getName(), colorWheelShader);
        this.shaders.put(colorTriangleShader.getName(), colorTriangleShader);
    }

    @Inject(method = "resize", at = @At("TAIL"))
    public void pain(int i, int j, CallbackInfo ci) {
        TempadClient.BLUR_RENDER_TARGET = new TextureTarget(i, j, true, Minecraft.ON_OSX);
    }
}
