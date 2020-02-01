package com.thanos.contract.mockserver.infrastructure.parser;

import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContractFileParserTest {

    @Test
    public void parse() {

        final URL resource = ContractFileParserTest.class.getClassLoader().getResource("contracts/provider/consumer1/");
        final ContractFileParser contractParser = new ContractFileParser();
        final List<Contract> result =
                contractParser.parse(resource.getPath(), "contract1.yml");

        System.out.println(result);
        assertEquals(1, result.size());
        assertEquals("provider-schema1-v1", result.get(0).getSchemaIndex());
        assertEquals("consumer1", result.get(0).getConsumer());
        assertEquals("v2", result.get(0).getVersion());

    }

    @Test
    public void parse_with_full_path() {
        final URL resource = ContractFileParserTest.class.getClassLoader()
                .getResource("contracts/provider/consumer2/contract2.yml");

        final ContractFileParser contractParser = new ContractFileParser();

        final List<Contract> result =
                contractParser.parse(resource.getPath());
        System.out.println(result);

        assertEquals(1, result.size());
        assertEquals("provider-schema2-v1", result.get(0).getSchemaIndex());

    }
}