package com.xmod_devx.fastpleacex.client;

import com.xmod_devx.fastpleacex.config.FastPleaceXConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** Kompaktes, dunkles FastPleaceX-Menü, erreichbar mit G. */
public final class FastPleaceXSettingsScreen extends Screen {
    private final Screen parent;
    private final FastPleaceXConfig config = FastPleaceXConfig.get();

    public FastPleaceXSettingsScreen(Screen parent) {
        super(Text.translatable("screen.fastpleacex.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = width / 2 - 180;
        int top = height / 2 - 105;
        addDrawableChild(ButtonWidget.builder(enabledLabel(config.enabled), button -> {
                    config.enabled = !config.enabled;
                    config.save();
                    button.setMessage(enabledLabel(config.enabled));
                }).dimensions(left, top + 38, 360, 24).build());
        addDrawableChild(new DelaySlider(left, top + 82, 360, 24, config.delayMs));
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.blacklist"),
                        button -> client.setScreen(new FastPleaceXBlacklistScreen(this)))
                .dimensions(left, top + 126, 360, 24).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.done"), button -> close())
                .dimensions(left, top + 170, 360, 24).build());
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
        int bottom = height / 2 + 125;
        context.fill(left + 4, top + 4, right + 4, bottom + 4, 0x70000000);
        context.fill(left, top, right, bottom, 0xE611141A);
        context.fill(left, top, right, top + 3, 0xFF21D4D8);
        context.drawTextWithShadow(textRenderer, title, left + 22, top + 18, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.subtitle"), left + 22, top + 30, 0xFF8E99A8);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.section.general"), left + 25, top + 51, 0xFF21D4D8);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.section.timing"), left + 25, top + 95, 0xFFFFB347);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.section.protection"), left + 25, top + 139, 0xFF21D4D8);
        context.drawTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.notice"), left + 22, bottom - 18, 0xFF8E99A8);
        super.render(context, mouseX, mouseY, delta);
    }

    private static Text enabledLabel(boolean value) {
        return Text.translatable("screen.fastpleacex.enabled", Text.translatable(value ? "screen.fastpleacex.on" : "screen.fastpleacex.off"));
    }

    private final class DelaySlider extends SliderWidget {
        DelaySlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Text.empty(), value / FastPleaceXConfig.MAX_DELAY_MS);
            updateMessage();
        }
        @Override protected void updateMessage() {
            double displayed = Math.round(value * FastPleaceXConfig.MAX_DELAY_MS);
            setMessage(Text.translatable("screen.fastpleacex.delay", displayed));
        }
        @Override protected void applyValue() {
            config.delayMs = FastPleaceXConfig.clamp(
                    Math.round(value * FastPleaceXConfig.MAX_DELAY_MS));
            config.save();
        }
    }
}
