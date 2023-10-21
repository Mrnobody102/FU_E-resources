package fpt.edu.eresourcessystem.enums;

public class CourseEnum {
    public enum Status {
        Active("Active"),
        Inactive("Inactive");
        private final String displayValue;

        Status(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }
    public enum DeleteFlag {
        PRESERVED("Preserved"),
        DELETED("Deleted");
        private final String displayValue;

        DeleteFlag(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
    }

}
