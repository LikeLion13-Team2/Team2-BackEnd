package com.poco.poco_backend.common.enums;

public enum PeriodType {
    DAY_1(1),
    DAY_3(3),
    DAY_7(7);

    private final int days;

    PeriodType(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
