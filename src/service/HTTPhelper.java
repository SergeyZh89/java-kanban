package service;

import typeAdapters.DurationAdapter;
import typeAdapters.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPhelper {
    public static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(Duration .class, new DurationAdapter().nullSafe())
            .registerTypeAdapter(LocalDateTime .class, new LocalDateAdapter().nullSafe())
            .create();
}
