package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("notificationRepository")
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByToAccount(String email);
}
