package com.example.pmbattleinfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = PMBattleInfoMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientInitializer {
    public static KeyBinding showDamageKeyBinding;
    private static boolean isShowHeld = false;

    public static void init() {
        showDamageKeyBinding = new KeyBinding(
                "key.pmbattleinfo.show_damage",
                KeyConflictContext.IN_GAME,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.categories.pmbattleinfo"
        );
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(showDamageKeyBinding);
        MinecraftForge.EVENT_BUS.register(new ClientInitializer());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (showDamageKeyBinding == null) return;
        isShowHeld = showDamageKeyBinding.isKeyDown();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!isShowHeld) return;
        if (Minecraft.getInstance().player == null) return;

        String[] lines = DamageHudProvider.getDamageOverlayLines();
        int x = 6;
        int y = 6;
        for (String line : lines) {
            Minecraft.getInstance().font.draw(event.getMatrixStack(), line, x, y, 0xFFFF55);
            y += 10;
        }
    }
}