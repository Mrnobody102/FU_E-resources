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
        VIEW("View"),
        UPDATE_LECTURER("Update-lecturer"),
        REMOVE_LECTURER("remove-lecturer"),
        UPDATE_STATUS("update-status");
        private final String displayValue;

        Action(String displayValue) {
            this.displayValue = displayValue;
        }
        public String getDisplayValue() {
            return displayValue;
        }
    }
}
