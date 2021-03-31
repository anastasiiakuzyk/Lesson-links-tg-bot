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

    static final String linkMeet = "https://meet.google.com/";

    static String getLink(String message, String linkType) {
        int start = message.indexOf(linkType);
        int end = start + linkType.length();
        String link = "";
        String test = linkType;
        for (int j = end; j < message.length(); j++) {

            test += message.charAt(j);
            if (test.matches("https://meet.google.com/[a-z-]*")) {
                link += message.charAt(j);

            } else break;
        }
        return linkType + link;
    }

    static Map<String, String> nameAndLinkFromGmailMessages(int iterNum) throws Exception {
        Gmail service = Connection.service();
        Map<String, String> nameLink = new HashMap<>();

        String user = "me";
        ListMessagesResponse listMessagesResponse = service.users().messages().list(user).execute();
        List<Message> messages = listMessagesResponse.getMessages();
        if (messages.isEmpty()) {
            System.out.println("No messages");
        } else {
            for (int i = iterNum; i >= 0; i--) {
                Message message = messages.get(i);
                Message message1 = service.users().messages().get(user, message.getId()).setFormat("RAW").execute();
                String codedMessage = new String(message1.decodeRaw(), StandardCharsets.UTF_8);
                String sh = "Шушура";
                String kz = "Kuzmenko";
                var kzDecoded = URLEncoder.encode(kz).replace("%", "=");
                var shDecoded = URLEncoder.encode(sh).replace("%", "=");
                var splited = codedMessage.split("\r\n\r\n");

                byte[] codedAfterBase64Bytes = new byte[0];
                try {
                    codedAfterBase64Bytes = Base64.getMimeDecoder().decode(splited[splited.length - 1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String codedAfterBase64 = new String(codedAfterBase64Bytes, StandardCharsets.UTF_8);
                if (codedMessage.contains(kz)) {
                    if (codedMessage.contains(linkMeet)) {
                        nameLink.put("Алгоритми: ", getLink(codedMessage, linkMeet));
                    }
                } else if (codedMessage.contains(kzDecoded)) {
                    if (codedMessage.contains(linkMeet)) {
                        nameLink.put("Алгоритми: ", getLink(codedMessage, linkMeet));
                    }
                } else if (codedAfterBase64.contains(kz)) {
                    if (codedAfterBase64.contains(linkMeet)) {
                        nameLink.put("Алгоритми: ", getLink(codedMessage, linkMeet));
                    }
                } else if (codedMessage.contains(sh)) {
                    if (codedMessage.contains(linkMeet)) {
                        nameLink.put("Моделювання: ", getLink(codedMessage, linkMeet));
                    }
                } else if (codedMessage.contains(shDecoded)) {
                    if (codedMessage.contains(linkMeet)) {
                        nameLink.put("Моделювання: ", getLink(codedMessage, linkMeet));
                    }
                } else if (codedAfterBase64.contains(sh)) {
                    if (codedAfterBase64.contains(linkMeet)) {
                        nameLink.put("Моделювання: ", getLink(codedAfterBase64, linkMeet));
                    }
                }
            }
        }
        for (Map.Entry<String, String> entry : nameLink.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }
        return nameLink;
    }

}