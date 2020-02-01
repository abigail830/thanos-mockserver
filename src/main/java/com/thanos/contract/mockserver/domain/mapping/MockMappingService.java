package com.thanos.contract.mockserver.domain.mapping;

import com.thanos.contract.mockserver.exception.BizException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MockMappingService {

    private MockMappingCache mockMappingCache;

    public MockMappingService(MockMappingCache mockMappingCache) {
        this.mockMappingCache = mockMappingCache;
    }

    public List<MockMapping> getAllMockMapping() {
        final Map<String, Integer> allMockMapping = mockMappingCache.getAllMockMapping();
        return allMockMapping.keySet().stream()
                .map(key -> new MockMapping(key, allMockMapping.get(key)))
                .collect(Collectors.toList());
    }

    public void addNewMockMapping(MockMapping mockMapping) throws BizException {
        mockMappingCache.addNewMockMapping(mockMapping);
    }

    public void removeMockMapping(String index) throws BizException {
        mockMappingCache.removeMockMapping(index);
    }

    public List<String> getAllMockMappingIndexs() throws BizException {
        final Map<String, Integer> allMockMapping = mockMappingCache.getAllMockMapping();
        return new ArrayList<>(allMockMapping.keySet());
    }

    public void cleanupMockMapping() {
        mockMappingCache.cleanupMockMapping();
    }

    public Integer getPortByIndex(String index) {
        return mockMappingCache.getAllMockMapping().get(index);
    }

}
