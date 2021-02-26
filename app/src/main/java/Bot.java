import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
    static final String Mod = "Моделювання: ";
    static final String Alg = "Алгоритми: ";
    static final String Test2 = "Тестування(лек, 2): ";
    static final String Test4 = "Тестування(лек, 4): ";
    static final String TestP = "Тестування(прак): ";
    static final String Engl = "Англ: ";
    static final String Arc = "Архітектура(лек): ";
    static final String ArcP = "Архітектура(прак): ";
    static final String LogP = "Logic(прак): ";

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            try {
                scheduleMessageForStaticLinks(message, "26.02.2021 14:13", ArcP);
                lessonsForWeek(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch(message.getText()){
                case "/start@lessonsTI-92Bot", "/start":
                    sendMsg(message, decode("start"));
                default:
                    sendMsg(message, decode("Вы подписались на повторную отправку сообщений.."));
            }
        }

    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);

        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {

            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void scheduleMessageForNonStaticLinks(Message message, String time, String lesson) throws Exception {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map<String, String> map = null;
                try {
                    map = GmailQuickstart.nameAndLinkFromGmailMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String link = map.get(lesson);
                sendMsg(message, decode(lesson)+link);
            }
        };

        Timer timer = new Timer("Timer");
        long period = 1000L*60L*60L*24L*7L;
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(time);


        timer.schedule(task, date , period);
    }

    public void scheduleMessageForStaticLinks(Message message, String time, String lesson) throws ParseException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map<String, String> map = null;
                try {
                    map = nameAndLink();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String link = map.get(lesson);
                sendMsg(message, decode(lesson) + link);
            }
        };
        Timer timer = new Timer("Timer");
        long period = 1000L * 60L * 60L * 24L * 7L;
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(time);


        timer.schedule(task, date, period);
    }

    public Map<String, String > nameAndLink(){
        Map<String, String> map = new HashMap<>();
        map.put(Test2, "https://us04web.zoom.us/j/75813448807?pwd%3DajJ0cHpzaWFOVmQ1N05MNG82Nm5pZz09");
        map.put(Test4, "https://us04web.zoom.us/j/77847644953?pwd%3DMC95M2UvS1FKRm4yU0NsN1IrVVI0UT09");
        map.put(TestP, "https://us02web.zoom.us/j/5764945700?pwd=YldPV01HaHM3NWJPaWp3b1l5Nnpadz09");
        map.put(Engl, "https://us02web.zoom.us/j/86599861456?pwd=Wm9DWGVTOXNIWENhM2kvNEp6ZmpVZz09");
        map.put(Arc, "https://zoom.us/j/97881110502?pwd=ZDVMd24xRlpENkJ3OUhvWjBvWnNYQT09");
        map.put(ArcP, "https://zoom.us/j/96969891138?pwd=dTdjMTRaMFoyN0c1Mk41eGdTbytmUT09");
        map.put(LogP, "https://us04web.zoom.us/j/3924836056?pwd=YVdUbkZkSW1relVheDV5bHhWRmF2QT09");
        return map;
    }

    public void lessonsForWeek(Message message) throws ParseException {
        scheduleMessageForStaticLinks(message, "01.03.2021 10:22", Test2);
        scheduleMessageForStaticLinks(message, "01.03.2021 12:17", Engl);
        scheduleMessageForStaticLinks(message, "01.03.2021 14:12", Test4);
//        scheduleMessageForNonStaticLinks(message, "02.03.2021 10:22", Mod);
//        scheduleMessageForNonStaticLinks(message, "02.03.2021 12:19", Alg);
        scheduleMessageForStaticLinks(message, "03.03.2021 10:22", Arc);
        scheduleMessageForStaticLinks(message, "03.03.2021 12:17", TestP);
//        scheduleMessageForNonStaticLinks(message, "04.03.2021 10:22", Mod);
        scheduleMessageForStaticLinks(message, "05.03.2021 14:12", ArcP);
    }

    public static String decode(String text){
        byte[] ptext = text.getBytes();
        return new String(ptext, StandardCharsets.UTF_8);
    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

//        keyboardFirstRow.add(new KeyboardButton("alg"));
//        keyboardFirstRow.add(new KeyboardButton("mod"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "lessonsTI_92Bot";
    }

    public String getBotToken() {
        return "1659504305:AAFHHCXThjBNRnjUM5Zfl5kR7xt8WxTNdkQ";
    }
}
