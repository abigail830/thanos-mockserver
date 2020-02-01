package com.thanos.contract.mockserver.infrastructure.client;

import com.thanos.contract.mockserver.domain.mockserver.MockServerRepository;
import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.exception.BizException;
import com.thanos.contract.mockserver.infrastructure.dto.ContractDTO;
import com.thanos.contract.mockserver.infrastructure.dto.SchemaDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ContractRestClientRepoImpl implements MockServerRepository {

    private ContractRestClient restClient;

    public ContractRestClientRepoImpl() {
        this.restClient = new ContractRestClient();
    }

    @Override
    public List<String> getAllContractIndex() throws BizException {
        try {
            return restClient.getAllContractIndex();
        } catch (Exception e) {
            log.error("Fail to HTTP GET getAllContractIndex", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Contract> getContractByIndex(String index) throws BizException {
        try {
            return restClient.getContractByIndex(index).stream()
                    .map(ContractDTO::toContract)
                    .filter(Contract::isValid)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Fail to HTTP GET getContractByIndex", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Schema> getSchemaByIndex(List<String> schemaNeeded) throws BizException {
        try {
            return restClient.getSchemaByIndex(schemaNeeded).stream()
                    .map(SchemaDTO::toSchema)
                    .filter(Schema::isValid)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Fail to HTTP GET getSchemaByIndex", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Integer getContractCountByIndex(String index) throws BizException {
        try {
            return restClient.getContractCountByIndex(index);
        } catch (IOException e) {
            log.error("Fail to HTTP GET getContractCountByIndex", e);
            throw new BizException("Fail to HTTP GET getContractCountByIndex", e);
        }
    }
}
