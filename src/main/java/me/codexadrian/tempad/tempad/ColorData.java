package me.codexadrian.tempad.tempad;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ColorData extends SavedData {
    public static final String COLOR_DATA_ID = "tempad_color_data";
    public Map<UUID, Integer> colorMap = new HashMap<>();

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag tag = new ListTag();
        for (Map.Entry<UUID, Integer> entry : colorMap.entrySet()) {
            CompoundTag playerColorData = new CompoundTag();
            playerColorData.putUUID("UUID", entry.getKey());
            playerColorData.putInt("color", entry.getValue());
            tag.add(playerColorData);
        }
        compoundTag.put("player_colors", tag);
        return compoundTag;
    }

    public static ColorData load(CompoundTag compoundTag) {
        ColorData colorData = new ColorData();
        ListTag player_colors = compoundTag.getList("player_colors", NbtType.COMPOUND);
        for (Iterator<Tag> it = player_colors.iterator(); it.hasNext(); ) {
            CompoundTag tag = (CompoundTag) it.next();
            colorData.colorMap.put(tag.getUUID("UUID"), tag.getInt("color"));
        }
        return colorData;
    }
}
