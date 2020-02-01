package com.thanos.contract.mockserver.infrastructure.cache;

import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.infrastructure.parser.ContractFileParser;
import com.thanos.contract.mockserver.infrastructure.parser.SchemaFileParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FileBaseCache {

    private static final String CONTRACTS_PATH = "contracts";
    private static final String SCHEMAS_PATH = "schemas";

    private static Map<String, List<Contract>> contractsMap = new HashMap<>();
    private static Map<String, List<Schema>> schemasMap = new HashMap<>();

    static List<String> getAllContractIndex() {
        return new ArrayList<>(contractsMap.keySet());
    }

    static synchronized List<Contract> getContractByIndex(String index) {
        final List<Contract> result = contractsMap.get(index);
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    private static List<String> getPaths(String schemasPath) {
        final URL resource = FileBaseCache.class.getClassLoader().getResource(schemasPath);
        try {
            return Files.walk(Paths.get(resource.getPath()))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("Fail to get paths");
            return Collections.EMPTY_LIST;
        }
    }

    static synchronized List<Schema> getSchemaByIndex(List<String> schemaNeeded) {
        final List<Schema> result = schemaNeeded.stream()
                .map(s -> schemasMap.get(s))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    synchronized void initContractMap() {
        final List<String> contractPaths = getPaths(CONTRACTS_PATH);
        final ContractFileParser contractParser = new ContractFileParser();

        contractPaths.stream().map(contractParser::parse).flatMap(Collection::stream)
                .forEach(contract -> {
                    if (contractsMap.containsKey(contract.getIndex())) {
                        final List<Contract> contractList = contractsMap.get(contract.getIndex());
                        contractList.add(contract);
                        contractsMap.put(contract.getIndex(), contractList);
                    } else {
                        contractsMap.put(contract.getIndex(), new ArrayList<>(Arrays.asList(contract)));
                    }
                });

    }

    synchronized void initSchemaMap() {
        final List<String> schemaPaths = getPaths(SCHEMAS_PATH);
        final SchemaFileParser schemaParser = new SchemaFileParser();
        schemaPaths.stream().map(schemaParser::parse).flatMap(Collection::stream)
                .forEach(schema -> {
                    if (schemasMap.containsKey(schema.getIndex())) {
                        final List<Schema> list = schemasMap.get(schema.getIndex());
                        list.add(schema);
                        schemasMap.put(schema.getIndex(), list);
                    } else {
                        schemasMap.put(schema.getIndex(), new ArrayList<>(Arrays.asList(schema)));
                    }
                });
    }
}
