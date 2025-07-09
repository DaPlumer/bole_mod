package net.daplumer.more_gems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.test.TestContext;

public class MoreGemsGameTest implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        SharedConstants.isDevelopment = true;
    }
}
