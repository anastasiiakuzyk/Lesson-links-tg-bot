import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLEncoder;


public class GmailQuickstart {

    static final String linkType = "https://meet.google.com/";

    public static void main(String... args) throws Exception {
        System.out.println(nameAndLinkFromGmailMessages());

    }

    static String getLink(String message){
        int start = message.indexOf(linkType);
        int end = start+linkType.length();
        String link = "";
        String test = linkType;
        for (int j = end; j < message.length(); j++) {
            test += message.charAt(j);
            if(test.matches("https://meet.google.com/[a-z-]*")) {
                link += message.charAt(j);
            } else break;
        }
        return linkType+link;
    }

    static Map<String, String> nameAndLinkFromGmailMessages() throws Exception{
        Gmail service = Connection.service();
        Map<String, String > nameLink = new HashMap<>();

        String user = "me";
        ListMessagesResponse listMessagesResponse = service.users().messages().list(user).execute();
        List<Message> messages = listMessagesResponse.getMessages();
        if (messages.isEmpty()) {
            System.out.println("No messages");
        } else {
            for (int i = 10; i >= 0; i--) {
                Message message = messages.get(i);
                Message message1 = service.users().messages().get(user, message.getId()).setFormat("RAW").execute();
                String codedMessage = new String(message1.decodeRaw(), StandardCharsets.UTF_8);
                String sh = "Шушура";
                var shDecoded = URLEncoder.encode(sh).replace("%", "=");
                var splited = codedMessage.split("\r\n\r\n");
                var codedAfterBase64Bytes = Base64.getMimeDecoder().decode(splited[splited.length - 1]);
                String codedAfterBase64 = new String(codedAfterBase64Bytes, StandardCharsets.UTF_8);
                if (codedMessage.contains("Kuzmenko")) {
                    if (codedMessage.contains(linkType)) {
                        nameLink.put("Алгоритми: ", getLink(codedMessage));
                    }
                } else if (codedMessage.contains(shDecoded)) {
                    if (codedMessage.contains(linkType)) {
                        nameLink.put("Моделювання: ", getLink(codedMessage));
                    }
                } else if (codedAfterBase64.contains(sh)) {
                    if (codedAfterBase64.contains(linkType)) {
                        nameLink.put("Моделювання: ", getLink(codedAfterBase64));
                    }
                }
            }
        }
        for (Map.Entry<String , String> entry: nameLink.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }
        return nameLink;
    }

}