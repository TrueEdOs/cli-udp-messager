package cli.udp.messenger.server;

import cli.udp.messenger.server.dto.*;
import cli.udp.messenger.server.models.Chat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CUMServer {
    private static final int MESSAGES_PER_CYCLE = 100;

    private static final Logger log = LogManager.getLogger(CUMServer.class);
    private static Connector connector;
    private static Map<String, Chat> chatMap = new HashMap<>();
    private static Map<String, String> hostMap = new HashMap<>();
    private static Map<String, Integer> portMap = new HashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

    public static void processMessage(final String messages) throws Exception {
        DefaultClientRequest defaultClientRequest = objectMapper.readValue(messages, DefaultClientRequest.class);

        String login = defaultClientRequest.getLogin();
        String type = defaultClientRequest.getType();
        String data = defaultClientRequest.getData();

        if (type == null) {
            throw new Exception("Message hasn't type");
        }

        if (login == null) {
            throw new Exception("Message hasn't login");
        }

        if (type.equals("auth")) {
            AuthRequest request = objectMapper.readValue(data, AuthRequest.class);
            if (request.getHost() == null || request.getPort() == null) {
                throw new Exception("Cannot establish host");
            }
            hostMap.put(login, request.getHost());
            portMap.put(login, request.getPort());
            connector.sendMessage(InetAddress.getByName(request.getHost()), request.getPort(), "auth");
            return;
        }

        if (type.equals("addMember")) {
            ChatAddMemberRequest request = objectMapper.readValue(data, ChatAddMemberRequest.class);
            if (request.getChatName() == null || request.getNickName() == null) {
                throw new Exception("Cannot add member");
            }

            Chat chat = chatMap.get(request.getChatName());

            if (chat == null) {
                throw new Exception("Cannot add member. There are no such chat");
            }

            chat.addMember(request.getNickName());
            return;
        }

        if (type.equals("createChat")) {
            ChatCreationRequest request = objectMapper.readValue(data, ChatCreationRequest.class);

            if (request.getName() == null) {
                throw new Exception("Cannot create chat");
            }

            if (chatMap.containsKey(request.getName())) {
                throw new Exception("Cannot create chat. Already exist");
            }

            chatMap.put(request.getName(), new Chat(request.getName()));
            return;
        }

        if (type.equals("sendMessage")) {
            SendMessageRequest request = objectMapper.readValue(data, SendMessageRequest.class);

            if (request.getMessage() == null || request.getReceiver() == null || request.getType() == null) {
                throw new Exception("Cannot send message");
            }

            if (request.getType().equals("user")) {
                if (!hostMap.containsKey("user")) {
                    throw new Exception("Cannot send message. There are no such user");
                }

                String user = request.getReceiver();
                connector.sendMessage(InetAddress.getByName(hostMap.get(user)), portMap.get(user), objectMapper.writeValueAsString(request.getMessage()));
            }
        }


        throw new Exception("Can't recognize message type");
    }

    public static void main(String[] args) throws SocketException {
        //objectMapper
        connector = new Connector(55555);
        connector.start();

        log.info("Chat server started");

        while (true) {
            if (connector.hasMessages()) {
                String message = connector.getLastMessage();
                try {
                    processMessage(message);
                } catch (Exception e) {
                    log.error("Failed to process message. Message: " + message);
                }
            }
        }
    }
}
