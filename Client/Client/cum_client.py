import socket
import queue
import json
import threading
from tkinter import *
from tkinter import scrolledtext

message_queue = queue.Queue()
send_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_host = 'localhost'
server_port = 55555
chat_dict = {}
login = ""


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


rt = ReceiveThread('localhost', 7776)
rt.start()


def auth_rq(nickname):
    auth_msg = json.dumps({'type': 'auth', 'login': nickname, 'data': {'host': rt.host, 'port': rt.port}})
    send_socket.sendto(auth_msg.encode('utf-8'), (server_host, server_port))
    global login
    login = nickname


class ComradeUI:
    def __init__(self, window):
        self.messages = scrolledtext.ScrolledText(window, width=80, height=40)
        self.messages.grid(column=0, row=0, columnspan=2, rowspan=10)

        self.send_entry = Entry(window, width=60)
        self.send_entry.grid(column=0, row=10)
        self.send_button = Button(window, text="SEND", bg="black", fg="red", width=20)
        self.send_button.grid(column=1, row=10)

        self.login_entry = Entry(window, width=20)
        self.login_entry.grid(column=2, row=0)
        self.login_button = Button(window, text="LOGIN", bg="orange", fg="black", width=20, command=self.login)
        self.login_button.grid(column=2, row=1)
        self.chat_entry = Entry(window, width=20)
        self.chat_entry.grid(column=2, row=2)
        self.chat_button = Button(window, text="ADD CHAT", bg="orange", fg="black", width=20)
        self.chat_button.grid(column=2, row=3)

        self.chat_list = Listbox(window)
        self.chat_list.grid(column=2, row=4, selectmode=SINGLE)
        self.chat_list.bind('<<ListboxSelect>>', self.update)

    def add_chat(self, chat_name):
        self.chat_list.insert(END, chat_name)

    def login(self):
        auth_rq(self.login_entry.get())

    def update(self):
        slctd = self.chat_list.curselection()
        if len(slctd) == 0:
            return
        chat = self.chat_list.get(slctd[0])
        self.messages.cle
        chat_dict

def main():
    window = Tk()
    window.title("UDP Messenger")

    ui = ComradeUI(window)

    while True:
        window.update_idletasks()
        window.update()
        if not message_queue.empty():
            server_msg = message_queue.get()
            if server_msg == 'auth':
                print("LOGIN SUCC$$$")
                continue
            parsed = json.loads(server_msg)
            chat = parsed['chatName']
            nickname = parsed['nickname']
            data = parsed['data']
            timestamp = parsed['timestamp']

            if chat == login:
                chat = nickname

            if chat_dict.get(chat) is None:
                chat_dict[chat] = list()
                ui.add_chat(chat)

            msg = "[" + str(timestamp) + " ms : " + nickname + "] " + data

            if nickname == login:
                msg = "RECEIVED " + msg

            chat_dict[chat].append(msg)


main()
