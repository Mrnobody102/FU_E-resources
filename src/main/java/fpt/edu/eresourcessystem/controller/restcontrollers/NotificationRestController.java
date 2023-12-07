package fpt.edu.eresourcessystem.controller.restcontrollers;

import fpt.edu.eresourcessystem.dto.Response.NotificationResponseDto;
import fpt.edu.eresourcessystem.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notification")
public class NotificationRestController {
    private final NotificationService notificationService;

    public NotificationRestController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{accountId}")
    public List<NotificationResponseDto> getNotifications(@PathVariable(name = "accountId", required = false) String accountId) {
        List<NotificationResponseDto> notificationResponseDtos = notificationService.findByToAccount(accountId).stream().map(o -> new NotificationResponseDto(o)).toList();
        return notificationResponseDtos;
    }
}
