package org.vined.ikea.modules.utility;

import java.util.List;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.Item;
import net.minecraft.screen.GenericContainerScreenHandler;
import org.vined.ikea.IKEA;

public class AutoItemMove extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final Setting<List<Item>> items = this.sgGeneral
            .add(new ItemListSetting.Builder()
                    .name("items")
                    .description("Which items to put in the container.")
                    .build());

    public AutoItemMove() {
        super(IKEA.UTILITY, "auto-item-move", "Automatically puts items in a container.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert this.mc.player != null;
        ScreenHandler handler = this.mc.player.currentScreenHandler;
        if (handler instanceof GenericContainerScreenHandler || handler instanceof ShulkerBoxScreenHandler) {
            moveItems(handler);
        }
    }

    private int getRows(ScreenHandler handler) {
        return (handler instanceof GenericContainerScreenHandler) ? ((GenericContainerScreenHandler) handler).getRows()
                : 3;
    }

    public void moveItems(ScreenHandler handler) {
        int playerInvOffset = getRows(handler) * 9;
        MeteorExecutor.execute(() -> moveSlots(handler, playerInvOffset, playerInvOffset + 36));
    }

    private void moveSlots(ScreenHandler handler, int start, int end) {
        for (int i = start; i < end; i++) {
            Slot slot = handler.getSlot(i);
            if (slot.hasStack() && this.items.get().contains(slot.getStack().getItem())) {
                if (this.mc.currentScreen == null)
                    break;
                InvUtils.quickMove().slotId(i);
            }
        }
    }
}