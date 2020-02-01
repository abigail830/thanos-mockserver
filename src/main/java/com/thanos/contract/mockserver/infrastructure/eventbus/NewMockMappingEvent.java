package com.thanos.contract.mockserver.infrastructure.eventbus;

import com.thanos.contract.mockserver.domain.mapping.MockMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewMockMappingEvent {

    String index;
    int port;

    public MockMapping toMockMapping() {
        return new MockMapping(index, port);
    }
}
