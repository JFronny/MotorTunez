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

        parent.add(theme.label("Duration: " + MotorTunez.getTime()));

        parent.row();
        super.add(parent, screen, theme);
    }
}
