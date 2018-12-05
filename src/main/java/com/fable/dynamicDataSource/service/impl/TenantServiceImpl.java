package com.fable.dynamicDataSource.service.impl;

import com.fable.dynamicDataSource.dao.TenantDao;
import com.fable.dynamicDataSource.domain.Tenant;
import com.fable.dynamicDataSource.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService{
    @Autowired
    TenantDao tenantDao;

    @Override
    public String findByTenantID(String tenantID) {
        String jdbcUrl = "";
        List<Tenant> tenantList = tenantDao.findByTenantID(tenantID);
        if(tenantList.size() == 1){
            jdbcUrl = tenantList.get(0).getJdbc_Url();
        }
        return jdbcUrl;
    }
}
