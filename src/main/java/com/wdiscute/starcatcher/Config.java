package com.wdiscute.starcatcher;

import com.wdiscute.starcatcher.guide.FishingGuideScreen;
import com.wdiscute.starcatcher.guide.SettingsScreen;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue MINIGAME_GUI_SCALE = BUILDER
            .comment("//ALL THESE SETTINGS CAN ALSO BE ACCESSED")
            .comment("//THROUGH THE IN-GAME SETTING TAB INSIDE")
            .comment("//THE STARCATCHER'S GUIDE")
            .defineInRange("minigame_gui_scale", 3, 0, 6);

    public static final ForgeConfigSpec.DoubleValue HIT_DELAY = BUILDER
            .defineInRange("hit_delay", 0.0d, -20, 20);

    public static final ForgeConfigSpec.EnumValue<SettingsScreen.Units> UNIT = BUILDER
            .defineEnum("units", SettingsScreen.Units.METRIC);

    public static final ForgeConfigSpec.EnumValue<FishingGuideScreen.Sort> SORT = BUILDER
            .defineEnum("sort", FishingGuideScreen.Sort.ALPHABETICAL_DOWN);

    static final ForgeConfigSpec SPEC = BUILDER.build();


    private static final ForgeConfigSpec.Builder BUILDER_SERVER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue GIVE_GUIDE = BUILDER_SERVER
            .comment("Award guide when joining the world/server once per player")
            .define("give_guide", true);

    public static final ForgeConfigSpec.BooleanValue SHOW_EXCLAMATION_MARK_PARTICLE = BUILDER_SERVER
            .define("show_exclamation_mark_particle", false);

    public static final ForgeConfigSpec.BooleanValue ENABLE_BONE_MEAL_ON_FARMLAND_FOR_WORMS = BUILDER_SERVER
            .comment("enables/disables the ability to bonemeal farmland for worms.")
            .define("enable_worms", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_MINIGAME = BUILDER_SERVER
            .define("enable_minigame", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_FTB_TEAM_SHARING = BUILDER_SERVER
            .comment("enables/disables fishes caught being unlocked for all online team members.")
            .comment("Offline players won't be awarded the entry.")
            .define("enable_seasons", true);

    public static final ForgeConfigSpec.BooleanValue ENABLE_SEASONS = BUILDER_SERVER
            .comment("enables/disables fishes being restricted by seasons.")
            .comment("Useful if you want to play with a seasons mod but don't like the built-in restrictions.")
            .define("enable_seasons", true);

    static final ForgeConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


}
