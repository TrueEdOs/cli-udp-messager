package cli.udp.messenger.server.models;

import java.util.ArrayList;
import java.util.List;

import static sun.swing.MenuItemLayoutHelper.max;

public class Chat {
    private String name;
    private List<String> members = new ArrayList<>();

    public Chat(final String name) {
        this.name = name;
    }

    public Chat addMember(final String nickname) {
        members.add(nickname);
        return this;
    }

    public List<String> getMembers() {
        return members;
    }
}
