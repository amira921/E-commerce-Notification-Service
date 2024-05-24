package org.ecommerce.notification.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public interface NotificationService<T> {
    boolean sendNotification(String to, String content);
    T insertRecordInDB(T record);
    T updateRecordInDB(T record);
    List<T> getFailedRecords();
}