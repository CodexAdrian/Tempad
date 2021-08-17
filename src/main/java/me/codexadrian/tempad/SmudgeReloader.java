package me.codexadrian.tempad;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.IOException;

public class SmudgeReloader implements ResourceManagerReloadListener {

    public PostChain smudge;

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        var minecraft = Minecraft.getInstance();

        if (this.smudge != null) {
            this.smudge.close();
        }

        ResourceLocation resourceLocation = new ResourceLocation(Tempad.MODID, "shaders/post/smudge.json");

        try {
            this.smudge = new PostChain(minecraft.getTextureManager(), resourceManager, minecraft.getMainRenderTarget(), resourceLocation);
            this.smudge.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        } catch (IOException var3) {
            Tempad.LOGGER.warn("Failed to load shader: {}", resourceLocation, var3);
            this.smudge = null;
        } catch (JsonSyntaxException var4) {
            Tempad.LOGGER.warn("Failed to parse shader: {}", resourceLocation, var4);
            this.smudge = null;
        }
    }
}
