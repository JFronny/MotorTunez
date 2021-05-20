package io.gitlab.jfronny.motortunez;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {
    public List<AudioTrack> tracks = new ArrayList<>();
    public void queue(AudioTrack track) {
        if (track == null)
            return;
        MotorTunez.log.info(track.getInfo().title);
        if (!MotorTunez.player.startTrack(track, true)) {
            if (tracks.contains(track) || MotorTunez.player.getPlayingTrack() == track)
                track = track.makeClone();
            tracks.add(track);
        } else
            MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.MUSIC);
        refreshUI();
    }
    
    public boolean isPlaying() {
        return MotorTunez.player.getPlayingTrack() != null
                && !MotorTunez.player.isPaused()
                && MotorTunez.streamPlayer.playing;
    }
    
    public void setPaused(boolean paused) {
        MotorTunez.player.setPaused(paused);
        refreshUI();
    }
    
    public void setVolume(float volume) {
        MotorTunez.player.setVolume((int)(volume * 100));
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        MotorTunez.log.info("Player paused");
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        MotorTunez.log.info("Player resumed");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            playNext(player);
        }
        else {
            refreshUI();
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        MotorTunez.toast(new TranslatableText("motor_tunez.toast.trackException.title"),
                new TranslatableText("motor_tunez.toast.trackException.description"),
                SystemToast.Type.PACK_COPY_FAILURE);
        MotorTunez.log.error(exception);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        MotorTunez.toast(new TranslatableText("motor_tunez.toast.trackStuck.title"),
                new TranslatableText("motor_tunez.toast.trackStuck.description", thresholdMs),
                SystemToast.Type.PACK_COPY_FAILURE);
        playNext(player);
    }

    public void playNext(AudioPlayer player) {
        if (!tracks.isEmpty()) {
            player.playTrack(tracks.get(0));
            tracks.remove(0);
        }
        else {
            player.stopTrack();
        }
        refreshUI();
    }

    public void refreshUI() {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof TunezScreen) {
            ((TunezScreen)screen).construct();
        }
    }
}
