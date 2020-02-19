package cli.udp.messenger.server.dto;

import cli.udp.messenger.server.models.ChatMessage;

public class SendMessageRequest {
    private String receiver;
    private String type;
    private ChatMessage message;

    public String getReceiver() {
        return receiver;
    }

    public SendMessageRequest setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public String getType() {
        return type;
    }

    public SendMessageRequest setType(String type) {
        this.type = type;
        return this;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public SendMessageRequest setMessage(ChatMessage message) {
        this.message = message;
        return this;
    }
}
