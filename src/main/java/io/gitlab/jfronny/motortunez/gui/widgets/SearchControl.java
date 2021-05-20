package io.gitlab.jfronny.motortunez.gui.widgets;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WTable;

public class SearchControl extends CustomWidget {
    public AudioPlaylist searchResults;
    private SearchBar searchBar;
    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        searchBar.add(parent, screen, theme);
        if (searchResults == null || searchResults.getTracks().isEmpty())
            return;
        parent.row();
        WTable p = parent.add(theme.section("Search results")).expandX().widget().add(theme.table()).expandX().widget();
        p.add(theme.button("Add all")).widget().action = () -> {
            for (AudioTrack track : searchResults.getTracks()) {
                MotorTunez.trackScheduler.queue(track);
            }
            searchResults = null;
            screen.construct();
        };
        p.add(theme.button("OK")).widget().action = () -> {
            searchResults = null;
            screen.construct();
        };
        super.add(p, screen, theme);
    }

    public SearchControl() {
        searchBar = new SearchBar(this);
        PaginationProvider pagination = new PaginationProvider();
        childWidgets.add(new PlaylistPage(pagination, () -> searchResults.getTracks(), i -> {
            MotorTunez.trackScheduler.queue(searchResults.getTracks().get(i));
        }, null));
        childWidgets.add(pagination);
    }
}
