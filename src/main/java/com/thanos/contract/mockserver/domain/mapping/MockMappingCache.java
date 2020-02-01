package com.thanos.contract.mockserver.domain.mapping;

import com.thanos.contract.mockserver.exception.BizException;

import java.util.HashMap;
import java.util.Map;

public class MockMappingCache {

    private static Map<String, Integer> mockMappings;

    public MockMappingCache() {
        mockMappings = new HashMap<>();
    }

    synchronized void addNewMockMapping(MockMapping mockMapping) {
        if (mockMappings.containsKey(mockMapping.getIndex()))
            throw new BizException("MockServer already existed at" +
                    mockMapping.getIndex() + "/" + mockMappings.get(mockMapping.getIndex()));
        else
            mockMappings.put(mockMapping.getIndex(), mockMapping.getPort());
    }

    synchronized void removeMockMapping(String index) {
        mockMappings.remove(index);
    }

    synchronized Map<String, Integer> getAllMockMapping() {
        return mockMappings;
    }

    synchronized void cleanupMockMapping() {
        mockMappings = new HashMap<>();
    }

}
