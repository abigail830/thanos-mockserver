package com.thanos.contract.mockserver.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanos.contract.mockserver.domain.mockserver.model.ContractField;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractNotifyFieldDTO {

    private String name;
    private String contractContent;

    public ContractField toContractField() {
        return new ContractField(name, contractContent);
    }
}
