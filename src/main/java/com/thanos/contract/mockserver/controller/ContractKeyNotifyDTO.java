package com.thanos.contract.mockserver.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractKeyNotifyDTO {

    private String name;
    private String version;
    private String schemaIndex;
    private String consumer;
    private String provider;

    public String getIndex() {
        return provider + '-' + consumer;
    }

    public Contract toContract() {
        return new Contract(name, version, schemaIndex, consumer, provider, null, null);
    }

}