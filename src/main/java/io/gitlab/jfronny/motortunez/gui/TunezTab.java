package io.gitlab.jfronny.motortunez.gui;

import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.tabs.Tab;
import minegame159.meteorclient.gui.tabs.TabScreen;
import net.minecraft.client.gui.screen.Screen;

public class TunezTab extends Tab {
    public TunezTab() {
        super("Music");
    }

    @Override
    protected TabScreen createScreen(GuiTheme theme) {
        return new TunezScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof TunezScreen;
    }
}
