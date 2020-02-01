package com.thanos.contract.mockserver.domain.mockserver;

import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.ContractField;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class ContractTest {

    Contract contract;

    @Before
    public void setUp() throws Exception {
        LinkedList<ContractField> req = new LinkedList<>();
        req.add(new ContractField("ield1", "1"));
        req.add(new ContractField("field2", "1"));

        LinkedList<ContractField> res = new LinkedList<>();
        res.add(new ContractField("field3", "1"));
        res.add(new ContractField("field4", "1"));

        contract = new Contract("name", "version", "schemaIndex",
                "contracts/provider", "consumer", req, res);
    }

    @Test
    public void getContractExpectValue() {
        final String result = contract.getContractExpectValue("field3");
        assertEquals("1", result);
    }

    @Test
    public void getContractExpectValue_empty() {
        final String result = contract.getContractExpectValue("field5");
        assertTrue(result.isEmpty());
    }

    @Test
    public void matchSchemaIndex() {
        assertTrue(contract.matchSchemaIndex("schemaIndex"));
    }

    @Test
    public void notMatchSchemaIndex() {
        assertFalse(contract.matchSchemaIndex("schemaIndex1"));
    }
}