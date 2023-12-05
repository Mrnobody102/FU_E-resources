package fpt.edu.eresourcessystem.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FeedbackEnum {
    VERY_SATISFIED(5, "Very Satisfied"),
    SATISFIED(4, "Satisfied"),
    GOOD(3, "Good"),
    NOT_GOOD(2, "Not good"),
    BAD(1, "Bad");
    private final int value;
    private final String code;

}

