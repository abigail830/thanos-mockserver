package com.thanos.contract.mockserver.infrastructure.cache;

import com.thanos.contract.mockserver.domain.mockserver.MockServerRepository;
import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.exception.BizException;

import java.util.List;

public class FileBaseCacheRepoImpl implements MockServerRepository {

    FileBaseCache fileBaseCache;

    public FileBaseCacheRepoImpl() {
        fileBaseCache = new FileBaseCache();
        fileBaseCache.initContractMap();
        fileBaseCache.initSchemaMap();
    }

    @Override
    public List<String> getAllContractIndex() throws BizException {
        return FileBaseCache.getAllContractIndex();
    }

    @Override
    public List<Contract> getContractByIndex(String index) throws BizException {
        return FileBaseCache.getContractByIndex(index);
    }

    @Override
    public List<Schema> getSchemaByIndex(List<String> schemaNeeded) throws BizException {
        return FileBaseCache.getSchemaByIndex(schemaNeeded);
    }

    @Override
    public Integer getContractCountByIndex(String index) throws BizException {
        return FileBaseCache.getContractByIndex(index).size();
    }
}
