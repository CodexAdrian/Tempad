package me.codexadrian.tempad;

import com.google.gson.JsonSyntaxException;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.IOException;
import java.lang.module.ModuleDescriptor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static me.codexadrian.tempad.Tempad.MODID;

public class BlurReloader implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
    public static PostChain timedoorBlur;
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        var minecraft = Minecraft.getInstance();

        if (timedoorBlur != null) {
            timedoorBlur.close();
        }
        ResourceLocation resourceLocation = new ResourceLocation("shaders/post/timedoorblur.json");
        try {
            timedoorBlur = new PostChain(minecraft.getTextureManager(), resourceManager, minecraft.getMainRenderTarget(), resourceLocation);
            timedoorBlur.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        } catch (JsonSyntaxException | IOException var4) {
            Tempad.LOGGER.warn("Failed to parse shader: {}", resourceLocation, var4);
            timedoorBlur = null;
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(MODID, "timedoorblur");
    }
}
