package fpt.edu.eresourcessystem.enums;

public class AccountEnum {
    public enum Role {
        STUDENT,
        LECTURER,
        LIBRARIAN
    }

    public enum Campus {
        HN("FU-HOA LAC"),
        HCM("FU-HO CHI MINH"),
        DN("FU-DA NANG"),
        CT("FU-CAN THO"),
        QN("FU-QUY NHON");

        private final String displayValue;

        Campus(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }
    public enum Gender {
        M("Male"),
        F("Female"),
        O("Other");
        private final String displayValue;

        Gender(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }
}
