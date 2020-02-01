package com.thanos.contract.mockserver.infrastructure.parser;

import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.exception.InfraException;
import com.thanos.contract.mockserver.infrastructure.dto.ContractFileDTO;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class ContractFileParser {

    Yaml yaml = new Yaml(new Constructor(ContractFileDTO.class));

    public List<Contract> parse(String path, String fileName) {
        return parse(path + fileName);
    }

    public List<Contract> parse(String fullPath) {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get(fullPath));
            final Iterable<Object> contractList = yaml.loadAll(inputStream);
            return ContractFileDTO.buildFrom(contractList);
        } catch (IOException e) {
            throw new InfraException("IO Exception when access contract resource", e.getCause());
        }
    }

}
