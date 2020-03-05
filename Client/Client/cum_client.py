import socket
import queue
import json
import threading
import random
from datetime import datetime
from tkinter import *
from tkinter import scrolledtext

message_queue = queue.Queue()
send_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_host = 'localhost'
server_port = 55555
client_host = 'localhost'
client_port = 27776 + random.randint(0, 1000)
chat_dict = {}


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


class Core:
    def __init__(self, cs_host, cs_port, rt_host, rt_port):
        self.send_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.cs_host = cs_host
        self.cs_port = cs_port
        self.rt_host = rt_host
        self.rt_port = rt_port

    def auth_rq(self, login):
        auth_msg = json.dumps({'type': 'auth', 'login': login, 'data': {'host': self.rt_host, 'port': self.rt_port}})
        send_socket.sendto(auth_msg.encode('utf-8'), (self.cs_host, self.cs_port))

    def create_chat(self, login, chat_name):
        cc_message = json.dumps({'type': 'createChat', 'login': login, 'data': {'name': chat_name}})
        send_socket.sendto(cc_message.encode('utf-8'), (self.cs_host, self.cs_port))

    def add_chat_member(self, login, chat_name, new_member_name):
        acm_message = json.dumps(
            {'type': 'addMember', 'login': login, 'data': {'chatName': chat_name, 'nickname': new_member_name}})
        send_socket.sendto(acm_message.encode('utf-8'), (self.cs_host, self.cs_port))

    def send_message(self, login, receiver, message):
        sd_message = json.dumps({'type': 'sendMessage', 'login': login, 'data': {'receiver': receiver,
                                                                                 'message': {'chatName': receiver,
                                                                                             'nickname': login,
                                                                                             'data': message,
                                                                                             'timestamp': datetime.timestamp(
                                                                                                 datetime.now())}}})
        send_socket.sendto(sd_message.encode('utf-8'), (self.cs_host, self.cs_port))


class ComradeUI:
    def __init__(self, window, core):
        self.core = core
        self.nickname = ''
        self.chat = None
        self.messages = scrolledtext.ScrolledText(window, width=80, height=40)
        self.messages.grid(column=0, row=0, columnspan=2, rowspan=10)

        self.send_entry = Entry(window, width=60)
        self.send_entry.grid(column=0, row=10)
        self.send_button = Button(window, text="SEND", bg="black", fg="red", width=20, command=self.send_message)
        self.send_button.grid(column=1, row=10)

        self.login_entry = Entry(window, width=20)
        self.login_entry.grid(column=2, row=0)
        self.login_button = Button(window, text="LOGIN", bg="orange", fg="black", width=20, command=self.login)
        self.login_button.grid(column=2, row=1)
        self.create_chat_entry = Entry(window, width=20)
        self.create_chat_entry.grid(column=2, row=2)
        self.create_chat_button = Button(window, text="CREATE CHAT", bg="orange", fg="black", width=20,
                                         command=self.create_chat)
        self.create_chat_button.grid(column=2, row=3)

        self.add_user_entry = Entry(window, width=20)
        self.add_user_entry.grid(column=2, row=4)
        window.bind("<Return>", self.send_message)
        self.add_user_button = Button(window, text="ADD USER TO CHAT", bg="orange", fg="black", width=20,
                                      command=self.add_user_to_chat)
        self.add_user_button.grid(column=2, row=5)

        self.create_dialog_entry = Entry(window, width=20)
        self.create_dialog_entry.grid(column=2, row=6)
        self.create_dialog_button = Button(window, text="CREATE DIALOG", bg="orange", fg="black", width=20,
                                           command=self.create_dialog)
        self.create_dialog_button.grid(column=2, row=7)

        self.chat_list = Listbox(window)
        self.chat_list.grid(column=2, row=8)
        self.chat_list.bind('<<ListboxSelect>>', self.update_chat)

    def add_chat(self, chat_name):
        self.chat_list.insert(END, chat_name)

    def get_chat_name(self):
        return self.chat

    def login(self):
        self.nickname = self.login_entry.get()
        self.core.auth_rq(self.nickname)

    def create_dialog(self):
        receiver = self.create_dialog_entry.get()
        if receiver == '':
            return
        self.core.send_message(self.nickname, receiver, "CONNECTION ESTABLISHED")

    def create_chat(self):
        chat = self.create_chat_entry.get()
        if chat is None:
            return

        self.core.create_chat(self.nickname, chat)

    def add_user_to_chat(self):
        member = self.add_user_entry.get()
        if member == '':
            return

        chat = self.get_chat_name()
        if chat is None:
            return

        self.core.add_chat_member(self.nickname, chat, member)

    def send_message(self, t=None):
        msg = self.send_entry.get()
        if msg == '':
            return

        chat = self.get_chat_name()
        if chat is None:
            return

        self.send_entry.delete(0, 'end')
        self.core.send_message(self.nickname, chat, msg)

    def update_chat(self, strange_arg=None):
        slctd = self.chat_list.curselection()
        if len(slctd) == 0:
            return None

        self.chat = self.chat_list.get(slctd[0])
        self.update()

    def update(self):
        chat = self.get_chat_name()
        if chat is None:
            return

        self.messages.delete('1.0', END)

        for msg in chat_dict[chat]:
            self.messages.insert('1.0', msg + '\n')



def main():
    window = Tk()
    window.title("UDP Messenger")

    rt = ReceiveThread(client_host, client_port)
    rt.start()

    core = Core(server_host, server_port, client_host, client_port)
    ui = ComradeUI(window, core)

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

            if chat == ui.nickname:
                chat = nickname

            if chat_dict.get(chat) is None:
                chat_dict[chat] = list()
                ui.add_chat(chat)

            msg = "[" + str(timestamp) + " ms : " + nickname + "] " + data

            if nickname == ui.nickname:
                msg = "RECEIVED " + msg

            chat_dict[chat].append(msg)
            ui.update()


main()
