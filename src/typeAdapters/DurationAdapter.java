package typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration time) throws IOException {
        if (time != null) {
            out.value(time.toString());
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
       return Duration.parse(in.nextString());
    }
}
