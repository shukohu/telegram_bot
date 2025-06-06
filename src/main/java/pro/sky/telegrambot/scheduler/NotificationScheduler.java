package pro.sky.telegrambot.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.configuration.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationScheduler {
    private final TelegramBot telegramBot;
    private final NotificationTaskRepository repository;

    public NotificationScheduler(TelegramBot telegramBot, NotificationTaskRepository repository) {
        this.telegramBot = telegramBot;
        this.repository = repository;
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendNotification() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = repository.findByNotificationDateTime(currentTime);

        tasks.forEach(notificationTask -> {
            SendMessage message = new SendMessage(notificationTask.getChatId(), "Напоминание: " + notificationTask.getMessage());
            telegramBot.execute(message);

            repository.delete(notificationTask);
        });
    }
}
