package org.example.chatApplication.Service.ServiceImpl;


import org.example.chatApplication.Util.closeReaderWriterUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class UserManagerServiceImpl implements Runnable, closeReaderWriterUtil {
    private static final ArrayList<UserManagerServiceImpl> userManagerServiceArrayList = new ArrayList<>();
    private final Socket socket;
    private String userName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public UserManagerServiceImpl(Socket socket) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = bufferedReader.readLine();
            userManagerServiceArrayList.add(this);
            broadcastMessage("Server: " + userName+ " " + " has joined the chat");
            System.out.println("Server: " + userName + " has joined the chat");
        } catch (IOException ne) {
            ne.printStackTrace();
            removeClientManager();
        }
    }

    @Override
    public void run() {
        String messageFromUser;
        while (socket.isConnected()) {
            try {
                messageFromUser = bufferedReader.readLine();
                if (messageFromUser.equalsIgnoreCase("exit")) {
                    removeClientManager();
                    broadcastMessage(userName + " left the chat");
                    break;
                } else
                    broadcastMessage(userName + " :" + messageFromUser);
            }catch (NullPointerException e){
                removeClientManager();
                System.out.println(userName + " left the chat");
                broadcastMessage(userName + " left the chat");
                break;
            } catch (IOException e) {
                 closeReaderWriter(socket,bufferedReader,bufferedWriter);
                removeClientManager();
            }
        }
    }
//used to send a msg to everyone on the chat
    private void broadcastMessage(String messageToSend) {
        for (UserManagerServiceImpl umsi :userManagerServiceArrayList) {
            if(!umsi.userName.equals(userName))
                broadcastIndividualMessage(umsi, messageToSend);
        }
    }

    private  void broadcastIndividualMessage(UserManagerServiceImpl umsi, String messageToSend){
        try {
            umsi.bufferedWriter.write(messageToSend);
            umsi.bufferedWriter.newLine();
            umsi.bufferedWriter.flush();
        } catch (IOException e) {
            closeReaderWriter(socket, bufferedReader, bufferedWriter);
            removeClientManager();
        }
    }
   private  void removeClientManager(){
       userManagerServiceArrayList.remove(this);
       broadcastMessage("Server: " + userName + " has just left the chat!");
   }
}
