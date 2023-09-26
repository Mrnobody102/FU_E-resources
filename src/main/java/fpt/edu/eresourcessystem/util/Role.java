package fpt.edu.eresourcessystem.util;

public enum Role {
    STUDENT("Student"),
    TEACHER("Teacher"),
    LIBRARY_MANAGER("Library Manager");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
