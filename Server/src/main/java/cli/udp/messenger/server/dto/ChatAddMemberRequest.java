package cli.udp.messenger.server.dto;

public class ChatAddMemberRequest {
    private String chatName;
    private String nickname;

    public String getChatName() {
        return chatName;
    }

    public ChatAddMemberRequest setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public ChatAddMemberRequest setNickname(String nickName) {
        this.nickname = nickName;
        return this;
    }
}
