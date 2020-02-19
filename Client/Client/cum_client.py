import socket
import queue
import json
import threading

message_queue = queue.Queue()
send_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_host = 'localhost'
server_port = 55555


class ReceiveThread(threading.Thread):
    def __init__(self, host, port):
        threading.Thread.__init__(self)
        self.host = host
        self.port = port

    def run(self):
        receive_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        receive_socket.bind((self.host, self.port))
        while True:
            msg = (receive_socket.recvfrom(1024)[0])
            message_queue.put(msg.decode('utf-8'))


def main():
    rt = ReceiveThread('localhost', 7776)
    rt.start()

    # print("Login: ")
    login = 'ED'  # input()

    auth_msg = json.dumps({'type': 'auth', 'login': login, 'data': {'host': rt.host, 'port': rt.port}})
    send_socket.sendto(auth_msg.encode('utf-8'), (server_host, server_port))

    while True:
        server_msg = message_queue.get()
        if server_msg == 'auth':
            print("s ucc")


main()
