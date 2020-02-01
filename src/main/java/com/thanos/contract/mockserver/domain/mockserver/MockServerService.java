package com.thanos.contract.mockserver.domain.mockserver;

import com.google.common.base.Strings;
import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.Message;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.exception.BizException;
import com.thanos.contract.mockserver.infrastructure.eventbus.ContractRemoveEvent;
import com.thanos.contract.mockserver.infrastructure.eventbus.ContractUpdateEvent;
import com.thanos.contract.mockserver.infrastructure.eventbus.EventBusFactory;
import com.thanos.contract.mockserver.infrastructure.eventbus.SchemaUpdateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MockServerService {

    private MockServerRepository mockServerRepository;

    public MockServerService(MockServerRepository mockServerRepository) {
        this.mockServerRepository = mockServerRepository;
    }

    public List<String> getAllContractIndex() {
        return mockServerRepository.getAllContractIndex();
    }

    public List<Contract> getContractByIndex(String index) {
        return mockServerRepository.getContractByIndex(index);
    }

    public List<Schema> getSchemaByIndex(List<String> schemaNeeded) {
        return mockServerRepository.getSchemaByIndex(schemaNeeded);

    }

    public String buildRequestForContract(String consumer, String provider,
                                          String contractName, String contractVersion) {

        validationInput(consumer, provider);

        String contractIndex = provider + '-' + consumer;
        final Contract matchedContract = getContractByIndex(contractIndex).stream()
                .filter(contract -> contract.getName().equalsIgnoreCase(contractName))
                .filter(contract -> contract.getVersion().equalsIgnoreCase(contractVersion))
                .findFirst().orElseThrow(() -> new BizException("Fail to get associated contract"));

        final Schema matchedSchema =
                getSchemaByIndex(Arrays.asList(matchedContract.getSchemaIndex()))
                        .stream().findFirst().orElseThrow(() -> new BizException("Fail to get associated schema"));

        return matchedSchema.getRequest().stream()
                .map(schemaField -> matchedContract.getReq().stream()
                        .filter(field -> field.getName().equalsIgnoreCase(schemaField.getName()))
                        .map(f -> f.getValidator().getExpectedValue())
                        .findFirst()
                        .orElse(schemaField.getValidator().getExpectedValue()))
                .collect(Collectors.joining());
    }

    void validationInput(String consumer, String provider) {
        if (Strings.isNullOrEmpty(consumer) || Strings.isNullOrEmpty(provider))
            throw new BizException("Invalid null or empty input parameter.");
    }

    public String parseRequest(String request, String provider, String schemaName, String schemaVersion) {
        String schemaIndex = provider + '-' + schemaName + '-' + schemaVersion;
        final Schema matchedSchema = getSchemaByIndex(Arrays.asList(schemaIndex))
                .stream().findFirst().orElseThrow(() -> new BizException("Fail to get associated schema"));

        final Message message = matchedSchema.parseRequest(request);
        return message.getParseRequest().toString();
    }

    public void addOrUpdateSchema(Schema schema) {
        if (schema.isValid()) {
            EventBusFactory.getInstance().post(new SchemaUpdateEvent(schema));
            log.debug("SchemaUpdateEvent send [{}]", schema);
        } else {
            throw new BizException("Invalid input");
        }
    }

    public void addOrUpdateContract(Contract contract) {
        if (contract.isValid()) {
            EventBusFactory.getInstance().post(new ContractUpdateEvent(contract));
            log.debug("ContractUpdateEvent send [{}]", contract);
        } else {
            throw new BizException("Invalid input");
        }
    }

    public void removeContract(Contract contract) {
        EventBusFactory.getInstance().post(new ContractRemoveEvent(contract));
        log.debug("ContractRemoveEvent send [{}]", contract);
    }

    public Integer getContractCountByIndex(String index) {
        return mockServerRepository.getContractCountByIndex(index);
    }
}
