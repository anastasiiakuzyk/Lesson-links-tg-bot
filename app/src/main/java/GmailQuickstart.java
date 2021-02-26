import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

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
//            System.out.println("Messages:");
            for (int i = 3; i >= 0; i--) {
                Message message = messages.get(i);
                Message message1 = service.users().messages().get(user, message.getId()).setFormat("RAW").execute();
                String codedMessage = new String(message1.decodeRaw());
                String sh = "Шушура";
                var shDecoded = URLEncoder.encode(sh).replace("%", "=");
                if (codedMessage.contains("Kuzmenko")) {
                    if (codedMessage.contains(linkType)) {
                        nameLink.put("Алгоритми: ", getLink(codedMessage));
                    }
                } else if (codedMessage.contains(shDecoded)) {
                    if (codedMessage.contains(linkType)) {
                        nameLink.put("Моделювання: ", getLink(codedMessage));
                    }
                }
            }
        }
        for (Map.Entry<String , String> entry: nameLink.entrySet()
             ) {
            System.out.println(entry.getKey() + entry.getValue());
        }
        return nameLink;
    }

}