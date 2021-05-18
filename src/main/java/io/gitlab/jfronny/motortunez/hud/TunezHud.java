package io.gitlab.jfronny.motortunez.hud;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sun.tools.javac.util.List;
import io.gitlab.jfronny.motortunez.MotorTunez;
import minegame159.meteorclient.systems.modules.render.hud.HUD;
import minegame159.meteorclient.systems.modules.render.hud.HudRenderer;
import minegame159.meteorclient.systems.modules.render.hud.modules.HudElement;

public class TunezHud extends HudElement {
    public TunezHud(HUD hud) {
        super(hud, "tunez", "Displays the current music");
        //TODO fancier HUD with scrollbar-like view for time left in song and number of songs after it
        //     also display more info (optional)
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(Math.max(renderer.textWidth(getText(0)), renderer.textWidth(getText(1))), renderer.textHeight() * 2 + 2);
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        for (String t : List.of(getText(0), getText(1))) {
            renderer.text(t, x, y, hud.primaryColor.get());
            y += 2 + renderer.textHeight();
        }
    }
    
    private String getText(int line) {
        if (MotorTunez.player == null)
            return "";
        AudioTrack t = MotorTunez.player.getPlayingTrack();
        if (line == 0) {
            if (t == null)
                return "Not playing";
            return "[" + (MotorTunez.trackScheduler.isPlaying() ? "p" : "x") + "] " + t.getInfo().title;
        } else {
            if (t == null)
                return "";
            return "Left: " + MotorTunez.getTime();
        }
    }
}
