package com.thanos.contract.mockserver.domain.mapping;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MockMappingServiceTest {

    @Test
    public void getAllMockMapping() {
        final MockMappingService service = new MockMappingService(new MockMappingCache());
        service.cleanupMockMapping();
        service.addNewMockMapping(new MockMapping("index", 1234));

        assertEquals(service.getAllMockMapping().size(), 1);
        assertEquals("MockMapping(index=index, port=1234)", service.getAllMockMapping().get(0).toString());

    }
}