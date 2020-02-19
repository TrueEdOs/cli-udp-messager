package cli.udp.messenger.server.models;

public class ChatMessage {
    private String nickname;
    private String data;
    private long timestamp;

    public String getNickname() {
        return nickname;
    }

    public ChatMessage setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getData() {
        return data;
    }

    public ChatMessage setData(String data) {
        this.data = data;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ChatMessage setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
