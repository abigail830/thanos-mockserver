package com.thanos.contract.mockserver.domain.mockserver.model;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class MessageTest {

    Message message;

    @Before
    public void setUp() throws Exception {
        LinkedHashMap<String, String> parseRequest = new LinkedHashMap<>();
        parseRequest.put("field1", "value1");
        parseRequest.put("field2", "0");
        message = new Message(new Schema(), parseRequest);
    }

    @Test
    public void matchContract() {
        LinkedList<ContractField> req = new LinkedList<>();
        req.add(new ContractField("field1", "value1"));
        Contract contract = new Contract("name", "version", "schemaIndex",
                "contracts/provider", "consumer", req, new LinkedList<>());

        assertTrue(message.matchContract(contract));
    }


    @Test
    public void matchContract_regex() {
        LinkedList<ContractField> req = new LinkedList<>();
        req.add(new ContractField("field1", "value1"));
        req.add(new ContractField("field2", "regex(0|1)"));
        Contract contract = new Contract("name", "version", "schemaIndex",
                "contracts/provider", "consumer", req, new LinkedList<>());

        assertTrue(message.matchContract(contract));
    }

    @Test
    public void matchContract_false() {
        LinkedList<ContractField> req = new LinkedList<>();
        req.add(new ContractField("field1", "value2"));
        Contract contract = new Contract("name", "version", "schemaIndex",
                "contracts/provider", "consumer", req, new LinkedList<>());

        assertFalse(message.matchContract(contract));
    }


    @Test
    public void matchContract_false_regex() {
        LinkedList<ContractField> req = new LinkedList<>();
        req.add(new ContractField("field1", "value1"));
        req.add(new ContractField("field2", "regex(1|2)"));
        Contract contract = new Contract("name", "version", "schemaIndex",
                "contracts/provider", "consumer", req, new LinkedList<>());

        assertFalse(message.matchContract(contract));
    }

    @Test
    public void buildResponse() {
        LinkedList<ContractField> res = new LinkedList<>();
        res.add(new ContractField("field1", "value1"));
        res.add(new ContractField("field2", "regex(1|2)"));
        Contract contract = new Contract("name", "version", "schemaIndex",
                "contracts/provider", "consumer", new LinkedList<>(), res);

        LinkedList<SchemaField> response = new LinkedList<>();
        response.add(new SchemaField("field1", "CHAR", 6, "value1"));
        response.add(new SchemaField("field2", "CHAR", 1, "regex(1|2|3)"));
        Schema schema = new Schema("contracts/provider", "version", "name",
                new LinkedList<>(), response);

        message = new Message(schema, new LinkedHashMap<>());

        final String result = message.buildResponse(contract);
        System.out.println(result);
        assertTrue(result.startsWith("value1"));
        assertEquals(7, result.length());
        assertFalse(result.contains("3"));
    }


}