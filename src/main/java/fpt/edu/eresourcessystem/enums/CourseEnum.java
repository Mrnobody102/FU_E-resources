package fpt.edu.eresourcessystem.enums;

public class CourseEnum {
    public enum Major {
        SE("Information Technology"),
        HS("Business Administration"),
        HA("Language Studies");
        private final String displayValue;

        Major(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }
}
