package io.gitlab.jfronny.motortunez.gui;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.widgets.CustomWidget;
import io.gitlab.jfronny.motortunez.gui.widgets.PaginationProvider;
import io.gitlab.jfronny.motortunez.gui.widgets.PlaylistPage;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.WindowScreen;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsScreen extends WindowScreen {
    private final AudioPlaylist results;
    private final TunezScreen tunezScreen;
    private List<CustomWidget> childWidgets;
    private WTable table;

    public SearchResultsScreen(GuiTheme theme, AudioPlaylist results, TunezScreen tunezScreen) {
        super(theme, "Music - Results");
        this.results = results;
        this.tunezScreen = tunezScreen;
        this.parent = tunezScreen;
    }

    @Override
    protected void init() {
        super.init();
        childWidgets = new ArrayList<>();
        PaginationProvider pagination = new PaginationProvider();
        childWidgets.add(new PlaylistPage(pagination, results::getTracks, i -> {
            MotorTunez.trackScheduler.queue(results.getTracks().get(i));
        }, null));
        childWidgets.add(pagination);
        if (table != null) table.clear();
        clear();
        table = add(theme.table()).expandX().minWidth(300).widget();
        construct();
    }

    public void construct() {
        table.clear();
        table.add(theme.button("Add all")).widget().action = () -> {
            for (AudioTrack track : results.getTracks()) {
                MotorTunez.trackScheduler.queue(track);
            }
            MinecraftClient.getInstance().openScreen(parent);
            tunezScreen.construct();
        };
        table.add(theme.button("OK")).widget().action = () -> {
            MinecraftClient.getInstance().openScreen(parent);
            tunezScreen.construct();
        };
        for (CustomWidget customWidget : childWidgets) {
            customWidget.add(table, tunezScreen, theme);
        }
    }
}
