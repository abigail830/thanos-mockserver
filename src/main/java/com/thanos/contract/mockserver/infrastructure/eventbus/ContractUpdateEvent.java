package com.thanos.contract.mockserver.infrastructure.eventbus;

import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContractUpdateEvent {

    Contract contract;
}
