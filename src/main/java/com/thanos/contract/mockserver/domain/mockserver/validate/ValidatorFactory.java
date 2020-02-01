package com.thanos.contract.mockserver.domain.mockserver.validate;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.time.FastDateFormat;
import sun.misc.FloatingDecimal;

import java.text.ParseException;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: This lexer support ONLY one kind of validator, requires AST parsing for composite validator.
public class ValidatorFactory {

    private static final String PATTERN_IN_STRING = "^\\s?([A-Za-z0-a_]*)\\((.*)\\)\\s?$";

    private static final Pattern FUNCTION_PATTERN = Pattern.compile(PATTERN_IN_STRING);

    private Map<String, Function<String, Boolean>> PREDEFINED_FUNCTIONS = ImmutableMap.<String, Function<String, Boolean>>builder()
            .put("isNumber", (actual) -> {
                try {
                    FloatingDecimal.parseDouble(actual);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            })
            .put("isDatetime", (actual) -> {
                final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
                try {
                    FastDateFormat.getInstance(YMD_HMS).parse(actual);
                } catch (ParseException e) {
                    return false;
                }
                return true;
            })
            .build();

    public Validator getValidator(String value) {
        if (value.matches(PATTERN_IN_STRING)) {
            Matcher matcher = FUNCTION_PATTERN.matcher(value);
            if (matcher.find()) {
                String functionName = matcher.group(1);
                if (RegexValidator.NAME.equals(functionName)) {
                    String regexpValue = matcher.group(2);
                    return new RegexValidator(regexpValue);
                }
            }
        } else {
            return new PlainTextValidator(value);
        }
        throw new RuntimeException("parse validator failed.");
    }
}
