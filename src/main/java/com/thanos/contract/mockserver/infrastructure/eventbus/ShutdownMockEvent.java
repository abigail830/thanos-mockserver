package com.thanos.contract.mockserver.infrastructure.eventbus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShutdownMockEvent {

    String index;
    int port;
}
