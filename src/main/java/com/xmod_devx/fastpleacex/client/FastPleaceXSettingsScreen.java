package com.xmod_devx.fastpleacex.client;

import com.xmod_devx.fastpleacex.config.FastPleaceXConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** Allgemeines Einstellungsmenü, erreichbar mit G. */
public final class FastPleaceXSettingsScreen extends Screen {
    private final Screen parent;
    private final FastPleaceXConfig config = FastPleaceXConfig.get();

    public FastPleaceXSettingsScreen(Screen parent) {
        super(Text.translatable("screen.fastpleacex.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        addDrawableChild(ButtonWidget.builder(enabledLabel(config.enabled), button -> {
                    config.enabled = !config.enabled;
                    button.setMessage(enabledLabel(config.enabled));
                }).dimensions(center - 100, this.height / 2 - 65, 200, 20).build());
        addDrawableChild(new DelaySlider(center - 100, this.height / 2 - 30, 200, 20, config.delayTicks));
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.blacklist"),
                        button -> client.setScreen(new FastPleaceXBlacklistScreen(this)))
                .dimensions(center - 100, this.height / 2 + 5, 200, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.done"), button -> close())
                .dimensions(center - 100, this.height / 2 + 40, 200, 20).build());
    }

    @Override
    public void close() {
        config.save();
        if (client != null) client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 30, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("screen.fastpleacex.notice"), width / 2, height - 35, 0xAAAAAA);
        super.render(context, mouseX, mouseY, delta);
    }

    private static Text enabledLabel(boolean value) {
        return Text.translatable("screen.fastpleacex.enabled", Text.translatable(value ? "screen.fastpleacex.on" : "screen.fastpleacex.off"));
    }

    private final class DelaySlider extends SliderWidget {
        DelaySlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Text.empty(), value / FastPleaceXConfig.MAX_DELAY_TICKS);
            updateMessage();
        }
        @Override protected void updateMessage() {
            double displayed = Math.round(value * FastPleaceXConfig.MAX_DELAY_TICKS * 10.0) / 10.0;
            setMessage(Text.translatable("screen.fastpleacex.delay", displayed));
        }
        @Override protected void applyValue() {
            config.delayTicks = FastPleaceXConfig.clamp(
                    Math.round(value * FastPleaceXConfig.MAX_DELAY_TICKS * 10.0) / 10.0);
        }
    }
}
