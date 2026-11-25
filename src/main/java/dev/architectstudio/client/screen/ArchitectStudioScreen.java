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

    public ArchitectStudioScreen() {
        super(Component.literal("ArchitectStudio"));
    }

    @Override
    protected void init() {
        int left = (this.width - 760) / 2;
        int top = (this.height - 430) / 2;
        int sidebarX = left + 18;
        int sidebarY = top + 92;

        addRenderableWidget(navButton("Рабочее место", "workspace", sidebarX, sidebarY));
        addRenderableWidget(navButton("Выделение", "selection", sidebarX, sidebarY + 38));
        addRenderableWidget(navButton("Пометки", "annotations", sidebarX, sidebarY + 76));
        addRenderableWidget(navButton("Кисти", "brushes", sidebarX, sidebarY + 114));
        addRenderableWidget(navButton("Блоки", "blocks", sidebarX, sidebarY + 152));
        addRenderableWidget(navButton("Слои", "layers", sidebarX, sidebarY + 190));

        addRenderableWidget(Button.builder(Component.literal("Закрыть"), button -> onClose())
                .bounds(left + 650, top + 386, 92, 24)
                .build());
    }

    private Button navButton(String label, String section, int x, int y) {
        return Button.builder(Component.literal(label), button -> {
            ArchitectStudioScreen next = new ArchitectStudioScreen();
            next.activeSection = section;
            Minecraft.getInstance().setScreen(next);
        }).bounds(x, y, 142, 30).build();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);

        int left = (this.width - 760) / 2;
        int top = (this.height - 430) / 2;
        int right = left + 760;
        int bottom = top + 430;

        graphics.fill(left, top, right, bottom, PANEL);
        graphics.fill(left, top, right, top + 58, 0xFF211D30);
        graphics.fill(left + 170, top + 72, right - 18, bottom - 54, 0xAA111018);
        graphics.fill(left + 1, top + 58, left + 3, bottom - 1, PURPLE);

        graphics.drawString(this.font, "ARCHITECT", left + 20, top + 17, 0xFFFFFFFF, false);
        graphics.drawString(this.font, "STUDIO", left + 91, top + 17, PURPLE, false);
        graphics.drawString(this.font, "CLIENT BUILDING TOOLKIT  •  v0.2.0", left + 20, top + 39, MUTED, false);

        graphics.drawString(this.font, "ИНСТРУМЕНТЫ", left + 22, top + 76, PURPLE, false);
        graphics.drawString(this.font, sectionTitle(), left + 190, top + 88, 0xFFFFFFFF, false);
        graphics.fill(left + 190, top + 108, right - 38, top + 109, 0xFF494052);

        renderSection(graphics, left, top);

        graphics.drawString(this.font, "Right Shift — открыть редактор", left + 190, bottom - 31, MUTED, false);
        graphics.drawString(this.font, "Мир остаётся активным", left + 500, bottom - 31, MUTED, false);

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

    private void renderSection(GuiGraphics graphics, int left, int top) {
        int x = left + 190;
        int y = top + 130;

        switch (activeSection) {
            case "selection" -> {
                drawCard(graphics, x, y, 520, 76, "Инструмент выделения",
                        "Следующий этап: рамка выделения и настройка границ");
                drawCard(graphics, x, y + 90, 250, 76, "Копировать", "Подготовим буфер выбранных блоков");
                drawCard(graphics, x + 270, y + 90, 250, 76, "Переместить", "Перемещение с визуальным предпросмотром");
            }
            case "annotations" -> {
                drawCard(graphics, x, y, 520, 76, "3D-пометки",
                        "Стрелки, линии, текстовые заметки и цветные маркеры");
                drawCard(graphics, x, y + 90, 250, 76, "Слои пометок", "Группировка и видимость");
                drawCard(graphics, x + 270, y + 90, 250, 76, "Измерения", "Линейки и размеры");
            }
            case "brushes" -> {
                drawCard(graphics, x, y, 520, 76, "Ручные кисти",
                        "Настраиваемый радиус, форма и сила воздействия");
                drawCard(graphics, x, y + 90, 250, 76, "Заливка", "Параметры будут добавлены позже");
                drawCard(graphics, x + 270, y + 90, 250, 76, "Градиент", "Контроль направления и диапазона");
            }
            case "blocks" -> {
                drawCard(graphics, x, y, 520, 76, "Каталог блоков",
                        "Поиск, избранное, категории и палитры материалов");
                drawCard(graphics, x, y + 90, 250, 76, "Избранное", "Быстрый доступ к материалам");
                drawCard(graphics, x + 270, y + 90, 250, 76, "Палитры", "Собственные наборы блоков");
            }
            case "layers" -> {
                drawCard(graphics, x, y, 520, 76, "Рабочие слои",
                        "Разделение элементов проекта и управление видимостью");
                drawCard(graphics, x, y + 90, 250, 76, "Группы", "Объединение элементов");
                drawCard(graphics, x + 270, y + 90, 250, 76, "История", "Основа для Undo / Redo");
            }
            default -> {
                drawCard(graphics, x, y, 520, 76, "Добро пожаловать в ArchitectStudio",
                        "Редактор ручного строительства — без генерации готовых построек");
                drawCard(graphics, x, y + 90, 250, 76, "Состояние игры", playerPosition());
                drawCard(graphics, x + 270, y + 90, 250, 76, "Измерение", dimensionName());
                drawCard(graphics, x, y + 180, 520, 76, "Следующий инструмент",
                        "Начнём с выделения области и визуального предпросмотра");
            }
        }
    }

    private void drawCard(GuiGraphics graphics, int x, int y, int width, int height,
                          String title, String description) {
        graphics.fill(x, y, x + width, y + height, PANEL_LIGHT);
        graphics.fill(x, y, x + 3, y + height, PURPLE);
        graphics.drawString(this.font, title, x + 14, y + 14, 0xFFFFFFFF, false);
        graphics.drawString(this.font, description, x + 14, y + 38, MUTED, false);
    }

    private String playerPosition() {
        if (Minecraft.getInstance().player == null) {
            return "Игрок не найден";
        }
        var pos = Minecraft.getInstance().player.blockPosition();
        return "X: " + pos.getX() + "   Y: " + pos.getY() + "   Z: " + pos.getZ();
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
