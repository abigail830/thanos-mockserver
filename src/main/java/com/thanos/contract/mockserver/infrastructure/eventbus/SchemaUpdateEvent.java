package com.thanos.contract.mockserver.infrastructure.eventbus;

import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SchemaUpdateEvent {

    Schema schema;
}
