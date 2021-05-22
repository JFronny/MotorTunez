package io.gitlab.jfronny.motortunez.gui.widgets;

import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.PlaylistsScreen;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WHorizontalList;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import minegame159.meteorclient.gui.widgets.pressable.WButton;
import net.minecraft.client.MinecraftClient;

import java.util.Collections;

public class PlaybackControls extends CustomWidget {
    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        WHorizontalList list = parent.add(theme.horizontalList()).widget();
        WButton pauseButton = list.add(theme.button(MotorTunez.player.isPaused() ? "Resume" : "Pause")).widget();
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
        if (MotorTunez.trackScheduler.hasNext()) {
            list.add(theme.button("Shuffle")).widget().action = () -> {
                Collections.shuffle(MotorTunez.trackScheduler.tracks);
                screen.construct();
            };
            list.add(theme.button("Clear")).widget().action = () -> {
                MotorTunez.trackScheduler.tracks.clear();
                screen.construct();
            };
        }
        list.add(theme.button("Playlists")).widget().action = () -> {
            MinecraftClient.getInstance().openScreen(new PlaylistsScreen(theme, screen));
        };

        list.add(theme.label("Duration: " + MotorTunez.getTime())).right();

        parent.row();
        super.add(parent, screen, theme);
    }
}
