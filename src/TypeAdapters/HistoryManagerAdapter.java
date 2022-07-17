package TypeAdapters;

import Manager.*;
import Model.Epic;
import Model.SubTask;
import Model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryManagerAdapter extends TypeAdapter<HistoryManager> {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .create();

    @Override
    public void write(JsonWriter writer, HistoryManager historyManager) throws IOException {
        writer.beginArray();
        List<Task> history = historyManager.getHistory();
        for (Task task : history) {
            if (task.getTypeTask().equals("TASK")) {
                gson.toJson(task);
            }
            if (task.getTypeTask().equals("EPIC")) {
                gson.toJson(task);
            }
            if (task.getTypeTask().equals("SUBTASK")) {
                gson.toJson(task);
            }
        }
        writer.endArray();
    }

    @Override
    public HistoryManager read(JsonReader reader) throws IOException {
        reader.beginArray();
        String fieldName = null;
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        while (reader.hasNext()) {
            reader.beginObject();
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = reader.nextName();
            }
            if (token.equals("Task")) {
                historyManager.add(readTask(reader));
            }
            if (token.equals("Epic")) {
                historyManager.add(readEpic(reader));
            }
            if (token.equals("SubTask")) {
                historyManager.add(readSubTask(reader));
            }
        }
        reader.endArray();
        return historyManager;
    }

    public Task readTask(JsonReader reader) throws IOException {
        String fieldname = null;
        Task task = new Task();
        reader.beginObject();
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

    public Epic readEpic(JsonReader reader) throws IOException {
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
            if ("subtaskId".equals(fieldname)) {
                reader.beginArray();
                token = reader.peek();
                ArrayList<Integer> list = new ArrayList<>();
                while (reader.hasNext()) {
                    int id = Integer.parseInt(reader.nextName());
                    list.add(id);
                }
                epic.setSubtasksId(list);
                reader.endArray();
            }
        }
        reader.endObject();
        return epic;
    }

    public SubTask readSubTask(JsonReader reader) throws IOException {
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
                subTask.setStartTime(LocalDateTime.parse((reader.nextString())));
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
