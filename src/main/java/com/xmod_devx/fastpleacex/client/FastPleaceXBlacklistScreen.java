package com.xmod_devx.fastpleacex.client;

import com.xmod_devx.fastpleacex.config.FastPleaceXConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.ArrayList;

/** Dunkle Blacklist-Verwaltung, erreichbar mit K. */
public final class FastPleaceXBlacklistScreen extends Screen {
    private final Screen parent;
    private final FastPleaceXConfig config = FastPleaceXConfig.get();

    public FastPleaceXBlacklistScreen(Screen parent) {
        super(Text.translatable("screen.fastpleacex.blacklist_title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        rebuildButtons();
    }

    private void rebuildButtons() {
        clearChildren();
        int left = width / 2 - 180;
        int top = height / 2 - 105;
        int y = top + 58;
        int visible = 0;
        for (String id : new ArrayList<>(config.blacklist)) {
            final String removeId = id;
            addDrawableChild(ButtonWidget.builder(Text.literal("×  " + id), button -> {
                        config.blacklist.remove(removeId);
                        config.save();
                        rebuildButtons();
                    }).dimensions(left, y, 360, 22).build());
            y += 26;
            visible++;
            if (visible >= 5) break;
        }
        if (visible == 0) {
            addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.empty"), button -> {})
                    .dimensions(left, y, 360, 22).build());
        }

        int bottom = height / 2 + 140;
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.open_picker"), button ->
                        client.setScreen(new FastPleaceXBlockPickerScreen(this)))
                .dimensions(left, bottom - 72, 175, 24).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.reset"), button -> {
                    config.resetBlacklistToDefaults();
                    config.save();
                    rebuildButtons();
                }).dimensions(left + 185, bottom - 72, 175, 24).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.back"), button -> close())
                .dimensions(left, bottom - 38, 360, 24).build());
    }

    private void addHeldBlock() {
        MinecraftClient client = this.client;
        if (client == null || client.player == null) return;
        if (client.player.getMainHandStack().getItem() instanceof BlockItem) {
            config.blacklist.add(Registries.ITEM.getId(client.player.getMainHandStack().getItem()).toString());
            config.save();
            rebuildButtons();
        }
    }

    @Override
    public void close() {
        config.save();
        if (client != null) client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int left = width / 2 - 210;
        int top = height / 2 - 145;
        int right = width / 2 + 210;
        int bottom = height / 2 + 140;
        context.fill(left + 4, top + 4, right + 4, bottom + 4, 0x70000000);
        context.fill(left, top, right, bottom, 0xE611141A);
        context.fill(left, top, right, top + 3, 0xFFFFB347);
        context.drawTextWithShadow(textRenderer, title, left + 22, top + 18, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.blacklist_hint"), left + 22, top + 32, 0xFF8E99A8);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.blacklist_count", config.blacklist.size()), left + 22, top + 42, 0xFFFFB347);
        super.render(context, mouseX, mouseY, delta);
    }
}
