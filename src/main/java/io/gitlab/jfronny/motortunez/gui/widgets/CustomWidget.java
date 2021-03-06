package io.gitlab.jfronny.motortunez.gui.widgets;

import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomWidget {
    protected List<CustomWidget> childWidgets = new ArrayList<>();
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        for (CustomWidget child : childWidgets) {
            child.add(parent, screen, theme);
        }
    }
}
