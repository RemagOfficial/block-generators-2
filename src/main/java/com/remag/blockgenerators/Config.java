package com.remag.blockgenerators;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = BlockGenerators.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue BLOCK_GENERATOR_GENERATE_TIME = BUILDER
            .comment("How long should the block generator take to generate a block without upgrades")
            .defineInRange("blockGeneratorGenerateTime", 160, 1, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int blockGeneratorGenerateTime;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        blockGeneratorGenerateTime = BLOCK_GENERATOR_GENERATE_TIME.get();
    }
}
