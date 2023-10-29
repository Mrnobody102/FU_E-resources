package fpt.edu.eresourcessystem.enums;

public class CourseEnum {
    public enum Status {
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

}
