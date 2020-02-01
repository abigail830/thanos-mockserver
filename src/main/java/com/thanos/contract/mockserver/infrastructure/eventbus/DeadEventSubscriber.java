package com.thanos.contract.mockserver.infrastructure.eventbus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadEventSubscriber {

    @Subscribe
    public void handleDeadEvent(DeadEvent deadEvent) {
        log.warn("No subscriber for event : {}", deadEvent);
    }
}
