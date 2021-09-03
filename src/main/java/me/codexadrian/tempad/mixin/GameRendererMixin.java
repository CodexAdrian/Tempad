package me.codexadrian.tempad.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import me.codexadrian.tempad.TempadClient;
import me.codexadrian.tempad.client.render.TimedoorBlurRenderer;
import me.codexadrian.tempad.client.render.TimedoorRenderer;
import me.codexadrian.tempad.entity.TimedoorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
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
        List<Pair<ShaderInstance, Consumer<ShaderInstance>>> list = new ArrayList<>();
        try {
                    list.add(Pair.of(new ShaderInstance(resourceManager, "rendertype_timedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (shaderInstance) -> TempadClient.timedoorShader = shaderInstance));
                    list.add(Pair.of(new ShaderInstance(resourceManager, "rendertype_blurtimedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (shaderInstance) -> TempadClient.blurShader = shaderInstance));
        } catch (Exception e) {
            list.forEach(pair -> pair.getFirst().close());
            throw new RuntimeException("could not reload shaders", e);
        }

        list.forEach(pair -> {
            ShaderInstance shaderInstance = pair.getFirst();
            this.shaders.put(shaderInstance.getName(), shaderInstance);
            pair.getSecond().accept(shaderInstance);
        });
    }

    @Inject(method = "resize", at = @At("TAIL"))
    public void pain(int i, int j, CallbackInfo ci) {
        TempadClient.BLUR_RENDER_TARGET = new TextureTarget(i, j,true, Minecraft.ON_OSX);
    }
}
