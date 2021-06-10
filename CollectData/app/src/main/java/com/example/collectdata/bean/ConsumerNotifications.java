package com.example.collectdata.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ConsumerNotifications {

    @Expose
    List<ConsumerNotification> consumerNotificationList;

    public ConsumerNotifications(List<ConsumerNotification> consumerNotificationList) {
        this.consumerNotificationList = consumerNotificationList;
    }

    public List<ConsumerNotification> getConsumerNotificationList() {
        return consumerNotificationList;
    }

    public void setConsumerNotificationList(List<ConsumerNotification> consumerNotificationList) {
        this.consumerNotificationList = consumerNotificationList;
    }
}
