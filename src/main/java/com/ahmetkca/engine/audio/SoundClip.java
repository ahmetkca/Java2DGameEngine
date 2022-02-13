package com.ahmetkca.engine.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundClip {

    private Clip clip = null;
    private FloatControl gainControl;

    public SoundClip(String path) {
        try {
            InputStream audioSource = SoundClip.class.getResourceAsStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSource);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat audioFormat = audioInputStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    audioFormat.getSampleRate(),
                    16,
                    audioFormat.getChannels(),
                    audioFormat.getChannels() * 2,
                    audioFormat.getSampleRate(),
                    false);
            AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);

            clip = AudioSystem.getClip();
            clip.open(decodedAudioInputStream);
            clip.flush();

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null)
            return;

        stop();
        clip.setFramePosition(0);
        while (!clip.isRunning()) {
            clip.start();
        }
    }

    public void stop() {
        if (clip.isRunning())
            clip.stop();
    }

    public void close() {
        stop();
        clip.drain();
        clip.close();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        play();
    }

    public void setVolume(float volVal) {
        if (volVal > 1.0f)
            volVal = 1.0f;
        if (volVal < 0)
            volVal = 0;
        float range = gainControl.getMaximum() - gainControl.getMinimum();

        float newVol = (range * volVal) + gainControl.getMinimum();
        System.out.println("range = " + range + "   " + newVol);
        gainControl.setValue(newVol);

    }

    public boolean isRunning() {
        return clip.isRunning();
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public FloatControl getGainControl() {
        return gainControl;
    }

    public void setGainControl(FloatControl gainControl) {
        this.gainControl = gainControl;
    }
}
