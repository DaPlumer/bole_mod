package net.daplumer.data_modification_utils.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DecoratedPotPatterns.class)
public class DecoratedPotMixin {
    @ModifyReturnValue(method = "fromSherd",at = @At("RETURN"))
    @Nullable
    private static RegistryKey<DecoratedPotPattern> aVoid(@Nullable RegistryKey<DecoratedPotPattern> original, @Local(argsOnly = true) Item sherd){
        if(original == null) try{
            RegistryKey<DecoratedPotPattern> key = RegistryKey.of(RegistryKeys.DECORATED_POT_PATTERN, Registries.ITEM.getId(sherd).withPath(string -> string.substring(0,string.length()-13)));
            if(Registries.DECORATED_POT_PATTERN.contains(key)) return key;
        } catch (Exception ignored){}
        return original;
    }
}
