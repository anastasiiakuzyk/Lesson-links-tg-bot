package demo;

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
    ArrayList<String> idList = new ArrayList<String>();

    static final String Phil = "Філософія: ";
    static final String Logic = "Logic(лек): ";
    static final String LogicP = "Logic(прак): ";
    static final String Log = "Логіка(лек): ";
    static final String LogP = "Логіка(прак): ";
    static final String PsyConf = "Псих. конф(лек): ";
    static final String PsyConfP = "Псих. конф(прак): ";
    static final String SocPsy = "Соц. псих: ";
    static final String Test2 = "Тестування(лек, 2): ";
    static final String Test4 = "Тестування(лек, 4): ";
    static final String TestP = "Тестування(прак): ";
    static final String Engl = "Англ: ";
    static final String Arc = "Архітектура(лек): ";
    static final String ArcP = "Архітектура(прак): ";
    static final String Scrypt = "Скрипт. мови: ";

    static final String Mod = "Моделювання: ";
    static final String Alg = "Алгоритми: ";


    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch(message.getText()){
                case "/start@lessonsTI_92Bot", "/start":
                    if (this.idList.contains(message.getChatId().toString())) {
                        break;
                    } else {
                        this.idList.add(message.getChatId().toString());
                    }
                    try {
                        lessonsForWeek(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }

    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);

        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {

            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void scheduleMessageForNonStaticLinks(Message message, String time, String lesson, long period) throws Exception {
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
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(time);


        timer.schedule(task, date , period);
    }

    public void scheduleMessageForStaticLinks(Message message, String time, String lesson, long period) throws ParseException {
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
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(time);

        timer.schedule(task, date, period);
    }

    public Map<String, String > nameAndLink(){
        Map<String, String> map = new HashMap<>();

        map.put(Phil, "https://us04web.zoom.us/j/77987850964?pwd=UEMrY3ZUaUx6cjlIVkxIMXdUVG5QQT09");
        map.put(Logic, "https://us04web.zoom.us/j/9964541547?pwd=aXErY0V6blR6ZHhic0trRFl6WjJBUT09");
        map.put(LogicP, "https://us04web.zoom.us/j/3924836056?pwd=YVdUbkZkSW1relVheDV5bHhWRmF2QT09");
        map.put(Log, "https://us04web.zoom.us/j/77488021410?pwd=Ui9NYkdZbGYwdlg1NmlYT1M3YlNGUT09");
        map.put(LogP, "https://us04web.zoom.us/j/73898186111?pwd=SmZVdUVTRVA4YTJ1cnV2MjZ6S0E4Zz09");
        map.put(PsyConf, "https://zoom.us/j/9953120638?pwd=WGZsYUhPK2hxbUc4YVJmT0lhdysyZz09");
        map.put(PsyConfP, "https://us04web.zoom.us/j/4115352172?pwd=V0tGU3RabFBUM3N4TW9xYzJtRnV1UT09");
        map.put(SocPsy, "https://zoom.us/j/5175581158?pwd=UlhFY3lBOUUrNG9pclRVNndTNTZzQT09");
        map.put(Test2, "https://us04web.zoom.us/j/75813448807?pwd%3DajJ0cHpzaWFOVmQ1N05MNG82Nm5pZz09");
        map.put(Test4, "https://us04web.zoom.us/j/77847644953?pwd%3DMC95M2UvS1FKRm4yU0NsN1IrVVI0UT09");
        map.put(TestP, "https://us02web.zoom.us/j/5764945700?pwd=YldPV01HaHM3NWJPaWp3b1l5Nnpadz09");
        map.put(Engl, "https://us02web.zoom.us/j/86599861456?pwd=Wm9DWGVTOXNIWENhM2kvNEp6ZmpVZz09");
        map.put(Arc, "https://zoom.us/j/97881110502?pwd=ZDVMd24xRlpENkJ3OUhvWjBvWnNYQT09");
        map.put(ArcP, "https://zoom.us/j/96969891138?pwd=dTdjMTRaMFoyN0c1Mk41eGdTbytmUT09");
        map.put(Scrypt, "https://bbb.kpi.ua/b/cyt-c79-9px");

        return map;
    }

    public void lessonsForWeek(Message message) throws Exception {
        long oneWeek = 1000L*60L*60L*24L*7L;
        long twoWeeks = oneWeek*2L;
        scheduleMessageForStaticLinks(message,"08.03.2021 10:22", Test2, oneWeek);
        scheduleMessageForStaticLinks(message,"08.03.2021 12:17", Engl, oneWeek);
        scheduleMessageForStaticLinks(message,"15.03.2021 14:12", Test4, twoWeeks);

        scheduleMessageForStaticLinks(message,"02.03.2021 08:27", Phil, twoWeeks);
        scheduleMessageForNonStaticLinks(message,"02.03.2021 10:22", Mod, oneWeek);
        scheduleMessageForNonStaticLinks(message,"02.03.2021 12:17", Alg, oneWeek);

        scheduleMessageForStaticLinks(message,"03.03.2021 10:22", Arc, oneWeek);
        scheduleMessageForStaticLinks(message,"03.03.2021 12:17", TestP, oneWeek);

        scheduleMessageForNonStaticLinks(message,"04.03.2021 10:22", Mod, oneWeek);
        scheduleMessageForStaticLinks(message,"04.03.2021 12:17", Scrypt, oneWeek);

        scheduleMessageForStaticLinks(message,"05.03.2021 08:27", Logic, twoWeeks);
        scheduleMessageForStaticLinks(message,"05.03.2021 10:22", SocPsy, twoWeeks);
        scheduleMessageForStaticLinks(message,"05.03.2021 14:12", ArcP, oneWeek);

        scheduleMessageForStaticLinks(message,"06.03.2021 10:22", LogP, twoWeeks);
        scheduleMessageForStaticLinks(message,"06.03.2021 12:17", PsyConfP, twoWeeks);

        scheduleMessageForStaticLinks(message,"08.03.2021 14:12", TestP, twoWeeks);
        scheduleMessageForNonStaticLinks(message,"11.03.2021 08:27", Alg, twoWeeks);
        scheduleMessageForStaticLinks(message,"12.03.2021 08:27", LogicP, twoWeeks);

        scheduleMessageForStaticLinks(message,"13.03.2021 10:22", Log, twoWeeks);
        scheduleMessageForStaticLinks(message,"02.03.2021 00:03", PsyConf, twoWeeks);

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
