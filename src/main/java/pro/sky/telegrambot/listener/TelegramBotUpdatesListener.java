package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private TelegramBot telegramBot;
    private NotificationService notificationService;

    @Autowired
    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        logger.info("Telegram bot listener started");
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null && update.message().text() != null) {
                long chatId = update.message().chat().id();
                String messageText = update.message().text();

                if ("/start".equals(messageText)) {
                    String welcomeMessage = "Привет! Это бот для напоминаний!" + "Отправь мне напоминание в формате: " + "дд.мм.гг чч.мм Текст напоминания";
                    SendMessage message = new SendMessage(chatId, welcomeMessage);
                    telegramBot.execute(message);
                }else {
                    notificationService.parseAndSave(chatId, messageText);
                    SendMessage response = new SendMessage(chatId, "Напоминание обновлено!");
                    telegramBot.execute(response);

                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


}
