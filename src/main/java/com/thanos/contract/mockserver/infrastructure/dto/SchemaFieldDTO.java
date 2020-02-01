package com.thanos.contract.mockserver.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanos.contract.mockserver.domain.mockserver.model.SchemaField;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaFieldDTO {
    String name;
    String type;
    Integer length;
    String content;

    public SchemaField toSchemaField() {
        return new SchemaField(name, type, length, content);
    }

}
