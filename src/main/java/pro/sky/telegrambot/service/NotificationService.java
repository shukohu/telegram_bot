package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.configuration.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {
    private final NotificationTaskRepository repository;
    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)}");

    public NotificationService(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    public void parseAndSave(Long chatId, String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            String dateTimeStr = matcher.group(1);
            String reminderText = matcher.group(3);

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            NotificationTask task = new NotificationTask();
            task.setChatId(chatId);
            task.setMessage(reminderText);
            task.setDateTime(dateTime);

        }
    }
}
