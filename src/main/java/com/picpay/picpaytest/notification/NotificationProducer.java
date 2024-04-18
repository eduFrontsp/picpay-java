package com.picpay.picpaytest.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.picpay.picpaytest.transaction.Transaction;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, Transaction> KafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, Transaction> KafkaTemplate) {
        this.KafkaTemplate = KafkaTemplate;
    }

    public void sendNotification(Transaction transaction) {
        KafkaTemplate.send("transaction-notification", transaction);
    }
}
