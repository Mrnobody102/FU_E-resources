package fpt.edu.eresourcessystem.enums;

public class CommonEnum {
    public enum DeleteFlg {
        PRESERVED,
        DELETED;
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
