package io.gitlab.jfronny.motortunez.util;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import io.gitlab.jfronny.motortunez.MotorTunez;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlaylistUtil {
    private static final Map<String, AudioPlaylist> playlists = new HashMap<>();
    private static final Path filePath = FabricLoader.getInstance().getGameDir().resolve("meteor-client").resolve("playlists.txt");
    public static void load() {
        playlists.clear();
        if (!Files.exists(filePath)) {
            playlists.put("https://music.youtube.com/playlist?list=PLM2V-zC1RSteO_kwDNGI0EvDSBRhKaUZq", null); // cloud kid
            playlists.put("https://music.youtube.com/watch?v=BAPRv3Zts_w&list=RDQMuICnGifx4w8", null); // chill nation mix
            playlists.put("https://music.youtube.com/playlist?list=PLDfKAXSi6kUbhK3vq2bkwqAMdLF4tk-Ff", null); // MrSuicideSheep
            playlists.put("https://music.youtube.com/playlist?list=PLRBp0Fe2GpglKIXdvLnzcnCdRwEr3tbkO", null); // NCS
            playlists.put("https://music.youtube.com/playlist?list=PL2vYabJDBczNGOYVGXMIlH4G_wcYje5Oi", null); // rap nation
            playlists.put("https://music.youtube.com/playlist?list=PLC1og_v3eb4h1MA88R3JHcPoLUpCRtxiZ", null); // trap nation bestof
            playlists.put("https://music.youtube.com/playlist?list=PLU_bQfSFrM2PemIeyVUSjZjJhm6G7auOY", null); // trap city
            save();
            playlists.clear();
        }
        try {
            Files.lines(filePath).forEach(s -> {
                if (s != null) {
                    SearchUtil.search(s, (title, description) -> {
                        MotorTunez.log.error(description.asString());
                    }, playlist -> playlists.put(s, playlist));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        try {
            Files.write(filePath, playlists.keySet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void add(String source) {
        playlists.put(source, null);
        save();
        load();
    }

    public static void remove(String source) {
        playlists.remove(source);
        save();
    }

    public static int count() {
        return playlists.size();
    }

    public static List<Map.Entry<String, AudioPlaylist>> getEntriesOrdered() {
        List<Map.Entry<String, AudioPlaylist>> l = new ArrayList<>(playlists.entrySet());
        l.sort((e1, e2) -> {
            int res = String.CASE_INSENSITIVE_ORDER.compare(e1.getValue().getName(), e2.getValue().getName());
            if (res == 0) {
                res = e1.getValue().getName().compareTo(e2.getValue().getName());
            }
            return res;
        });
        return l;
    }
}
