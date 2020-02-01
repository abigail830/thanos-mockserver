package com.thanos.contract.mockserver.domain.mockserver.validate;

import com.mifmif.common.regex.Generex;

public class RegexValidator implements Validator {

    public static final String NAME = "regex";
    private String regexp;

    public RegexValidator(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean validate(String actualValue) {
        return actualValue != null && actualValue.matches(regexp);
    }

    public String getRegexp() {
        return this.regexp;
    }

    @Override
    public String getExpectedValue() {
        Generex generex = new Generex(this.regexp);
        return generex.random();
    }

    @Override
    public String toString() {
        return this.name();
    }
}
