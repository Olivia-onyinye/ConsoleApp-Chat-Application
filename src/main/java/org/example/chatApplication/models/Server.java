package org.example.chatApplication.models;

import org.example.chatApplication.Service.ServiceImpl.UserManagerServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();

        }
    }

    public void runServer() {
        // Listen for connections (clients to connect) on port 1240
        System.out.println("Server Begins....\nOpen to users");
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client just connected!");
                Thread thread1 = new Thread(new UserManagerServiceImpl(socket));
                thread1.start();
            }
        } catch (IOException e) {
            endServer();
        }
    }

    public void endServer(){
        System.out.println("Server is closed");
        try{
       if (serverSocket != null) this.serverSocket.close();
        } catch(IOException e){
           e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Server down....")));
        Server server = new Server(1240);
        server.runServer();
    }
}

