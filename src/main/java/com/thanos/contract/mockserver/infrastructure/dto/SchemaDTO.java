package com.thanos.contract.mockserver.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.domain.mockserver.model.SchemaField;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemaDTO {

    private String provider;
    private String version;
    private String name;
    private LinkedList<SchemaFieldDTO> request;
    private LinkedList<SchemaFieldDTO> response;

    public String getIndex() {
        return provider + '-' + name + '-' + version;
    }

    public Schema toSchema() {
        final LinkedList<SchemaField> req = request.stream()
                .map(SchemaFieldDTO::toSchemaField).collect(Collectors.toCollection(LinkedList::new));
        final LinkedList<SchemaField> res = response.stream()
                .map(SchemaFieldDTO::toSchemaField).collect(Collectors.toCollection(LinkedList::new));
        return new Schema(provider, version, name, req, res);
    }

    public static List<Schema> buildFrom(Iterable<Object> ymlResult) {
        final List<Schema> result = new LinkedList<>();
        for (Object record : ymlResult) {
            if (record instanceof SchemaDTO) {
                result.add(((SchemaDTO) record).toSchema());
            }
        }
        return result;
    }
}
