package com.thanos.contract.mockserver.infrastructure.parser;

import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.exception.InfraException;
import com.thanos.contract.mockserver.infrastructure.dto.SchemaDTO;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class SchemaFileParser {

    Yaml yaml = new Yaml(new Constructor(SchemaDTO.class));

    public List<Schema> parse(String path, String fileName) {
        return parse(path + fileName);
    }

    public List<Schema> parse(String fullPath) {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(fullPath));
            final Iterable<Object> objects = yaml.loadAll(inputStream);
            return SchemaDTO.buildFrom(objects);

        } catch (IOException e) {
            throw new InfraException("IO Exception when access schema resource", e.getCause());
        }

    }
}
