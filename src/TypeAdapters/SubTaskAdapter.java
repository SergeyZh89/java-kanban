package TypeAdapters;

import Manager.Status;
import Manager.TaskTypes;
import Model.SubTask;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskAdapter extends TypeAdapter<SubTask> {
    @Override
    public void write(JsonWriter writer, SubTask subTask) throws IOException {
        writer.beginObject();
        writer.name("name");
        writer.value(subTask.getName());
        writer.name("decription");
        writer.value(subTask.getdecription());
        writer.name("status");
        writer.value(subTask.getStatus().toString());
        writer.name("id");
        writer.value(subTask.getId());
        writer.name("typeSubTask");
        writer.value(subTask.getTypeSubTask().toString());
        writer.name("epicIds");
        writer.value(subTask.getEpicIds());

        if (subTask.getDuration() != null) {
            writer.name("duration");
            writer.value(subTask.getDuration().toString());
            writer.name("startTime");
            writer.value(subTask.getStartTime().toString());
        }
        writer.endObject();
    }

    @Override
    public SubTask read(JsonReader reader) throws IOException {
        SubTask subTask = new SubTask();
        reader.beginObject();
        String fieldname = null;
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldname = reader.nextName();
            }
            if ("name".equals(fieldname)) {
                token = reader.peek();
                subTask.setName(reader.nextString());
            }
            if ("decription".equals(fieldname)) {
                token = reader.peek();
                subTask.setdecription(reader.nextString());
            }
            if ("status".equals(fieldname)) {
                token = reader.peek();
                subTask.setStatus(Status.valueOf(reader.nextString()));
            }
            if ("id".equals(fieldname)) {
                token = reader.peek();
                subTask.setId(reader.nextInt());
            }
            if ("typeSubTask".equals(fieldname)) {
                token = reader.peek();
                subTask.setTypeSubTask(TaskTypes.valueOf(reader.nextString()));
            }
            if ("duration".equals(fieldname)) {
                token = reader.peek();
                subTask.setDuration(Duration.parse(reader.nextString()));
            }
            if ("startTime".equals(fieldname)) {
                token = reader.peek();
                subTask.setStartTime(LocalDateTime.parse(reader.nextString()));
            }
            if ("epicIds".equals(fieldname)) {
                token = reader.peek();
                subTask.setEpicIds(reader.nextInt());
            }
        }
        reader.endObject();
        return subTask;
    }
}
