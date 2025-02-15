package com.algorithmlx.liaveres.proxy;

import com.algorithmlx.liaveres.client.render.AmdanorMobRender;
import com.algorithmlx.liaveres.client.screen.container.YarnStationScreen;
import com.algorithmlx.liaveres.common.setup.Constants;
import com.algorithmlx.liaveres.common.setup.Registration;
import liquid.network.Direction;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.ModId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy implements Direction {
    @SubscribeEvent
    public static void doClient(final FMLClientSetupEvent event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getGameProfile().getName().equals("AlgorithmLX"))
            Minecraft.crash(new CrashReport(Component.translatable("developer.crash.desc").toString(), new Throwable()));

        MenuScreens.register(Registration.YARN_STATION_CONTAINER.get(), YarnStationScreen::new);
    }

    @SubscribeEvent
    public static void entityRender(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.AMDANOR_SKELETON.get(), AmdanorMobRender::new);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean clientSide() {
        return true;
    }

    @Override
    public Level clientLevel() {
        return Objects.requireNonNull(Minecraft.getInstance().player).level;
    }

    @Override
    public Player clientPlayer() {
        return Minecraft.getInstance().player;
    }
}
