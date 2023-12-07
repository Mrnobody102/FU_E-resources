package fpt.edu.eresourcessystem.enums;

public class NotificationEnum {
    public enum NotificationStatus {
        UNREAD("1", "unread"),
        READ("2", "read");

        private final String displayValue;

        private final String toString;

        NotificationStatus(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
    }

    public enum NotificationType {
        ASSIGN_LECTURER_TO_COURSE("0", " assigned you to course "), // implement added
        REMOVE_LECTURER_FROM_COURSE("1", " removed you from course "),
        USER_SEND_FEEDBACK("2", " sent a feedback."),
        STUDENT_SEND_REQUEST_DOCUMENT("3", " sent a request in "),
        STUDENT_ASK_ON_DOCUMENT("4", " asked on "),
        LECTURER_ANSWER_ON_DOCUMENT("5", " replied on ");
        private final String displayValue;

        private final String toString;

        NotificationType(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }

        public static NotificationType getType(String type) {
            return switch (type) {
                case "1" -> NotificationType.STUDENT_ASK_ON_DOCUMENT;
                case "2" -> NotificationType.LECTURER_ANSWER_ON_DOCUMENT;

                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

        public static String getStringType(String type) {
            return switch (type) {
                case "1" -> NotificationType.STUDENT_ASK_ON_DOCUMENT.toString;
                case "2" -> NotificationType.LECTURER_ANSWER_ON_DOCUMENT.toString;

                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

    }


}
