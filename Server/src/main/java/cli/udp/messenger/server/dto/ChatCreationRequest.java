package cli.udp.messenger.server.dto;

public class ChatCreationRequest {
    private String name;

    public String getName() {
        return name;
    }

    public ChatCreationRequest setName(String name) {
        this.name = name;
        return this;
    }
}
