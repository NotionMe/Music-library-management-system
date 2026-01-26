package ua.notion.musiclibrary.service.impl;

import ua.notion.musiclibrary.service.contract.AudioPlayerService;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

public class AudioPlayerServiceImpl implements AudioPlayerService {

    private static AudioPlayerServiceImpl instance;
    private final AudioPlayerComponent audioPlayerComponent;

    private AudioPlayerServiceImpl() {
        this.audioPlayerComponent = new AudioPlayerComponent();
    }

    public static synchronized AudioPlayerServiceImpl getInstance() {
        if (instance == null) {
            instance = new AudioPlayerServiceImpl();
        }
        return instance;
    }

    @Override
    public void play(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        audioPlayerComponent.mediaPlayer().media().play(filePath);
    }

    @Override
    public void pause() {
        audioPlayerComponent.mediaPlayer().controls().pause();
    }

    @Override
    public void resume() {
        audioPlayerComponent.mediaPlayer().controls().play();
    }

    @Override
    public void stop() {
        audioPlayerComponent.mediaPlayer().controls().stop();
    }

    @Override
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }
        audioPlayerComponent.mediaPlayer().audio().setVolume(volume);
    }

    @Override
    public int getVolume() {
        return audioPlayerComponent.mediaPlayer().audio().volume();
    }

    @Override
    public void seek(long timeMillis) {
        if (timeMillis < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        audioPlayerComponent.mediaPlayer().controls().setTime(timeMillis);
    }

    @Override
    public long getCurrentTime() {
        return audioPlayerComponent.mediaPlayer().status().time();
    }

    @Override
    public long getDuration() {
        return audioPlayerComponent.mediaPlayer().status().length();
    }

    @Override
    public boolean isPlaying() {
        return audioPlayerComponent.mediaPlayer().status().isPlaying();
    }

    @Override
    public void release() {
        audioPlayerComponent.release();
    }
}
