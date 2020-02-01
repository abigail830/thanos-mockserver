package com.thanos.contract.mockserver.infrastructure.eventbus;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBusFactory {

    private static AsyncEventBus asyncEventBus = null;

    private EventBusFactory() {
    }

    public static synchronized AsyncEventBus getInstance() {
        if (asyncEventBus == null) {
            final ExecutorService executorService =
                    Executors.newFixedThreadPool(10);
            asyncEventBus = new AsyncEventBus(executorService);
        }
        return asyncEventBus;
    }

}
