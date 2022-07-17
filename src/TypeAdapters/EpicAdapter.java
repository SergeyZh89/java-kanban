package TypeAdapters;

import Manager.Status;
import Manager.TaskTypes;
import Model.Epic;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter writer, Epic epic) throws IOException {
        writer.beginObject();
        writer.name("name");
        writer.value(epic.getName());
        writer.name("decription");
        writer.value(epic.getdecription());
        writer.name("status");
        writer.value(epic.getStatus().toString());
        writer.name("id");
        writer.value(epic.getId());
        writer.name("typeEpic");
        writer.value(epic.getTypeEpic().toString());
        writer.name("subtasksId");
        writer.jsonValue(epic.getSubtasksId().toString());

        if (epic.getDuration() != null) {
            writer.name("duration");
            writer.value(epic.getDuration().toString());
            writer.name("startTime");
            writer.value(epic.getStartTime().toString());
            writer.name("endTime");
            writer.value(epic.getEndTime().toString());
        }
        writer.endObject();
    }

    @Override
    public Epic read(JsonReader reader) throws IOException {
        Epic epic = new Epic();
        reader.beginObject();
        String fieldname = null;
        while (reader.hasNext()) {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldname = reader.nextName();
            }
            if ("name".equals(fieldname)) {
                token = reader.peek();
                epic.setName(reader.nextString());
            }
            if ("decription".equals(fieldname)) {
                token = reader.peek();
                epic.setdecription(reader.nextString());
            }
            if ("status".equals(fieldname)) {
                token = reader.peek();
                epic.setStatus(Status.valueOf(reader.nextString()));
            }
            if ("id".equals(fieldname)) {
                token = reader.peek();
                epic.setId(reader.nextInt());
            }
            if ("typeEpic".equals(fieldname)) {
                token = reader.peek();
                epic.setTypeEpic(TaskTypes.valueOf(reader.nextString()));
            }
            if ("duration".equals(fieldname)) {
                token = reader.peek();
                epic.setDuration(Duration.parse(reader.nextString()));
            }
            if ("startTime".equals(fieldname)) {
                token = reader.peek();
                epic.setStartTime(LocalDateTime.parse((reader.nextString())));
            }
            if ("endTime".equals(fieldname)) {
                token = reader.peek();
                epic.setEndTime(LocalDateTime.parse((reader.nextString())));
            }
            if ("subtasksId".equals(fieldname)) {
                ArrayList<Integer> list = new ArrayList<>();
                reader.beginArray();
                while (reader.hasNext()){
                    list.add(reader.nextInt());
                }
                reader.endArray();
                epic.setSubtasksId(list);
            }
        }
        reader.endObject();
        return epic;
    }
}
