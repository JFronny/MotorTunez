package io.gitlab.jfronny.motortunez.gui.widgets;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import minegame159.meteorclient.gui.widgets.pressable.WButton;

import java.util.Collections;

public class PlaylistHeader extends CustomWidget {
    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        AudioTrack current = MotorTunez.player.getPlayingTrack();
        WButton pauseButton = parent.add(theme.button(MotorTunez.player.isPaused() ? "Resume" : "Pause")).widget();
        pauseButton.action = () -> {
            if (MotorTunez.player.isPaused()) {
                MotorTunez.trackScheduler.setPaused(false);
                pauseButton.set("Pause");
            }
            else {
                MotorTunez.trackScheduler.setPaused(true);
                pauseButton.set("Resume");
            }
        };
        parent.add(theme.button("Shuffle")).expandX().widget().action = () -> {
            Collections.shuffle(MotorTunez.trackScheduler.tracks);
            screen.construct();
        };

        long durationTotal = current.getDuration() - current.getPosition();
        for (AudioTrack track : MotorTunez.trackScheduler.tracks) {
            durationTotal += track.getDuration();
        }
        parent.add(theme.label("Duration: " + formatTime(durationTotal)));

        parent.row();
        super.add(parent, screen, theme);
    }

    public String formatTime(long durationInMillis) {
        long millis = durationInMillis % 1000;
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
    }
}
