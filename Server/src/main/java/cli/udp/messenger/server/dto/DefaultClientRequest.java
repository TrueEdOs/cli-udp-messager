package cli.udp.messenger.server.dto;

import cli.udp.messenger.server.helpers.RawJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DefaultClientRequest {
    private String login;
    private String type;
    @JsonDeserialize(using = RawJsonDeserializer.class)
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
