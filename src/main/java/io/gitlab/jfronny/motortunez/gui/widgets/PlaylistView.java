package io.gitlab.jfronny.motortunez.gui.widgets;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WTable;

public class PlaylistView extends CustomWidget {
    private AudioTrack currentTrack;

    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        currentTrack = MotorTunez.player.getPlayingTrack();
        if (currentTrack != null) {
            parent.row();
            WTable currentTracks = parent.add(theme.section("Current tracks")).expandX().widget().add(theme.table()).expandX().widget();
            super.add(currentTracks, screen, theme);
        }
    }

    public PlaylistView() {
        childWidgets.add(new PlaylistHeader());
        childWidgets.add(new CurrentTrackView());
        PaginationProvider pagination = new PaginationProvider();
        childWidgets.add(new PlaylistPage(pagination, () -> MotorTunez.trackScheduler.tracks, i -> {
            MotorTunez.trackScheduler.tracks.subList(0, i).clear();
            MotorTunez.trackScheduler.playNext(MotorTunez.player);
        }, i -> {
            MotorTunez.trackScheduler.tracks.remove(i.intValue());
            MotorTunez.trackScheduler.refreshUI();
        }));
        childWidgets.add(pagination);
    }

    public class CurrentTrackView extends CustomWidget {
        @Override
        public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
            parent.add(theme.label("Current: " + screen.getName(currentTrack))).expandX();
            parent.add(theme.minus()).widget().action = () -> {
                MotorTunez.trackScheduler.playNext(MotorTunez.player);
            };
            super.add(parent, screen, theme);
        }
    }
}
