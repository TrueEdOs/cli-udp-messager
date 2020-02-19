package cli.udp.messenger.server.models;

import java.util.ArrayList;
import java.util.List;

import static sun.swing.MenuItemLayoutHelper.max;

public class Chat {
    private String name;
    private List<ChatMessage> messages = new ArrayList<>();
    private List<String> members;

    public Chat(final String name) {
        this.name = name;
    }

    public void addMember(final String nickname) {

    }

    public void sendMessage(final String nickname, final String data, final long timestamp) {
        messages.add(new ChatMessage().setNickname(nickname).setData(data).setTimestamp(timestamp));
    }

    public List<ChatMessage> getLastMessages(int count) {
        List<ChatMessage> last_messages = new ArrayList<>();

        for (int i = max(0, messages.size() - count); i < messages.size(); ++i) {
            last_messages.add(messages.get(i));
        }

        return last_messages;
    }
}
