package me.codexadrian.tempad;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class SmudgeReloader implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
/*        if (this.entityEffect != null) {
            this.entityEffect.close();
        }

        ResourceLocation resourceLocation = new ResourceLocation("shaders/post/entity_outline.json");

        try {
            this.entityEffect = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), resourceLocation);
            this.entityEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            this.entityTarget = this.entityEffect.getTempTarget("final");
        } catch (IOException var3) {
            LOGGER.warn((String)"Failed to load shader: {}", (Object)resourceLocation, (Object)var3);
            this.entityEffect = null;
            this.entityTarget = null;
        } catch (JsonSyntaxException var4) {
            LOGGER.warn((String)"Failed to parse shader: {}", (Object)resourceLocation, (Object)var4);
            this.entityEffect = null;
            this.entityTarget = null;
        }
    }*/
    }
}
