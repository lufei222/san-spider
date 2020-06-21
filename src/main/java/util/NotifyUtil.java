package util;

import demo.SMZDMSpider;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class NotifyUtil {
    /**
     * 提醒声音通知
     */
    public static void outletsNotify() {
        try {
            InputStream musicStream = SMZDMSpider.class.getResourceAsStream("../car.wav");
            AudioStream as = new AudioStream(musicStream);
            AudioPlayer.player.start(as);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        outletsNotify();
    }
}
