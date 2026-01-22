package ua.notion.musiclibrary.domain.repository.json.adapter;

import java.io.IOException;
import java.time.Duration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public Duration read(JsonReader in) throws IOException {
        if (in.peek() == null)
            return null;

        return Duration.ofNanos(in.nextLong());
    }

    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toNanos());
        }
    }
}
