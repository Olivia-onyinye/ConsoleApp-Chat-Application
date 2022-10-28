package org.example.chatApplication.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public interface closeReaderWriterUtil {
    default void closeReaderWriter(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null)socket.close();
        }catch (IOException e){
            System.err.println("Closing Error: " + e.getMessage());
        }
    }
}