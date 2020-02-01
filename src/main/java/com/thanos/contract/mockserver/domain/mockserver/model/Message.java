package com.thanos.contract.mockserver.domain.mockserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Getter
@NoArgsConstructor
public class Message {

    private Schema matchedSchema;
    private LinkedHashMap<String, String> parseRequest;

    public Message(Schema matchedSchema, LinkedHashMap<String, String> parseRequest) {
        this.matchedSchema = matchedSchema;
        this.parseRequest = parseRequest;
    }

    @Override
    public String toString() {
        return "Message{" +
                "matchedSchema=" +
                matchedSchema.getName() + '|' +
                matchedSchema.getVersion() + '|' +
                matchedSchema.getProvider() +
                ", parseRequest=" + parseRequest +
                '}';
    }

    public boolean matchContract(Contract contract) {
        for (ContractField field : contract.getReq()) {
            if (parseRequest.containsKey(field.getName()) &&
                    (!field.match(parseRequest.get(field.getName())))) {
                return false;
            }
        }
        return true;
    }

    public String buildResponse(Contract contract) {
        StringBuilder result = new StringBuilder();

        for (SchemaField schemaField : matchedSchema.getResponse()) {
            final String contractExpectValue = contract.getContractExpectValue(schemaField.getName());
            if (!contractExpectValue.isEmpty()) {
                result.append(contractExpectValue);
            } else {
                result.append((schemaField.getValidator()).getExpectedValue());
            }
        }
        return result.toString();
    }

    public LinkedHashMap<String, String> getParseRequest() {
        return parseRequest;
    }
}
