package fpt.edu.eresourcessystem.enums;

public class QuestionAnswerEnum {

    public enum Status {
        CREATED("0", "Created"),
        UNREAD("1", "Unread"),
        READ("2", "Read"),
        REPLIED("3", "Replied");

        private final String displayValue;

        private final String toString;

        Status(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
        public String getToString() {
            return toString;
        }
    }
}
