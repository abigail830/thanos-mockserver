package com.thanos.contract.mockserver.domain.mockserver.validate;

import java.util.Objects;

public class PlainTextValidator implements Validator {
    private String expectedValue;

    public PlainTextValidator(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    public String name() {
        return "PlainTextValidator";
    }

    @Override
    public boolean validate(String actualValue) {
        return Objects.equals(expectedValue, actualValue);
    }

    @Override
    public String getExpectedValue() {
        return this.expectedValue;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
