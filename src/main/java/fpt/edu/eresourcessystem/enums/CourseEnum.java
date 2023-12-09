package fpt.edu.eresourcessystem.enums;

public class CourseEnum {
    public enum Status {

        NEW("New"),
        PUBLISH("Publish"),
        DRAFT("Draft"),
        HIDE("Hide");
        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }

    public enum ChangeableStatus {
        PUBLISH("Publish"),
        DRAFT("Draft"),
        HIDE("Hide");
        private final String displayValue;

        ChangeableStatus(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }
    public enum LecturerAction {
        CHANGE_STATUS("change status"),
        UPDATE("update"),
        DELETE("delete"),
        ADD("add");
        private final String displayValue;

        LecturerAction(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }

    public enum CourseObject {
        COURSE("course"),
        TOPIC("topic"),
        RESOURCE_TYPE("resource_type"),
        DOCUMENT("document"),
        ANSWER("answer");
        private final String displayValue;

        CourseObject(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }



}
