package com.thanos.contract.mockserver.domain.mockserver.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class MessageService {


    private static final String MISMATCH_RESPONSE = "Incoming request does not match any existing contract";

    private List<Contract> contractList;
    private List<Schema> schemaList;

    public MessageService(List<Contract> contractList, List<Schema> schemaList) {
        this.contractList = contractList;
        this.schemaList = schemaList;
    }

    public String process(String inputRequest) {
        final Optional<Message> message = parseAndValidateMsg(inputRequest);
        if (!message.isPresent()) return MISMATCH_RESPONSE;

        Message msg = message.get();
        final List<Contract> matchedContracts = contractList.stream()
                .filter(contract -> contract.matchSchemaIndex(msg.getMatchedSchema().getIndex()))
                .sorted(Comparator.comparingInt(Contract::getPriority).reversed())
                .collect(Collectors.toList());

        for (Contract contract : matchedContracts) {
            if (msg.matchContract(contract)) {
                final String result = msg.buildResponse(contract);
                log.info("Incoming msg [{}] matched, responding [{}]", msg, result);
                return result;
            }
        }
        return MISMATCH_RESPONSE;
    }

    private Optional<Message> parseAndValidateMsg(String inputRequest) {
        for (Schema schema : schemaList) {
            try {
                return Optional.of(schema.parseRequest(inputRequest));
            } catch (Exception ex) {
                log.debug("Schema [{}] could not parse incoming request [{}]", schema.getName(), inputRequest);
            }
        }
        return Optional.empty();
    }
}
