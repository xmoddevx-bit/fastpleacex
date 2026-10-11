package com.xmod_devx.fastpleacex.client;

import com.xmod_devx.fastpleacex.config.FastPleaceXConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Auswahl aller registrierten BlockItems für die Blacklist. */
public final class FastPleaceXBlockPickerScreen extends Screen {
    private static final int PAGE_SIZE = 9;
    private final Screen parent;
    private final FastPleaceXConfig config = FastPleaceXConfig.get();
    private final List<String> allBlocks = new ArrayList<>();
    private String query = "";
    private int page;
    private boolean rebuilding;

    public FastPleaceXBlockPickerScreen(Screen parent) {
        super(Text.translatable("screen.fastpleacex.block_picker_title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        allBlocks.clear();
        Registries.ITEM.stream()
                .filter(item -> item instanceof BlockItem)
                .map(item -> Registries.ITEM.getId(item).toString())
                .sorted(Comparator.naturalOrder())
                .forEach(allBlocks::add);
        rebuildWidgets();
    }

    private List<String> filteredBlocks() {
        String needle = query.trim().toLowerCase(Locale.ROOT);
        return allBlocks.stream().filter(id -> needle.isEmpty() || id.contains(needle)).toList();
    }

    private void rebuildWidgets() {
        if (rebuilding) return;
        rebuilding = true;
        clearChildren();
        int left = width / 2 - 180;
        int top = height / 2 - 145;

        TextFieldWidget search = new TextFieldWidget(textRenderer, left, top + 42, 360, 22,
                Text.translatable("screen.fastpleacex.search"));
        search.setText(query);
        search.setChangedListener(value -> {
            query = value;
            page = 0;
            rebuildWidgets();
        });
        addDrawableChild(search);

        List<String> filtered = filteredBlocks();
        int pageCount = Math.max(1, (filtered.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        page = Math.max(0, Math.min(page, pageCount - 1));
        int from = page * PAGE_SIZE;
        int to = Math.min(filtered.size(), from + PAGE_SIZE);
        int y = top + 72;
        for (int i = from; i < to; i++) {
            String id = filtered.get(i);
            boolean selected = config.blacklist.contains(id);
            addDrawableChild(ButtonWidget.builder(Text.literal((selected ? "✓  " : "+  ") + id), button -> {
                        if (config.blacklist.contains(id)) config.blacklist.remove(id);
                        else config.blacklist.add(id);
                        config.save();
                        rebuildWidgets();
                    }).dimensions(left, y, 360, 22).build());
            y += 24;
        }
        if (from == to) {
            addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.no_results"), button -> {})
                    .dimensions(left, y, 360, 22).build());
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> {
                    page = Math.max(0, page - 1);
                    rebuildWidgets();
                }).dimensions(left, top + 292, 42, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.page", page + 1, pageCount), button -> {})
                .dimensions(left + 48, top + 292, 264, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> {
                    page = Math.min(pageCount - 1, page + 1);
                    rebuildWidgets();
                }).dimensions(left + 318, top + 292, 42, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("screen.fastpleacex.back"), button -> close())
                .dimensions(left, top + 322, 360, 24).build());
        rebuilding = false;
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
        int top = height / 2 - 165;
        int right = width / 2 + 210;
        int bottom = height / 2 + 185;
        context.fill(left + 4, top + 4, right + 4, bottom + 4, 0x70000000);
        context.fill(left, top, right, bottom, 0xE611141A);
        context.fill(left, top, right, top + 3, 0xFF55D6FF);
        context.drawTextWithShadow(textRenderer, title, left + 22, top + 18, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer,
                Text.translatable("screen.fastpleacex.block_picker_hint", allBlocks.size()), left + 22, top + 30, 0xFF8E99A8);
        super.render(context, mouseX, mouseY, delta);
    }
}
