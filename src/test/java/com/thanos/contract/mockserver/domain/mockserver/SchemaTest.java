package com.thanos.contract.mockserver.domain.mockserver;

import com.thanos.contract.mockserver.domain.mockserver.model.Message;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.domain.mockserver.model.SchemaField;
import com.thanos.contract.mockserver.exception.BizException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class SchemaTest {

    Schema schema;

    @Before
    public void setUp() throws Exception {
        LinkedList<SchemaField> request = new LinkedList<>();
        request.add(new SchemaField("field1", "CHAR", 1, "1"));
        request.add(new SchemaField("field2", "CHAR", 1, "regex(0|1)"));

        LinkedList<SchemaField> response = new LinkedList<>();
        schema = new Schema("contracts/provider", "version", "name",
                request, response);
    }

    @Test(expected = BizException.class)
    public void parseRequest_StringIndexOutOfBoundsException() {
        schema.parseRequest("1");
    }

    @Test(expected = BizException.class)
    public void parseRequest_does_not_match() {
        schema.parseRequest("12");
    }

    @Test
    public void parseRequest_match() {
        final Message message = schema.parseRequest("10");
        assertEquals(schema.getIndex(), message.getMatchedSchema().getIndex());
    }
}