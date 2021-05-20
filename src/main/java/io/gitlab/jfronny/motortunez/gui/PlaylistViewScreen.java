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
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class PlaylistViewScreen extends WindowScreen {
    private final AudioPlaylist results;
    private final TunezScreen tunezScreen;
    private List<CustomWidget> childWidgets;
    private WTable table;

    public PlaylistViewScreen(GuiTheme theme, AudioPlaylist playlist, TunezScreen tunezScreen) {
        super(theme, "Music - " + playlist.getName());
        this.results = playlist;
        this.tunezScreen = tunezScreen;
        this.parent = tunezScreen;
    }

    @Override
    protected void init() {
        super.init();
        childWidgets = new ArrayList<>();
        PaginationProvider pagination = new PaginationProvider(j -> construct());
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
        table.add(theme.button("Add all")).expandX().widget().action = () -> {
            for (AudioTrack track : results.getTracks()) {
                MotorTunez.trackScheduler.queue(track);
            }
            MinecraftClient.getInstance().openScreen(parent);
            tunezScreen.construct();
        };
        table.row();
        for (CustomWidget customWidget : childWidgets) {
            customWidget.add(table, tunezScreen, theme);
        }
    }
    
    public PlaylistViewScreen setParent(Screen screen) {
        parent = screen;
        return this;
    }
}
