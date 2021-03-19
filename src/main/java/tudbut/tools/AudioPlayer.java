package tudbut.tools;

import de.tudbut.io.StreamReader;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer {
    
    public static AudioTrackPlayer startPlaying(InputStream stream) throws IOException {
        AudioTrackPlayer r = new AudioTrackPlayer();
        
        try {
            AudioInputStream as = AudioSystem.getAudioInputStream(new ByteArrayInputStream(new StreamReader(stream).readAllAsBytes()));
            DataLine.Info info = new DataLine.Info(Clip.class, as.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(as);
            clip.start();
            r.player = clip;
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new AudioException(e);
        }
        
        return r;
    }
    
    public static class AudioException extends IOException {
    
        public AudioException(Throwable cause) {
            super(cause);
        }
    }
    
    public static class AudioTrackPlayer {
        private Clip player;
        
        public void setVolume(float vol) {
            ((FloatControl) player.getControl(FloatControl.Type.VOLUME)).setValue(vol);
        }
        public void setMS(int ms) {
            player.setFramePosition(ms);
        }
        public boolean isPlaying() {
            return player.isActive();
        }
        public boolean isPausedOrPlaying() {
            return player.isOpen();
        }
        public void start() {
            if(!isPausedOrPlaying())
                setMS(0);
            player.start();
        }
        public void pause() {
            player.stop();
        }
        public void stop() {
            player.stop();
            player.close();
        }
    }
}
