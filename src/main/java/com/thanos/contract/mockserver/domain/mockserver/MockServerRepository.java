package com.thanos.contract.mockserver.domain.mockserver;

import com.thanos.contract.mockserver.domain.mockserver.model.Contract;
import com.thanos.contract.mockserver.domain.mockserver.model.Schema;
import com.thanos.contract.mockserver.exception.BizException;

import java.util.List;

public interface MockServerRepository {

    List<String> getAllContractIndex() throws BizException;

    List<Contract> getContractByIndex(String index) throws BizException;

    List<Schema> getSchemaByIndex(List<String> schemaNeeded) throws BizException;

    Integer getContractCountByIndex(String index) throws BizException;
}
