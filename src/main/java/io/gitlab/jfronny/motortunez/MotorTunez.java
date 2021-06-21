package io.gitlab.jfronny.motortunez;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.gitlab.jfronny.motortunez.gui.TunezTab;
import io.gitlab.jfronny.motortunez.hud.TunezHud;
import io.gitlab.jfronny.motortunez.util.PlaylistUtil;
import io.gitlab.jfronny.motortunez.util.StreamPlayer;
import meteordevelopment.meteorclient.MeteorAddon;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class MotorTunez extends MeteorAddon {
    public static DefaultAudioPlayerManager playerManager;
    public static AudioPlayer player;
    public static TrackScheduler trackScheduler;
    public static StreamPlayer streamPlayer;
    public static Thread soundThread;
    public static final Logger log = LogManager.getLogger("motor_tunez");
    
    @Override
    public void onInitialize() {
        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setOutputFormat(StandardAudioDataFormats.COMMON_PCM_S16_BE);
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
        trackScheduler = new TrackScheduler();
        trackScheduler.setVolume(((ISoundManager)MinecraftClient.getInstance().getSoundManager()).getMusicVolume());
        player.addListener(trackScheduler);
        streamPlayer = new StreamPlayer();
        Tabs.add(new TunezTab());
        PlaylistUtil.load();
        start();

        HUD hud = Modules.get().get(HUD.class);
        hud.elements.add(new TunezHud(hud));
    }
    
    public static void toast(Text title, Text description, SystemToast.Type type) {
        SystemToast.add(MinecraftClient.getInstance().getToastManager(), type, title, description);
    }

    public static void start() {
        soundThread = new Thread(streamPlayer, "MotorTunez");
        soundThread.start();
    }

    public static String getTime() {
        AudioTrack current = MotorTunez.player.getPlayingTrack();
        if (current == null)
            return "";
        long durationTotal = current.getDuration() - current.getPosition();
        for (AudioTrack track : MotorTunez.trackScheduler.tracks) {
            durationTotal += track.getDuration();
        }
        long minute = (durationTotal / (1000 * 60)) % 60;
        long hour = (durationTotal / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d", hour, minute);
    }
}
