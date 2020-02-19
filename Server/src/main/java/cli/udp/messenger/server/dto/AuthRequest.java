package cli.udp.messenger.server.dto;

public class AuthRequest {
    private String host;
    private Integer port;

    public String getHost() {
        return host;
    }

    public AuthRequest setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public AuthRequest setPort(Integer port) {
        this.port = port;
        return this;
    }
}
