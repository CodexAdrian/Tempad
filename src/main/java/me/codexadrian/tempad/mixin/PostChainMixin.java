package me.codexadrian.tempad.mixin;

import me.codexadrian.tempad.PostChainAccessor;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PostChain.class)
public class PostChainMixin implements PostChainAccessor {

    @Shadow @Final private List<PostPass> passes;

    @Override
    public List<PostPass> getPasses() {
        return passes;
    }
}
