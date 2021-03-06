package io.gitlab.jfronny.motortunez.gui.widgets;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;

public class CurrentTracksView extends CustomWidget {
    private AudioTrack currentTrack;
    private final PaginationProvider pagination;

    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        currentTrack = MotorTunez.player.getPlayingTrack();
        parent.row();
        WTable currentTracks = parent.add(theme.section("Current tracks")).expandX().widget().add(theme.table()).expandX().widget();
        super.add(currentTracks, screen, theme);
    }

    public CurrentTracksView(TunezScreen screen) {
        childWidgets.add(new PlaybackControls());
        childWidgets.add(new CurrentTrackView());
        pagination = new PaginationProvider(i -> screen.construct());
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
            if (currentTrack != null && pagination.getPageOffset() == 0) {
                parent.add(theme.label("Current: " + screen.getName(currentTrack))).expandX();
                parent.add(theme.minus()).widget().action = () -> {
                    MotorTunez.trackScheduler.playNext(MotorTunez.player);
                };
                if (MotorTunez.trackScheduler.hasNext())
                    parent.row();
            }
            super.add(parent, screen, theme);
        }
    }
}
