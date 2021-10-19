/*
package me.codexadrian.tempad;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;


public record TempadLocation(@Nullable String name, @Nullable ResourceKey<Level> key, BlockPos position) {

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        if(name() != null) {
            tag.putString("positionName", name());
        }
        if(key() != null) {
            var dimensionTag = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, key());
            Optional<Tag> result = dimensionTag.result();
            result.ifPresent(value -> tag.put("dimension", value));
        }
        tag.put("dimPosition", NbtUtils.writeBlockPos(position()));
        tag.putString("id", this.toString());
        return tag;
    }

    public static TempadLocation fromTag(CompoundTag tag) {
        if(tag.contains("dimPosition")) {
            String name = null;
            ResourceKey<Level> key = null;
            var position = NbtUtils.readBlockPos(tag.getCompound("dimPosition"));
            if(tag.contains("dimension")) {
                var dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("dimension")).result();
                if (dimension.isPresent()) {
                    key = dimension.get();
                }
            }
            if(tag.contains("positionName")) {
                name = tag.getString("positionName");
            }
            return new TempadLocation(name, key, position);
        }
        return null;
    }
    public ServerLevel getLevel(Level level) {
        return Objects.requireNonNull(level.getServer()).getLevel(key());
    }

    public static List<TempadLocation> getLocationsFromStack(ItemStack stack) {
        var keys = stack.getTag().getAllKeys();
        return keys.stream().map(key -> {
            List<TempadLocation> returnVal = new ArrayList<>();
            var tags = stack.getTag().getList(key, 10);
            for(int i = 0; i < tags.size(); i++) {
                var prelim = fromTag(tags.getCompound(i));
                ResourceKey<Level> notNullKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(key));
                returnVal.add(new TempadLocation(prelim.name(), notNullKey, prelim.position()));
            }
            return returnVal;
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
*/
