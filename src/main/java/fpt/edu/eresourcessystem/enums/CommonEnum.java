package fpt.edu.eresourcessystem.enums;

public class CommonEnum {
    public enum DeleteFlg {
        PRESERVED("Preserved"),
        DELETED("Deleted");
        private final String displayValue;

        DeleteFlg(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
    }

    public enum Action {
        CREATE("Create"),
        UPDATE("Update"),
        DELETE("Delete"),
        VIEW("View");
        private final String displayValue;

        Action(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
    }
}
