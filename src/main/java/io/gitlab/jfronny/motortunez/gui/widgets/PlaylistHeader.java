package io.gitlab.jfronny.motortunez.gui.widgets;

import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WHorizontalList;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import minegame159.meteorclient.gui.widgets.pressable.WButton;

import java.util.Collections;

public class PlaylistHeader extends CustomWidget {
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
        list.add(theme.button("Shuffle")).expandX().widget().action = () -> {
            Collections.shuffle(MotorTunez.trackScheduler.tracks);
            screen.construct();
        };

        parent.add(theme.label("Duration: " + MotorTunez.getTime())).right();
        parent.row();
        super.add(parent, screen, theme);
    }
}
