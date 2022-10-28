package org.example.chatApplication.Service.ServiceImpl;

import chatApplication.Service.userService;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UserServiceImpl implements userService {
    private Socket socket;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void connectToSocket(String ipAddress, int port) {
        System.out.println("What is your preferred username");
        String name = scanner.nextLine();
        System.out.println("Creating connection with server...");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //client connects with a wrong port
        int amountOfTry = 0;
        while (amountOfTry < 3 && socket == null) {
            try {
                amountOfTry++;
                socket = new Socket(ipAddress, port);
                socket.isConnected();
            } catch (IOException e) {
                System.out.println("Trying again in few seconds");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ne) {
                    ne.printStackTrace();
                }
                if (amountOfTry == 3) System.err.println("Unable to create connection with server");
            }
        }
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connected Successfully");
        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void waitForMessage() {
        new Thread(() -> {
            String groupChatMessage;
            while (socket.isConnected()) {
                try {
                    groupChatMessage = bufferedReader.readLine();
                    if (groupChatMessage == null) {
                        socket.isClosed();
                        break;
                    } else System.out.println(groupChatMessage);
                } catch (IOException e) {
                    closeReaderWriter(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    @Override
    public String startChat() {
        String messageToSend;
        try {
            while (socket.isConnected()) {
                messageToSend = scanner.nextLine();
                if (messageToSend.equalsIgnoreCase("exit")) {
                    bufferedWriter.write(messageToSend);
                    closeReaderWriter(socket, bufferedReader, bufferedWriter);
                    socket.isClosed();
                } else {
                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            closeReaderWriter(socket, bufferedReader, bufferedWriter);
            e.printStackTrace();
        }
        return "messageToSend";
    }
}
