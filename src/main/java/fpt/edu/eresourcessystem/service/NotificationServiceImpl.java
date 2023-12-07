package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.enums.NotificationEnum;
import fpt.edu.eresourcessystem.model.Notification;
import fpt.edu.eresourcessystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("notificationService")
@RequiredArgsConstructor
public class NotificationServiceImpl implements  NotificationService{

    private final NotificationRepository notificationRepository;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<Notification> findByToAccount(String email) {
        return notificationRepository.findByToAccount(email);
    }

    @Override
    public Notification addNotification(NotificationDto notificationDto) {
        Notification added =  notificationRepository.insert(new Notification(
                notificationDto
        ));
        messagingTemplate.convertAndSendToUser(added.getToAccount(),"/notifications/private", added.getContent());
        return added;
    }

    @Override
    public void updateNotificationStatus(NotificationEnum.NotificationStatus oldStatus, NotificationEnum.NotificationStatus newStatus) {
        Query query = new Query(Criteria.where("notificationStatus").is(oldStatus));
        Update update = new Update().set("notificationStatus", newStatus);
        mongoTemplate.updateFirst(query, update, Notification.class);
    }
}
