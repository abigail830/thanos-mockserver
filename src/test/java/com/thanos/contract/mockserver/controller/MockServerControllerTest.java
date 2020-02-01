package com.thanos.contract.mockserver.controller;

import com.google.common.eventbus.AsyncEventBus;
import com.thanos.contract.mockserver.domain.mapping.MockMappingService;
import com.thanos.contract.mockserver.domain.mockserver.MockServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockServerControllerTest {

    @Spy
    @InjectMocks
    MockServerController mockServerController;

    @Mock
    MockMappingService mockMappingService;
    @Mock
    MockServerService mockServerService;
    @Mock
    AsyncEventBus asyncEventBus;

    @Test
    public void initMock_not_create_new() {
        doReturn(Arrays.asList("newIndex")).when(mockMappingService).getAllMockMappingIndexs();
        doReturn(Arrays.asList("newIndex")).when(mockServerService).getAllContractIndex();

        mockServerController.initMock();

        verify(mockServerController, times(0)).createNewMockForIndex("newIndex");
    }

    @Test
    public void initMock_create_new() {
        doReturn(new ArrayList<>()).when(mockMappingService).getAllMockMappingIndexs();
        doReturn(Arrays.asList("newIndex")).when(mockServerService).getAllContractIndex();

        mockServerController.initMock();

        verify(mockServerController, times(1)).createNewMockForIndex("newIndex");
    }
}