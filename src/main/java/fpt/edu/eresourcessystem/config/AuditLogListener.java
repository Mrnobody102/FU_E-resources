//package fpt.edu.eresourcessystem.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
//import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//
//@Configuration
//public class AuditLogListener extends AbstractMongoEventListener<Object> {
//
//    @Override
//    public void onBeforeSave(BeforeSaveEvent<Object> event) {
//        Object source = event.getSource();
//        LocalDateTime timestamp = LocalDateTime.now();
//        String username = getCurrentUsername();
//
//        Field createdByField = ReflectionUtils.findField(source.getClass(), "createdBy");
//        Field createdDateField = ReflectionUtils.findField(source.getClass(), "createdDate");
//        Field lastModifiedByField = ReflectionUtils.findField(source.getClass(), "lastModifiedBy");
//        Field lastModifiedDateField = ReflectionUtils.findField(source.getClass(), "lastModifiedDate");
//
//        if (createdByField != null) {
//            createdByField.setAccessible(true);
//            ReflectionUtils.setField(createdByField, source, username);
//        }
//
//        if (createdDateField != null) {
//            createdDateField.setAccessible(true);
//            ReflectionUtils.setField(createdDateField, source, timestamp);
//        }
//
//        if (lastModifiedByField != null) {
//            lastModifiedByField.setAccessible(true);
//            ReflectionUtils.setField(lastModifiedByField, source, username);
//        }
//
//        if (lastModifiedDateField != null) {
//            lastModifiedDateField.setAccessible(true);
//            ReflectionUtils.setField(lastModifiedDateField, source, timestamp);
//        }
//    }
//
//    private String getCurrentUsername() {
//        // return loggedInUser.getUsername();
//        return "Admin";
//    }
//}
