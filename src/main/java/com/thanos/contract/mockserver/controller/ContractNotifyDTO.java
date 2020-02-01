package com.thanos.contract.mockserver.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.ContractField;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractNotifyDTO {

    private String name;
    private String version;
    private String schemaIndex;
    private String consumer;
    private String provider;

    private LinkedList<ContractNotifyFieldDTO> request;
    private LinkedList<ContractNotifyFieldDTO> response;

    public static List<Contract> buildFrom(Iterable<Object> ymlResult) {
        final List<Contract> result = new ArrayList<>();
        for (Object record : ymlResult) {
            if (record instanceof com.thanos.contract.mockserver.infrastructure.dto.ContractDTO) {
                result.add(((com.thanos.contract.mockserver.infrastructure.dto.ContractDTO) record).toContract());
            }
        }
        return result;
    }

    public String getIndex() {
        return provider + '-' + consumer;
    }

    public Contract toContract() {
        final LinkedList<ContractField> request = this.request.stream()
                .filter(field -> !Strings.isNullOrEmpty(field.getContractContent()))
                .map(ContractNotifyFieldDTO::toContractField).collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<ContractField> response = this.response.stream()
                .filter(field -> !Strings.isNullOrEmpty(field.getContractContent()))
                .map(ContractNotifyFieldDTO::toContractField).collect(Collectors.toCollection(LinkedList::new));

        return new Contract(name, version, schemaIndex, consumer, provider, request, response);
    }

}