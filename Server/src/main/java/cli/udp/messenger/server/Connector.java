package cli.udp.messenger.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Connector extends Thread{
    private DatagramSocket receiveSocket;
    private DatagramSocket sendSocket;
    private Queue<String> incomingMessages = new ConcurrentLinkedDeque<>();

    public Connector(final int port) throws SocketException {
        receiveSocket = new DatagramSocket(port);
        sendSocket = new DatagramSocket();
    }

    public void sendMessage(final InetAddress address, final int port, final String data) throws IOException {
        DatagramPacket dp = new DatagramPacket(data.getBytes() , data.getBytes().length , address, port);
        sendSocket.send(dp);
    }

    public boolean isAnyMessages() {
        return !incomingMessages.isEmpty();
    }

    public String getLastMessages() {
        return incomingMessages.poll();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[65536];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);


        while(true)
        {
            try {
                receiveSocket.receive(incoming);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = incoming.getData();
            String s = new String(data, 0, incoming.getLength());
        }
    }
}
