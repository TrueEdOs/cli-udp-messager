package cli.udp.messenger.server.dto;

public class ChatAddMemberRequest {
    private String chatName;
    private String nickName;

    public String getChatName() {
        return chatName;
    }

    public ChatAddMemberRequest setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public ChatAddMemberRequest setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
