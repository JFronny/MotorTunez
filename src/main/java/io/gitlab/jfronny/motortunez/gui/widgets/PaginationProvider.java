package io.gitlab.jfronny.motortunez.gui.widgets;

import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WTable;

public class PaginationProvider extends CustomWidget {
    private int currentPage = 0;
    private int maxPage = 0;
    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        if (maxPage > screen.pageSize) {
            parent.row();
            for (int i = 0; i < maxPage; i++) {
                int j = i;
                String pageName = Integer.toString(j + 1);
                if (j == currentPage) {
                    parent.add(theme.label(pageName));
                }
                else {
                    parent.add(theme.button(pageName)).widget().action = () -> {
                        currentPage = j;
                        screen.construct();
                    };
                }
            }
        }
        super.add(parent, screen, theme);
    }
    
    public void setMaxPage(int max) {
        maxPage = max;
        if (currentPage > max)
            currentPage = max;
    }
    
    public int getPageOffset() {
        return currentPage * TunezScreen.pageSize;
    }
}
