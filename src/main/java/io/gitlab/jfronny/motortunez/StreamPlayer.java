package io.gitlab.jfronny.motortunez;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;

import javax.sound.sampled.*;
import java.io.IOException;

public class StreamPlayer implements Runnable {
    public boolean playing;
    public boolean stop = false;
    SourceDataLine line;

    @Override
    public void run() {
        //setup
        AudioDataFormat format = new Pcm16AudioDataFormat(2, 44100, StandardAudioDataFormats.COMMON_PCM_S16_BE.chunkSampleCount, true);
        AudioInputStream stream = AudioPlayerInputStream.createStream(MotorTunez.player, format, 10000L, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(stream.getFormat());
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        //run
        playing = true;
        byte[] buffer = new byte[StandardAudioDataFormats.COMMON_PCM_S16_BE.maximumChunkSize()];

        int chunkSize;
        try {
            while((chunkSize = stream.read(buffer)) > 0) {
                while (!playing) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.line.write(buffer, 0, chunkSize);
                if (stop) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.destroy();
    }

    private void destroy() {
        MotorTunez.player.destroy();
        this.line.drain();
        this.line.stop();
        this.line.close();
        stop = false;
        playing = false;
    }
}
