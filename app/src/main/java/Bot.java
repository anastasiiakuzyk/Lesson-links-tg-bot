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

    ArrayList<String> idList = new ArrayList<>();

    static final String PhilP = "Філософія(прак): ";
    static final String Logic = "Logic(лек): ";
    static final String LogicP = "Logic(прак): ";
    static final String Log = "Логіка(лек): ";
    static final String LogP = "Логіка(прак): ";
    static final String PsyConf = "Псих. конф(лек): ";
    static final String PsyConfP = "Псих. конф(прак): ";
    static final String SocPsy = "Соц. псих: ";
    static final String SocPsyEn = "Soc. ps.: ";
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
            switch (message.getText()) {
                case "/start@lessonsTI_92Bot", "/start":
                    if (this.idList.contains(message.getChatId().toString())) {
                        break;
                    } else {
                        sendMsg(message, "Всем ку");
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
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void scheduleMessageForNonStaticLinks(Message message, String time, String lesson, long period) throws Exception {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map<String, String> map;
                String link = null;
                try {
                    map = GmailQuickstart.nameAndLinkFromGmailMessages(10);
                    link = map.get(lesson);
                    for (int i = 0; i < 15 && link == null; i++) {
                        Thread.sleep(1000 * 15);
                        map = GmailQuickstart.nameAndLinkFromGmailMessages(3);
                        link = map.get(lesson);
                    }
                    if (link == null) {
                        link = "@anastasiiakuzyk - дура, где ссылка!!";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendMsg(message, decode(lesson) + link);
            }
        };

        Timer timer = new Timer("Timer");
        Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(time);


        timer.schedule(task, date, period);
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

    public Map<String, String> nameAndLink() {
        Map<String, String> map = new HashMap<>();

        map.put(PhilP, "https://us04web.zoom.us/j/77987850964?pwd=UEMrY3ZUaUx6cjlIVkxIMXdUVG5QQT09");
        map.put(Logic, "http://meet.google.com/div-hxhw-nqt");
        map.put(LogicP, "https://us04web.zoom.us/j/3924836056?pwd=YVdUbkZkSW1relVheDV5bHhWRmF2QT09");
        map.put(Log, "https://us04web.zoom.us/j/77488021410?pwd=Ui9NYkdZbGYwdlg1NmlYT1M3YlNGUT09");
        map.put(LogP, "https://us04web.zoom.us/j/73898186111?pwd=SmZVdUVTRVA4YTJ1cnV2MjZ6S0E4Zz09");
        map.put(PsyConf, "https://zoom.us/j/9953120638?pwd=WGZsYUhPK2hxbUc4YVJmT0lhdysyZz09");
        map.put(PsyConfP, "https://us04web.zoom.us/j/4115352172?pwd=V0tGU3RabFBUM3N4TW9xYzJtRnV1UT09");
        map.put(SocPsy, "https://zoom.us/j/5175581158?pwd=UlhFY3lBOUUrNG9pclRVNndTNTZzQT09");
        map.put(SocPsyEn, "https://zoom.us/j/5175581158?pwd=UlhFY3lBOUUrNG9pclRVNndTNTZzQT09");
        map.put(Test2, "@anastasiiakuzyk - кинь ссылку");
        map.put(Test4, "@anastasiiakuzyk - кинь ссылку");
        map.put(TestP, "https://us02web.zoom.us/j/5764945700?pwd=YldPV01HaHM3NWJPaWp3b1l5Nnpadz09");
        map.put(Engl, "https://us02web.zoom.us/j/86599861456?pwd=Wm9DWGVTOXNIWENhM2kvNEp6ZmpVZz09");
        map.put(Arc, "https://zoom.us/j/97881110502?pwd=ZDVMd24xRlpENkJ3OUhvWjBvWnNYQT09");
        map.put(ArcP, "https://zoom.us/j/96969891138?pwd=dTdjMTRaMFoyN0c1Mk41eGdTbytmUT09");
        map.put(Scrypt, "https://bbb.kpi.ua/b/cyt-c79-9px");

        return map;
    }

    public void lessonsForWeek(Message message) throws Exception {
        long oneWeek = 1000L * 60L * 60L * 24L * 7L;
        long twoWeeks = oneWeek * 2L;
        scheduleMessageForStaticLinks(message, "05.04.2021 10:23", Test2, oneWeek);
        scheduleMessageForStaticLinks(message, "05.04.2021 12:18", Engl, oneWeek);
        scheduleMessageForStaticLinks(message, "12.04.2021 14:13", Test4, twoWeeks);

        scheduleMessageForStaticLinks(message, "06.04.2021 08:28", PhilP, twoWeeks);
        scheduleMessageForNonStaticLinks(message, "06.04.2021 10:23", Mod, oneWeek);
        scheduleMessageForNonStaticLinks(message, "06.04.2021 12:19", Alg, oneWeek);

        scheduleMessageForStaticLinks(message,"07.04.2021 10:23", Arc, oneWeek);
        scheduleMessageForStaticLinks(message,"07.04.2021 12:18", TestP, oneWeek);

        scheduleMessageForNonStaticLinks(message,"01.04.2021 10:23", Mod, oneWeek);
        scheduleMessageForStaticLinks(message,"01.04.2021 12:18", Scrypt, oneWeek);

        scheduleMessageForStaticLinks(message,"02.04.2021 08:28", Logic, twoWeeks);
        scheduleMessageForStaticLinks(message,"02.04.2021 10:23", SocPsyEn, oneWeek);
        scheduleMessageForStaticLinks(message,"02.04.2021 14:13", ArcP, oneWeek);

        scheduleMessageForStaticLinks(message,"03.04.2021 10:23", LogP, twoWeeks);
        scheduleMessageForStaticLinks(message,"03.04.2021 12:18", PsyConfP, twoWeeks);

        scheduleMessageForStaticLinks(message,"05.04.2021 16:08", TestP, twoWeeks);
        scheduleMessageForNonStaticLinks(message,"08.04.2021 08:29", Alg, twoWeeks);
        scheduleMessageForStaticLinks(message,"09.04.2021 08:28", LogicP, twoWeeks);

        scheduleMessageForStaticLinks(message,"10.04.2021 10:23", Log, twoWeeks);
        scheduleMessageForStaticLinks(message,"10.04.2021 12:18", PsyConf, twoWeeks);

    }

    public static String decode(String text) {
        byte[] ptext = text.getBytes();
        return new String(ptext, StandardCharsets.UTF_8);
    }

    public String getBotUsername() {
        return "lessonsTI_92Bot";
    }

    public String getBotToken() {
        return "1659504305:AAFHHCXThjBNRnjUM5Zfl5kR7xt8WxTNdkQ";
    }
}
