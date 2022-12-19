package service.taskManager;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final int PORT_HTTP_TASK_SERVER = 8082;
    public static final String URL_TO_KV_SERVER = "http://localhost:8078";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT_KVSERVER = 8078;
    public static final DateTimeFormatter FORMATTER_WRITER = DateTimeFormatter.ofPattern("dd--MM--yyyy HH:mm");
    public static final DateTimeFormatter FORMATTER_READER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public final static String DATE_TIME_FORMAT_FILE_BACKED_TASK_MANAGER = "[dd.MM.yyyy]/[HH:mm]";

}
