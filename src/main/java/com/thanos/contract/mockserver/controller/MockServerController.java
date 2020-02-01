package com.thanos.contract.mockserver.controller;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.thanos.contract.mockserver.domain.mapping.MockMapping;
import com.thanos.contract.mockserver.domain.mapping.MockMappingService;
import com.thanos.contract.mockserver.domain.mockserver.MockServerService;
import com.thanos.contract.mockserver.domain.mockserver.MockServerThread;
import com.thanos.contract.mockserver.exception.BizException;
import com.thanos.contract.mockserver.infrastructure.eventbus.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MockServerController {

    ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private List<String> startedIndexs = new ArrayList<>();

    private MockMappingService mockMappingService;
    private MockServerService mockServerService;
    private AsyncEventBus asyncEventBus;

    public MockServerController(MockMappingService mockMappingService, MockServerService mockServerService) {
        this.mockMappingService = mockMappingService;
        this.mockServerService = mockServerService;

        asyncEventBus = EventBusFactory.getInstance();
        asyncEventBus.register(this);
    }

    /**
     * Group mock server by provider-consumer
     */
    public synchronized void initMock() {
        startedIndexs = mockMappingService.getAllMockMappingIndexs();

        final List<String> allContractIndex = mockServerService.getAllContractIndex();

        allContractIndex.stream().filter(index -> !startedIndexs.contains(index))
                .forEach(this::createNewMockForIndex);
    }

    void createNewMockForIndex(String index) {
        log.info("Creating new mock server: {}", index);
        executor.execute(new MockServerThread(index, mockServerService));
    }

    @Subscribe
    public void receiveNewMockMappingEvent(NewMockMappingEvent newMockMappingEvent) {
        try {
            mockMappingService.addNewMockMapping(
                    new MockMapping(newMockMappingEvent.getIndex(), newMockMappingEvent.getPort()));
            startedIndexs.add(newMockMappingEvent.getIndex());

        } catch (BizException ex) {
            asyncEventBus.post(
                    new ShutdownMockEvent(newMockMappingEvent.getIndex(), newMockMappingEvent.getPort()));
            startedIndexs.remove(newMockMappingEvent.getIndex());
        }
    }

    @Subscribe
    public void receiveContractRemoveEvent(ContractRemoveEvent contractRemoveEvent) {
        final Integer contractCountByIndex =
                mockServerService.getContractCountByIndex(contractRemoveEvent.getContract().getIndex());
        if (contractCountByIndex == 0) {
            mockMappingService.removeMockMapping(contractRemoveEvent.getContract().getIndex());
            startedIndexs.remove(contractRemoveEvent.getContract().getIndex());
            log.info("MockServerController will remove {}.", contractRemoveEvent.getContract().getIndex());
        }
    }

    @Subscribe
    public void receiveContractUpdateEvent(ContractUpdateEvent contractUpdateEvent) {
        if (!startedIndexs.contains(contractUpdateEvent.getContract().getIndex())) {
            createNewMockForIndex(contractUpdateEvent.getContract().getIndex());
            log.info("Going to startup new MockServer Thread [{}]", contractUpdateEvent.getContract().getIndex());
        } else {
            log.debug("MockServer Thread [{}] already exist. No new startup needed",
                    contractUpdateEvent.getContract().getIndex());
        }
    }

}
