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
        STUDENT_ASK_ON_DOCUMENT("1", " asked on "),
        LECTURER_REPLY_ON_DOCUMENT("2", " replied on "),
        STUDENT_REPLY_ON_DOCUMENT("3", " replied on "),
        USER_SEND_FEEDBACK("4", " sent a feedback."),
        STUDENT_SEND_REQUEST_DOCUMENT("5", " sent a request in ");

        private final String displayValue;

        private final String toString;

        NotificationType(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }

        public static NotificationType getType(String type) {
            return switch (type) {
                case "1" -> NotificationType.STUDENT_ASK_ON_DOCUMENT;
                case "2" -> NotificationType.LECTURER_REPLY_ON_DOCUMENT;
                case "3" -> NotificationType.STUDENT_REPLY_ON_DOCUMENT;
                case "4" -> NotificationType.USER_SEND_FEEDBACK;
                case "5" -> NotificationType.STUDENT_SEND_REQUEST_DOCUMENT;

                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

        public static String getStringType(String type) {
            return switch (type) {
                case "1" -> NotificationType.STUDENT_ASK_ON_DOCUMENT.toString;
                case "2" -> NotificationType.LECTURER_REPLY_ON_DOCUMENT.toString;
                case "3" -> NotificationType.USER_SEND_FEEDBACK.toString;
                case "4" -> NotificationType.STUDENT_SEND_REQUEST_DOCUMENT.toString;

                default -> throw new IllegalStateException("Unexpected value: " + type);
            };
        }

    }


}
