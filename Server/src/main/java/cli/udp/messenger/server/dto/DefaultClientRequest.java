package cli.udp.messenger.server.dto;

public class DefaultClientRequest {
    private String login;
    private String type;
    private String data;

    public String getLogin() {
        return login;
    }

    public DefaultClientRequest setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getType() {
        return type;
    }

    public DefaultClientRequest setType(String type) {
        this.type = type;
        return this;
    }

    public String getData() {
        return data;
    }

    public DefaultClientRequest setData(String data) {
        this.data = data;
        return this;
    }
}
