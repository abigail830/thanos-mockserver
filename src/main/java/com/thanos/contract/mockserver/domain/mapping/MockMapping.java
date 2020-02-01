package com.thanos.contract.mockserver.domain.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MockMapping {

    private String index;
    private Integer port;
}
