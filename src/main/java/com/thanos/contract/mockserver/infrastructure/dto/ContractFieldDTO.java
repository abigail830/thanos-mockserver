package com.thanos.contract.mockserver.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanos.contract.mockserver.domain.mockserver.model.ContractField;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractFieldDTO {

    private String name;
    private String content;

    public ContractField toContractField() {
        return new ContractField(name, content);
    }
}
