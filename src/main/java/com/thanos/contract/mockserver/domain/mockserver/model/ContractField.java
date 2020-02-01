package com.thanos.contract.mockserver.domain.mockserver.model;

import com.thanos.contract.mockserver.domain.mockserver.validate.Validator;
import com.thanos.contract.mockserver.domain.mockserver.validate.ValidatorFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ContractField {

    private String name;
    private String content;
    private Validator validator;

    public ContractField(String name, String content) {
        this.name = name;
        this.content = content;
        validator = new ValidatorFactory().getValidator(content);
    }

    public boolean match(String actualContent) {
        return validator.validate(actualContent);
    }
}
