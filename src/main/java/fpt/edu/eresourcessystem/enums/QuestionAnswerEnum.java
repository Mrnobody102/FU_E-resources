package fpt.edu.eresourcessystem.enums;

public class QuestionAnswerEnum {

    public enum Status {
        CREATED("0", "created"),
        UNREAD("1", "unread"),
        READ("2", "read"),
        REPLIED("3", "replied");

        private final String displayValue;

        private final String toString;

        Status(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
    }
}
