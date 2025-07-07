package net.daplumer.more_gems.data_types;

import net.daplumer.data_modification_utils.mod_registries.SoundEventRegisterer;
import net.daplumer.more_gems.MoreGems;
import net.minecraft.sound.SoundEvent;

public class ModSounds {
    private ModSounds(){}
    public static final SoundEventRegisterer SOUNDS = MoreGems.REGISTERER.SOUNDS;
    public static final SoundEvent BOULDER_HIT = SOUNDS.register("boulder_hit");
    public static final SoundEvent BOLE_AMBIENCE = SOUNDS.register("bole_ambience");
    public static void initialize(){

    }
}
