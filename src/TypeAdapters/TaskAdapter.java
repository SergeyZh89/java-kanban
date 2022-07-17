package TypeAdapters;

import Manager.Status;
import Manager.TaskTypes;
import Model.Task;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskAdapter extends TypeAdapter<Task> {
    @Override
    public void write(JsonWriter writer, Task task) throws IOException {
        writer.beginObject();
        writer.name("name");
        writer.value(task.getName());
        writer.name("decription");
        writer.value(task.getdecription());
        writer.name("status");
        writer.value(task.getStatus().toString());
        writer.name("id");
        writer.value(task.getId());
        writer.name("typeTask");
        writer.value(task.getTypeTask().toString());

        if (task.getDuration() != null) {
            writer.name("duration");
            writer.value(task.getDuration().toString());
            writer.name("startTime");
            writer.value(task.getStartTime().toString());
        }
        writer.endObject();
    }

    @Override
    public Task read(JsonReader reader) throws IOException {
            Task task = new Task();
            reader.beginObject();
            String fieldname = null;
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (token.equals(JsonToken.NAME)) {
                    fieldname = reader.nextName();
                }
                if ("name".equals(fieldname)) {
                    token = reader.peek();
                    task.setName(reader.nextString());
                }
                if ("decription".equals(fieldname)) {
                    token = reader.peek();
                    task.setdecription(reader.nextString());
                }
                if ("status".equals(fieldname)) {
                    token = reader.peek();
                    task.setStatus(Status.valueOf(reader.nextString()));
                }
                if ("id".equals(fieldname)) {
                    token = reader.peek();
                    task.setId(reader.nextInt());
                }
                if ("typeTask".equals(fieldname)) {
                    token = reader.peek();
                    task.setTypeTask(TaskTypes.valueOf(reader.nextString()));
                }
                if ("duration".equals(fieldname)) {
                    token = reader.peek();
                    task.setDuration(Duration.parse(reader.nextString()));
                }
                if ("startTime".equals(fieldname)) {
                    token = reader.peek();
                    task.setStartTime(LocalDateTime.parse((reader.nextString())));
                }
            }
            reader.endObject();
            return task;
    }
}

