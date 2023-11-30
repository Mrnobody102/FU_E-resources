package fpt.edu.eresourcessystem.enums;

public class NotificationEnum {
    public enum NotificationStatus{
        NEW("0", "new"),
        UNREAD("1", "unread"),
        READ("2", "read"),
        DELETED("3", "deleted");

        private final String displayValue;

        private final String toString;

        NotificationStatus(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
    }

    public enum NotificationType{
        ASSIGN_LECTURER_TO_COURSE("0", "assign lecturer to course"), // implement added
        REMOVE_LECTURER_FROM_COURSE("1", "remove lecturer from course"),
        USER_SEND_FEEDBACK("2", "user send feedback"),
        STUDENT_SEND_REQUEST_DOCUMENT("3", "student send request"),
        STUDENT_ASK_ON_DOCUMENT("4", "student ask on document"),
        LECTURER_ANSWER_ON_DOCUMENT("5", "lecturer reply on document");
        private final String displayValue;

        private final String toString;

        NotificationType(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
    }
}
