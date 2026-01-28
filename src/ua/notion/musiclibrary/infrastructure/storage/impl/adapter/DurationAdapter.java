package ua.notion.musiclibrary.infrastructure.storage.impl.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import static com.google.gson.stream.JsonToken.NULL;
import static com.google.gson.stream.JsonToken.BEGIN_OBJECT;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.toString());
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        if (in.peek() == NULL) {
            in.nextNull();
            return null;
        }
        if (in.peek() == BEGIN_OBJECT) {
            long seconds = 0;
            int nanos = 0;
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if (name.equals("seconds")) {
                    seconds = in.nextLong();
                } else if (name.equals("nanos")) {
                    nanos = in.nextInt();
                } else {
                    in.skipValue();
                }
            }
            in.endObject();
            return Duration.ofSeconds(seconds, nanos);
        }
        return Duration.parse(in.nextString());
    }
}
