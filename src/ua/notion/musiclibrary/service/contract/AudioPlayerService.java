package ua.notion.musiclibrary.service.contract;

public interface AudioPlayerService {

    void play(String filePath);

    void pause();

    void resume();

    void stop();

    void setVolume(int volume);

    int getVolume();

    void seek(long timeMillis);

    long getCurrentTime();

    long getDuration();

    boolean isPlaying();

    void release();
}
