package com.thanos.contract.mockserver.domain.mockserver.validate;

public interface Validator {
    String name();

    boolean validate(String actualValue);

    String getExpectedValue();

}
