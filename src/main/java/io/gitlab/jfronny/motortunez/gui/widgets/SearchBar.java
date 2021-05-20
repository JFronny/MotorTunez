package io.gitlab.jfronny.motortunez.gui.widgets;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.MotorTunez;
import io.gitlab.jfronny.motortunez.gui.SearchResultsScreen;
import io.gitlab.jfronny.motortunez.gui.TunezScreen;
import minegame159.meteorclient.gui.GuiTheme;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import minegame159.meteorclient.gui.widgets.input.WTextBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class SearchBar extends CustomWidget {
    @Override
    public void add(WTable parent, TunezScreen screen, GuiTheme theme) {
        WTextBox box = parent.add(theme.textBox("")).expandX().widget();
        parent.add(theme.button("Search")).widget().action = () -> {
            String url = box.get();
            if (!url.startsWith("http:") && !url.startsWith("https:"))
                url = "ytmsearch:" + url;
            MotorTunez.playerManager.loadItem(url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    MotorTunez.trackScheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    if (playlist.getTracks().isEmpty())
                        return;
                    MinecraftClient.getInstance().openScreen(new SearchResultsScreen(theme, playlist, screen));
                }

                @Override
                public void noMatches() {
                    MotorTunez.toast(new TranslatableText("motor_tunez.toast.noMatches.title"),
                            new TranslatableText("motor_tunez.toast.noMatches.description"),
                            SystemToast.Type.PACK_COPY_FAILURE);
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    MotorTunez.toast(new TranslatableText("motor_tunez.toast.loadFailed.title"),
                            new LiteralText(exception.severity.toString()),
                            SystemToast.Type.PACK_COPY_FAILURE);
                    MotorTunez.log.error(exception);
                }
            });
        };
        super.add(parent, screen, theme);
    }
}
