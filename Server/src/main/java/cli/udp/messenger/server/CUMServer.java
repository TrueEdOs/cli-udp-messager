package cli.udp.messenger.server;

import cli.udp.messenger.server.models.Chat;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class CUMServer {
    private static Connector connector;
    private static Map<String, Chat> chatMap = new HashMap<>();
    private static Map<String, String> hostMap = new HashMap<>();
    private static Map<String, Integer> portMap = new HashMap<>();

    public static void main(String[] args) throws SocketException {
        connector = new Connector(55555);


    }
}
