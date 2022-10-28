package org.example.chatApplication.models;


import chatApplication.Service.ServiceImpl.UserServiceImpl;


public  class User{
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.connectToSocket("localhost", 1240);
        userService.waitForMessage();
        userService.startChat();
    }
}
