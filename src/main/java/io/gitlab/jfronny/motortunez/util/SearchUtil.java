package io.gitlab.jfronny.motortunez.util;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SearchUtil {
    public static void search(String url, BiConsumer<Text, Text> errorHandler, Consumer<AudioPlaylist> success) {
        if (url == null)
            errorHandler.accept(new TranslatableText("motor_tunez.toast.loadFailed.title"),
                    new TranslatableText("motor_tunez.toast.loadFailed.description"));
        else {
            if (!url.startsWith("http:") && !url.startsWith("https:"))
                url = "ytmsearch:" + url;
            MotorTunez.playerManager.loadItem(url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    MotorTunez.trackScheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    if (playlist.getTracks().isEmpty()) noMatches();
                    else success.accept(playlist);
                }

                @Override
                public void noMatches() {
                    errorHandler.accept(new TranslatableText("motor_tunez.toast.noMatches.title"),
                            new TranslatableText("motor_tunez.toast.noMatches.description"));
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    errorHandler.accept(new TranslatableText("motor_tunez.toast.loadFailed.title"),
                            new LiteralText(exception.severity.toString()));
                    MotorTunez.log.error(exception);
                }
            });
        }
    }
}
