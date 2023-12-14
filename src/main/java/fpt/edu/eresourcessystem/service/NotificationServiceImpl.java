package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.NotificationDto;
import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.NotificationEnum;
import fpt.edu.eresourcessystem.model.Notification;
import fpt.edu.eresourcessystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("notificationService")
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Notification findById(String noId) {
        Notification notification = notificationRepository.findByIdAndDeleteFlg(noId, CommonEnum.DeleteFlg.PRESERVED);
        return notification;
    }

    @Override
    public Notification updateNotification(Notification notification) {
        Notification result = notificationRepository.save(notification);
        return result;
    }

    @Override
    public List<NotificationResponseDto> findByToAccount(String email) {
        Query query = new Query(Criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
                .and("toAccount").is(email)
                .and("notificationStatus").is(NotificationEnum.NotificationStatus.UNREAD))
                .skip(0)
                .limit(5)
                .with(Sort.by(Sort.Order.desc("lastModifiedDate")));
        List<Notification> notifications = mongoTemplate.find(query, Notification.class);
        if (null != notifications) {
            List<NotificationResponseDto> responseList = notifications.stream()
                    .map(entity -> new NotificationResponseDto(entity))
                    .collect(Collectors.toList());
            return responseList;
        } else return null;
    }

    @Override
    public List<NotificationResponseDto> findAllByToAccount(String email) {
        Query query = new Query(Criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
                .and("toAccount").is(email))
                .with(Sort.by(Sort.Order.desc("createdDate")));
        List<Notification> notifications = mongoTemplate.find(query, Notification.class);
        if (null != notifications) {
            List<NotificationResponseDto> responseList = notifications.stream()
                    .map(entity -> new NotificationResponseDto(entity))
                    .collect(Collectors.toList());
            return responseList;
        } else return null;
    }

    @Override
    public List<NotificationResponseDto> findUnreadByToAccount(String email) {
        Query query = new Query(Criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
                .and("toAccount").is(email)
                .and("notificationStatus").is(NotificationEnum.NotificationStatus.UNREAD))
                .with(Sort.by(Sort.Order.desc("createdDate")));
        List<Notification> notifications = mongoTemplate.find(query, Notification.class);
        if (null != notifications) {
            List<NotificationResponseDto> responseList = notifications.stream()
                    .map(entity -> new NotificationResponseDto(entity))
                    .collect(Collectors.toList());
            return responseList;
        } else return null;
    }

    @Override
    public Notification addNotification(NotificationDto notificationDto) {
        Notification added = notificationRepository.insert(new Notification(
                notificationDto
        ));
        if (notificationDto.getType().equals("1")) {
            messagingTemplate.convertAndSendToUser(added.getToAccount(), "/notifications/private", new NotificationResponseDto(added));
        }
        if (notificationDto.getType().equals("3")) {
            messagingTemplate.convertAndSendToUser(added.getToAccount(), "/notifications/studentReply", new NotificationResponseDto(added));
        }
        if (notificationDto.getType().equals("2")) {
            messagingTemplate.convertAndSendToUser(added.getToAccount(), "/notifications/reply", new NotificationResponseDto(added));
        }
        if (notificationDto.getType().equals("4")) {
            messagingTemplate.convertAndSendToUser(added.getToAccount(), "/notifications/feedback", new NotificationResponseDto(added));
        }
        return added;
    }

    @Override
    public Notification addNotificationWhenUpdateDocument(Notification notification) {
        Notification added = notificationRepository.insert(notification);
        messagingTemplate.convertAndSendToUser(added.getToAccount(), "/notifications/course_change", new NotificationResponseDto(added));
        return added;
    }

    @Override
    public void markReadAll(String email) {
        Query query = new Query(Criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
                .and("toAccount").is(email)
                .and("notificationStatus").is(NotificationEnum.NotificationStatus.UNREAD));
        Update update = new Update().set("notificationStatus", NotificationEnum.NotificationStatus.READ);
        mongoTemplate.updateMulti(query, update, Notification.class);
    }

    @Override
    public void deleteNotification(List<String> ids) {
        notificationRepository.deleteByIdIn(ids);
    }

    @Override
    public void updateNotificationStatus(NotificationEnum.NotificationStatus oldStatus, NotificationEnum.NotificationStatus newStatus) {
        Query query = new Query(Criteria.where("notificationStatus").is(oldStatus));
        Update update = new Update().set("notificationStatus", newStatus);
        mongoTemplate.updateFirst(query, update, Notification.class);
    }
}
