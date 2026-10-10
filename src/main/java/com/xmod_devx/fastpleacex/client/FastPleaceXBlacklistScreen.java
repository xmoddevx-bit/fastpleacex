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

/** GUI zum Bearbeiten der Block-Blacklist, erreichbar mit K. */
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
        int center = width / 2;
        int y = 55;
        for (String id : new ArrayList<>(config.blacklist)) {
            final String removeId = id;
            addDrawableChild(ButtonWidget.builder(Text.literal("×  " + id), button -> {
                        config.blacklist.remove(removeId);
                        config.save();
                        rebuildButtons();
                    }).dimensions(center - 120, y, 240, 20).build());
            y += 24;
            if (y > height - 105) break;
        }

        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.add_held"), button -> addHeldBlock())
                .dimensions(center - 120, height - 78, 240, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.back"), button -> close())
                .dimensions(center - 120, height - 50, 240, 20).build());
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
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 25, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.blacklist_hint"), width / 2, 40, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }
}
