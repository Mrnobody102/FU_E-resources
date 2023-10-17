package fpt.edu.eresourcessystem.enums;

public class CourseEnum {
    public enum Major {
        HE("Information Technology", "HE"),
        HS("Business Administration", "HS"),
        HA("Language Studies", "HA");
        private final String displayValue;
        private final String toString;

        Major(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }

        public String getDisplayValue() {
            return displayValue;
        }
        public String getToString() {
            return toString;
        }
    }
}
