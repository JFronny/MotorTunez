package io.gitlab.jfronny.motortunez.gui;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.gui.widgets.CustomWidget;
import io.gitlab.jfronny.motortunez.gui.widgets.CurrentTracksView;
import io.gitlab.jfronny.motortunez.gui.widgets.SearchBar;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.tabs.Tab;
import minegame159.meteorclient.gui.tabs.WindowTabScreen;
import minegame159.meteorclient.gui.widgets.containers.WTable;

import java.util.ArrayList;
import java.util.List;

public class TunezScreen extends WindowTabScreen {
    public TunezScreen(GuiTheme theme, Tab tab) {
        super(theme, tab);
    }
    private WTable table;
    public static final int pageSize = 10;
    private List<CustomWidget> childWidgets;

    @Override
    protected void init() {
        super.init();
        childWidgets = new ArrayList<>();
        childWidgets.add(new SearchBar());
        childWidgets.add(new CurrentTracksView(this));
        if (table != null) table.clear();
        clear();
        table = add(theme.table()).expandX().minWidth(300).widget();
        construct();
    }

    public void construct() {
        table.clear();
        for (CustomWidget customWidget : childWidgets) {
            customWidget.add(table, this, theme);
        }
    }

    public String getName(AudioTrack track) {
        if (track == null)
            return "Not playing";
        return track.getInfo().title + " (" + track.getInfo().author + ")";
    }
}
