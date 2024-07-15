package com.example.demo.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class MyTelegramBot extends TelegramLongPollingBot {

    public MyTelegramBot() {
        log.info("MyTelegramBot instance created");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Nhấn vào nút bên dưới để mở ứng dụng web:");

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText("Mở Mini App");
            inlineKeyboardButton.setWebApp(new WebAppInfo("https://testing-nine-gamma-69.vercel.app/"));

            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);

            markupInline.setKeyboard(rowsInline);
            message.setReplyMarkup(markupInline);


            try {
                execute(message); // Gửi tin nhắn
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "HunterXIshi_bot";
    }

    @Override
    public String getBotToken() {
        return "7490154438:AAFWq8HsPVepjyeUa22_qJywRAa6-EKBR-A";
    }
}
