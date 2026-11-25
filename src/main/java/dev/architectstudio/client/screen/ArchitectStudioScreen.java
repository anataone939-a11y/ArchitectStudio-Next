package dev.architectstudio.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class ArchitectStudioScreen extends Screen {
    private static final int PANEL = 0xF0181722;
    private static final int PANEL_LIGHT = 0xFF242132;
    private static final int PURPLE = 0xFFB66CFF;
    private static final int MUTED = 0xFFAAA5B8;

    private String activeSection = "workspace";

    private int panelLeft;
    private int panelTop;
    private int panelWidth;
    private int panelHeight;
    private int contentX;
    private int contentWidth;

    public ArchitectStudioScreen() {
        super(Component.literal("ArchitectStudio"));
    }

    @Override
    protected void init() {
        // Keep the whole editor inside the usable game window.
        panelWidth = Math.min(760, Math.max(300, this.width - 24));
        panelHeight = Math.min(430, Math.max(260, this.height - 24));
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;

        int sidebarWidth = Math.min(160, Math.max(126, panelWidth / 4));
        int sidebarX = panelLeft + 12;
        int sidebarY = panelTop + 88;
        int navWidth = sidebarWidth - 20;
        int navHeight = 28;
        int navGap = 7;

        addRenderableWidget(navButton("Рабочее место", "workspace",
                sidebarX, sidebarY, navWidth, navHeight));
        addRenderableWidget(navButton("Выделение", "selection",
                sidebarX, sidebarY + (navHeight + navGap), navWidth, navHeight));
        addRenderableWidget(navButton("Пометки", "annotations",
                sidebarX, sidebarY + 2 * (navHeight + navGap), navWidth, navHeight));
        addRenderableWidget(navButton("Кисти", "brushes",
                sidebarX, sidebarY + 3 * (navHeight + navGap), navWidth, navHeight));
        addRenderableWidget(navButton("Блоки", "blocks",
                sidebarX, sidebarY + 4 * (navHeight + navGap), navWidth, navHeight));
        addRenderableWidget(navButton("Слои", "layers",
                sidebarX, sidebarY + 5 * (navHeight + navGap), navWidth, navHeight));

        contentX = panelLeft + sidebarWidth + 24;
        contentWidth = panelLeft + panelWidth - contentX - 18;

        addRenderableWidget(Button.builder(Component.literal("Закрыть"), button -> onClose())
                .bounds(panelLeft + panelWidth - 104, panelTop + panelHeight - 34, 88, 22)
                .build());
    }

    private Button navButton(String label, String section, int x, int y, int width, int height) {
        return Button.builder(Component.literal(label), button -> {
            activeSection = section;
            clearWidgets();
            init();
        }).bounds(x, y, width, height).build();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);

        int left = panelLeft;
        int top = panelTop;
        int right = left + panelWidth;
        int bottom = top + panelHeight;

        graphics.fill(left, top, right, bottom, PANEL);
        graphics.fill(left, top, right, top + 58, 0xFF211D30);
        graphics.fill(left + 1, top + 58, left + 3, bottom - 1, PURPLE);
        graphics.fill(contentX - 12, top + 68, contentX - 11, bottom - 12, 0xFF494052);

        graphics.drawString(this.font, "ARCHITECT", left + 20, top + 17, 0xFFFFFFFF, false);
        graphics.drawString(this.font, "STUDIO", left + 91, top + 17, PURPLE, false);
        graphics.drawString(this.font, "CLIENT BUILDING TOOLKIT  •  v0.2.1", left + 20, top + 39, MUTED, false);

        graphics.drawString(this.font, "ИНСТРУМЕНТЫ", left + 14, top + 76, PURPLE, false);
        graphics.drawString(this.font, sectionTitle(), contentX, top + 82, 0xFFFFFFFF, false);
        graphics.fill(contentX, top + 102, right - 18, top + 103, 0xFF494052);

        renderSection(graphics, contentX, top + 120, contentWidth, bottom - top);

        graphics.drawString(this.font, "Right Shift — открыть редактор",
                contentX, bottom - 23, MUTED, false);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private String sectionTitle() {
        return switch (activeSection) {
            case "selection" -> "ВЫДЕЛЕНИЕ";
            case "annotations" -> "ПОМЕТКИ И РИСОВАНИЕ";
            case "brushes" -> "КИСТИ";
            case "blocks" -> "БИБЛИОТЕКА БЛОКОВ";
            case "layers" -> "СЛОИ";
            default -> "РАБОЧЕЕ МЕСТО";
        };
    }

    private void renderSection(GuiGraphics graphics, int x, int y, int width, int bottom) {
        int gap = 12;
        int cardWidth = Math.max(90, (width - gap) / 2);
        int cardHeight = 66;
        int row2 = y + cardHeight + 12;

        switch (activeSection) {
            case "selection" -> {
                drawCard(graphics, x, y, width, cardHeight, "Инструмент выделения",
                        "Рамка выделения и настройка границ");
                drawCard(graphics, x, row2, cardWidth, cardHeight, "Копировать",
                        "Буфер выбранных блоков");
                drawCard(graphics, x + cardWidth + gap, row2, cardWidth, cardHeight,
                        "Переместить", "Предпросмотр перемещения");
            }
            case "annotations" -> {
                drawCard(graphics, x, y, width, cardHeight, "3D-пометки",
                        "Стрелки, линии, текст и цветные маркеры");
                drawCard(graphics, x, row2, cardWidth, cardHeight, "Слои пометок",
                        "Группировка и видимость");
                drawCard(graphics, x + cardWidth + gap, row2, cardWidth, cardHeight,
                        "Измерения", "Линейки и размеры");
            }
            case "brushes" -> {
                drawCard(graphics, x, y, width, cardHeight, "Ручные кисти",
                        "Радиус, форма и сила воздействия");
                drawCard(graphics, x, row2, cardWidth, cardHeight, "Заливка",
                        "Настройка области");
                drawCard(graphics, x + cardWidth + gap, row2, cardWidth, cardHeight,
                        "Градиент", "Направление и диапазон");
            }
            case "blocks" -> {
                drawCard(graphics, x, y, width, cardHeight, "Каталог блоков",
                        "Поиск, избранное и категории");
                drawCard(graphics, x, row2, cardWidth, cardHeight, "Избранное",
                        "Быстрый доступ");
                drawCard(graphics, x + cardWidth + gap, row2, cardWidth, cardHeight,
                        "Палитры", "Наборы материалов");
            }
            case "layers" -> {
                drawCard(graphics, x, y, width, cardHeight, "Рабочие слои",
                        "Разделение элементов проекта");
                drawCard(graphics, x, row2, cardWidth, cardHeight, "Группы",
                        "Объединение элементов");
                drawCard(graphics, x + cardWidth + gap, row2, cardWidth, cardHeight,
                        "История", "Основа Undo / Redo");
            }
            default -> {
                drawCard(graphics, x, y, width, cardHeight, "Добро пожаловать в ArchitectStudio",
                        "Ручное редактирование — без генерации построек");
                drawCard(graphics, x, row2, cardWidth, cardHeight, "Состояние игры",
                        playerPosition());
                drawCard(graphics, x + cardWidth + gap, row2, cardWidth, cardHeight,
                        "Измерение", dimensionName());
            }
        }
    }

    private void drawCard(GuiGraphics graphics, int x, int y, int width, int height,
                          String title, String description) {
        graphics.fill(x, y, x + width, y + height, PANEL_LIGHT);
        graphics.fill(x, y, x + 3, y + height, PURPLE);
        graphics.drawString(this.font, title, x + 10, y + 12, 0xFFFFFFFF, false);
        graphics.drawString(this.font, description, x + 10, y + 36, MUTED, false);
    }

    private String playerPosition() {
        if (Minecraft.getInstance().player == null) {
            return "Игрок не найден";
        }
        var pos = Minecraft.getInstance().player.blockPosition();
        return "X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ();
    }

    private String dimensionName() {
        if (Minecraft.getInstance().level == null) {
            return "Нет активного мира";
        }
        ResourceLocation location = Minecraft.getInstance().level.dimension().location();
        return location.toString();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
