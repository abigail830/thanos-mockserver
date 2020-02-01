package com.thanos.contract.mockserver.domain.mockserver.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageServiceTest {

    MessageService messageService;

    @Before
    public void setUp() throws Exception {
        //prepare contract
        LinkedList<ContractField> req = new LinkedList<>();
        req.add(new ContractField("field1", "value1"));
        req.add(new ContractField("field2", "regex(0|1)"));
        LinkedList<ContractField> res = new LinkedList<>();
        res.add(new ContractField("field3", "response"));
        res.add(new ContractField("field4", "regex(2|3)"));

        Contract contract = new Contract("name", "version", "provider-name-version",
                "consumer", "provider", req, res);

        //prepare schema
        LinkedList<SchemaField> request = new LinkedList<>();
        request.add(new SchemaField("field1", "CHAR", 6, "value1"));
        request.add(new SchemaField("field2", "CHAR", 1, "regex(0|1|2)"));
        LinkedList<SchemaField> response = new LinkedList<>();
        response.add(new SchemaField("field3", "CHAR", 8, "response"));
        response.add(new SchemaField("field4", "CHAR", 1, "regex(0|1|2|3)"));
        response.add(new SchemaField("field5", "CHAR", 1, "Z"));
        response.add(new SchemaField("field6", "CHAR", 2, "regex(\\d{2})"));

        Schema schema = new Schema("provider", "version", "name",
                request, response);

        messageService = new MessageService(Arrays.asList(contract), Arrays.asList(schema));
    }

    @Test
    public void process_success() {
        final String result = messageService.process("value10");
        System.out.println(result);
        assertTrue(result.startsWith("response"));
        assertTrue(result.contains("Z"));
        assertEquals(12, result.length());
    }

    @Test
    public void process_not_match_contract() {
        final String result = messageService.process("value12");
        assertEquals("Incoming request does not match any existing contract", result);
    }

    @Test
    public void process_not_match_schema() {
        final String result = messageService.process("value1");
        assertEquals("Incoming request does not match any existing contract", result);
    }
}