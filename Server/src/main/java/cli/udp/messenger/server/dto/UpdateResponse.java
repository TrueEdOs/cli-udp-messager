package cli.udp.messenger.server.dto;

import cli.udp.messenger.server.models.ChatMessage;

public class UpdateResponse {
    private ChatMessage message;

    public ChatMessage getMessage() {
        return message;
    }

    public UpdateResponse setMessage(ChatMessage message) {
        this.message = message;
        return this;
    }
}
