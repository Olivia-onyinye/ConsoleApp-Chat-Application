package org.example.chatApplication.Service;

import org.example.chatApplication.Util.closeReaderWriterUtil;

public interface userService extends closeReaderWriterUtil {
    void connectToSocket(String ipAddress, int port);
    void waitForMessage();
    String startChat();
}
