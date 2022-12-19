package api;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

import static service.taskManager.Constants.FORMATTER_READER;
import static service.taskManager.Constants.FORMATTER_WRITER;


public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {


    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.format(FORMATTER_WRITER));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), FORMATTER_READER);
    }
}
