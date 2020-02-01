package com.thanos.contract.mockserver.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ContractDTO {

    private String name;
    private String version;
    private String schemaIndex;
    private String consumer;
    private String provider;

    private LinkedList<ContractFieldDTO> req;
    private LinkedList<ContractFieldDTO> res;

    public String getIndex() {
        return provider + '-' + consumer;
    }

    public Contract toContract() {
        final LinkedList<ContractField> request = this.req.stream()
                .map(ContractFieldDTO::toContractField).collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<ContractField> response = this.res.stream()
                .map(ContractFieldDTO::toContractField).collect(Collectors.toCollection(LinkedList::new));

        return new Contract(name, version, schemaIndex, consumer, provider, request, response);
    }

    public static List<Contract> buildFrom(Iterable<Object> ymlResult) {
        final List<Contract> result = new ArrayList<>();
        for (Object record : ymlResult) {
            if (record instanceof ContractDTO) {
                result.add(((ContractDTO) record).toContract());
            }
        }
        return result;
    }

}
