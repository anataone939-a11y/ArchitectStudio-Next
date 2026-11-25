package dev.architectstudio.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class ArchitectStudioScreen extends Screen {
    private static final int BACKGROUND = 0xF014121D;
    private static final int HEADER = 0xFF211C2D;
    private static final int SIDEBAR = 0xFF191622;
    private static final int CARD = 0xFF252132;
    private static final int CARD_HOVER = 0xFF332943;
    private static final int PURPLE = 0xFFB66CFF;
    private static final int TEXT = 0xFFF4F0FA;
    private static final int MUTED = 0xFFAAA2B8;

    private static final String[] SECTION_IDS = {
            "workspace", "selection", "annotations", "brushes", "blocks", "layers"
    };
    private static final String[] SECTION_NAMES = {
            "Рабочее место", "Выделение", "Пометки", "Кисти", "Блоки", "Слои"
    };

    private String activeSection = "workspace";
    private boolean gridEnabled = true;
    private boolean snapEnabled = true;
    private boolean previewEnabled = true;
    private int panelLeft, panelTop, panelWidth, panelHeight, sidebarWidth;
    private int contentX, contentY, contentWidth;
    private int closeX, closeY, closeWidth, closeHeight;

    public ArchitectStudioScreen() {
        super(Component.literal("ArchitectStudio"));
    }

    @Override
    protected void init() {
        panelWidth = Math.min(900, Math.max(320, width - 24));
        panelHeight = Math.min(540, Math.max(280, height - 24));
        panelLeft = (width - panelWidth) / 2;
        panelTop = (height - panelHeight) / 2;
        sidebarWidth = Math.min(190, Math.max(112, panelWidth / 4));
        contentX = panelLeft + sidebarWidth + 26;
        contentY = panelTop + 112;
        contentWidth = panelLeft + panelWidth - contentX - 22;
        closeWidth = 82;
        closeHeight = 22;
        closeX = panelLeft + panelWidth - closeWidth - 18;
        closeY = panelTop + panelHeight - closeHeight - 14;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        int right = panelLeft + panelWidth;
        int bottom = panelTop + panelHeight;

        graphics.fill(panelLeft, panelTop, right, bottom, BACKGROUND);
        graphics.fill(panelLeft, panelTop, right, panelTop + 68, HEADER);
        graphics.fill(panelLeft, panelTop + 68, panelLeft + sidebarWidth, bottom, SIDEBAR);
        graphics.fill(panelLeft, panelTop + 67, right, panelTop + 69, PURPLE);

        graphics.drawString(font, "ARCHITECT", panelLeft + 22, panelTop + 18, TEXT, false);
        graphics.drawString(font, "STUDIO", panelLeft + 98, panelTop + 18, PURPLE, false);
        graphics.drawString(font, "BUILDING WORKSPACE  /  CLIENT EDITOR  /  v0.3.1",
                panelLeft + 22, panelTop + 42, MUTED, false);

        graphics.drawString(font, "ИНСТРУМЕНТЫ", panelLeft + 16, panelTop + 86, PURPLE, false);
        for (int i = 0; i < SECTION_IDS.length; i++) {
            int x = panelLeft + 10;
            int y = panelTop + 108 + i * 38;
            int w = sidebarWidth - 20;
            boolean selected = activeSection.equals(SECTION_IDS[i]);
            boolean hovered = inside(mouseX, mouseY, x, y, w, 30);
            if (selected || hovered) {
                graphics.fill(x, y, x + w, y + 30, selected ? CARD_HOVER : CARD);
            }
            if (selected) graphics.fill(x, y, x + 3, y + 30, PURPLE);
            graphics.drawString(font, SECTION_NAMES[i], x + 12, y + 11,
                    selected ? TEXT : MUTED, false);
        }

        graphics.fill(contentX - 14, panelTop + 84, contentX - 13, bottom - 52, 0xFF453A52);
        graphics.drawString(font, sectionTitle(), contentX, panelTop + 88, TEXT, false);
        graphics.drawString(font, "Изменения и инструменты отображаются здесь",
                contentX, panelTop + 101, MUTED, false);
        graphics.fill(contentX, panelTop + 124, right - 18, panelTop + 125, 0xFF453A52);

        renderSection(graphics, mouseX, mouseY);

        graphics.drawString(font, "Right Shift  ·  открыть редактор", contentX,
                bottom - 24, MUTED, false);
        boolean closeHover = inside(mouseX, mouseY, closeX, closeY, closeWidth, closeHeight);
        graphics.fill(closeX, closeY, closeX + closeWidth, closeY + closeHeight,
                closeHover ? CARD_HOVER : CARD);
        graphics.drawCenteredString(font, "Закрыть", closeX + closeWidth / 2,
                closeY + 7, TEXT);
    }

    private void renderSection(GuiGraphics graphics, int mouseX, int mouseY) {
        int gap = 12;
        int cardWidth = Math.max(100, (contentWidth - gap) / 2);
        int cardHeight = 76;
        int y1 = contentY;
        int y2 = y1 + cardHeight + 12;
        switch (activeSection) {
            case "selection" -> {
                drawCard(graphics, contentX, y1, contentWidth, cardHeight, "Область выделения",
                        "Ручное выделение блоков и настройка границ", mouseX, mouseY);
                drawCard(graphics, contentX, y2, cardWidth, cardHeight, "Копировать / вставить",
                        "Подготовка операций с выбранной областью", mouseX, mouseY);
                drawCard(graphics, contentX + cardWidth + gap, y2, cardWidth, cardHeight,
                        "Трансформация", "Перемещение и поворот выделения", mouseX, mouseY);
            }
            case "annotations" -> {
                drawCard(graphics, contentX, y1, contentWidth, cardHeight, "3D-пометки",
                        "Линии, стрелки, текст и цветные маркеры", mouseX, mouseY);
                drawCard(graphics, contentX, y2, cardWidth, cardHeight, "Измерения",
                        "Линейки, расстояния и размеры", mouseX, mouseY);
                drawCard(graphics, contentX + cardWidth + gap, y2, cardWidth, cardHeight,
                        "Слои пометок", "Группировка и видимость заметок", mouseX, mouseY);
            }
            case "brushes" -> {
                drawCard(graphics, contentX, y1, contentWidth, cardHeight, "Ручные кисти",
                        "Настройка радиуса, формы и силы", mouseX, mouseY);
                drawCard(graphics, contentX, y2, cardWidth, cardHeight, "Заливка",
                        "Ручное заполнение выбранной области", mouseX, mouseY);
                drawCard(graphics, contentX + cardWidth + gap, y2, cardWidth, cardHeight,
                        "Градиент", "Переход между материалами", mouseX, mouseY);
            }
            case "blocks" -> {
                drawCard(graphics, contentX, y1, contentWidth, cardHeight, "Каталог блоков",
                        "Поиск стандартных и модифицированных блоков", mouseX, mouseY);
                drawCard(graphics, contentX, y2, cardWidth, cardHeight, "Избранное",
                        "Быстрый доступ к материалам", mouseX, mouseY);
                drawCard(graphics, contentX + cardWidth + gap, y2, cardWidth, cardHeight,
                        "Палитры", "Наборы блоков для проекта", mouseX, mouseY);
            }
            case "layers" -> {
                drawCard(graphics, contentX, y1, contentWidth, cardHeight, "Рабочие слои",
                        "Разделение проекта на независимые части", mouseX, mouseY);
                drawCard(graphics, contentX, y2, cardWidth, cardHeight, "Группы",
                        "Объединение элементов проекта", mouseX, mouseY);
                drawCard(graphics, contentX + cardWidth + gap, y2, cardWidth, cardHeight,
                        "История действий", "Основа для Undo / Redo", mouseX, mouseY);
            }
            default -> {
                drawCard(graphics, contentX, y1, contentWidth, cardHeight,
                        "Добро пожаловать в ArchitectStudio",
                        "Ручное редактирование мира — без генерации построек", mouseX, mouseY);
                drawCard(graphics, contentX, y2, cardWidth, cardHeight, "Координаты игрока",
                        playerPosition(), mouseX, mouseY);
                drawCard(graphics, contentX + cardWidth + gap, y2, cardWidth, cardHeight,
                        "Измерение", dimensionName(), mouseX, mouseY);
                int toggleY = y2 + cardHeight + 18;
                drawToggle(graphics, contentX, toggleY, "Сетка", gridEnabled);
                drawToggle(graphics, contentX + 118, toggleY, "Привязка", snapEnabled);
                drawToggle(graphics, contentX + 248, toggleY, "Предпросмотр", previewEnabled);
            }
        }
    }

    private void drawCard(GuiGraphics graphics, int x, int y, int w, int h,
                          String title, String subtitle, int mouseX, int mouseY) {
        boolean hover = inside(mouseX, mouseY, x, y, w, h);
        graphics.fill(x, y, x + w, y + h, hover ? CARD_HOVER : CARD);
        graphics.fill(x, y, x + 3, y + h, PURPLE);
        graphics.drawString(font, title, x + 12, y + 16, TEXT, false);
        graphics.drawString(font, subtitle, x + 12, y + 43, MUTED, false);
    }

    private void drawToggle(GuiGraphics graphics, int x, int y, String label, boolean enabled) {
        graphics.fill(x, y, x + 104, y + 28, CARD);
        graphics.drawString(font, label, x + 8, y + 10, TEXT, false);
        graphics.fill(x + 78, y + 7, x + 96, y + 21, enabled ? PURPLE : 0xFF625B6D);
        graphics.fill(x + (enabled ? 87 : 80), y + 9, x + (enabled ? 94 : 87), y + 19, TEXT);
    }

    private String sectionTitle() {
        for (int i = 0; i < SECTION_IDS.length; i++) {
            if (SECTION_IDS[i].equals(activeSection)) return SECTION_NAMES[i].toUpperCase();
        }
        return "РАБОЧЕЕ МЕСТО";
    }

    private String playerPosition() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return "Игрок не найден";
        var pos = mc.player.blockPosition();
        return "X " + pos.getX() + "   Y " + pos.getY() + "   Z " + pos.getZ();
    }

    private String dimensionName() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return "Нет активного мира";
        ResourceLocation id = mc.level.dimension().location();
        return id.toString();
    }

    private boolean inside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        if (inside(mouseX, mouseY, closeX, closeY, closeWidth, closeHeight)) {
            onClose();
            return true;
        }
        for (int i = 0; i < SECTION_IDS.length; i++) {
            int x = panelLeft + 10;
            int y = panelTop + 108 + i * 38;
            if (inside(mouseX, mouseY, x, y, sidebarWidth - 20, 30)) {
                activeSection = SECTION_IDS[i];
                return true;
            }
        }
        if ("workspace".equals(activeSection)) {
            int y = contentY + 2 * 76 + 30;
            if (inside(mouseX, mouseY, contentX, y, 104, 28)) gridEnabled = !gridEnabled;
            else if (inside(mouseX, mouseY, contentX + 118, y, 104, 28)) snapEnabled = !snapEnabled;
            else if (inside(mouseX, mouseY, contentX + 248, y, 104, 28)) previewEnabled = !previewEnabled;
            else return super.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
