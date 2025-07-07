package net.daplumer.data_modification_utils.mixin;

import net.daplumer.data_modification_utils.mod_registries.ModTrimRegisterer;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorTrimPatterns.class)
public abstract class trimMixin {
    @Shadow
    public static void register(Registerable<ArmorTrimPattern> registry, RegistryKey<ArmorTrimPattern> key) {
    }

    @Inject(method = "bootstrap",at = @At("HEAD"))
    private static void aVoid(Registerable<ArmorTrimPattern> registry, CallbackInfo ci){
        ModTrimRegisterer.PATTERNS.forEach(key -> register(registry,key));
    }
}
