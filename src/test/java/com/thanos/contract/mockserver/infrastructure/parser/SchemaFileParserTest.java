package com.thanos.contract.mockserver.infrastructure.parser;

import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SchemaFileParserTest {

    @Test
    public void parse() {
        final URL resource = SchemaFileParserTest.class.getClassLoader().getResource("schemas/");
        SchemaFileParser schemaFileParser = new SchemaFileParser();

        final List<Schema> result = schemaFileParser.parse(resource.getPath(), "provider_schema1_v1.yml");

        System.out.println(result);
        assertEquals(1, result.size());
        assertEquals("v1", result.get(0).getVersion());

        assertEquals(6, result.get(0).getRequest().size());
        assertEquals("MESSAGE_TYPE", result.get(0).getRequest().get(2).getName());

        assertEquals(2, result.get(0).getResponse().size());
    }
}