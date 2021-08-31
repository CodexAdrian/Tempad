package me.codexadrian.tempad.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import me.codexadrian.tempad.TempadClient;
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
        List<Pair<ShaderInstance, Consumer<ShaderInstance>>> list = new ArrayList<>();
        try {
                    list.add(Pair.of(new ShaderInstance(resourceManager, "rendertype_timedoor", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (shaderInstance) -> TempadClient.timedoorShader = shaderInstance));
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

    //@Inject(method = "resize", at = @At("TAIL"))
    //public void pain(int i, int j, CallbackInfo ci) {
        //TempadClient.BLUR_RENDER_TARGET = new TextureTarget(i, j,true, Minecraft.ON_OSX);
    //}

    //@Inject(method = "renderLevel", at = @At("TAIL"))
    //public void renderBlur(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        //RenderTarget frameBuffer = TempadClient.BLUR_RENDER_TARGET;
        //RenderTarget renderTarget = Minecraft.getInstance().gameRenderer.getMinecraft().gameRenderer.getMinecraft().getMainRenderTarget();
        //Window window = Minecraft.getInstance().getWindow();
        /*
        frameBuffer.bindWrite(false);
        //ash said no
        renderTarget.bindRead();
        frameBuffer.blitToScreen(window.getWidth(), window.getHeight());
        renderTarget.unbindRead();
        frameBuffer.unbindWrite();
        */
        //frameBuffer.bindRead();
        //renderTarget.bindWrite(false);
        //renderTarget.blitToScreen(window.getWidth() / 2, window.getHeight() / 2);
        //frameBuffer.unbindRead();
        //renderTarget.unbindWrite();
    //}
}
