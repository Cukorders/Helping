package com.cukorders.helping.chatting;

public class NotificationModel {

    public String to;

    public Notification notifacation = new Notification();

    public static class Notification {
        public String title;
        public String text;
    }
}
