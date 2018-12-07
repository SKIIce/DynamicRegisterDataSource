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
    public Tenant findByTenantID(String tenantID) {
        List<Tenant> tenantList = tenantDao.findByTenantID(tenantID);
        if(tenantList.size() == 1){
            return tenantList.get(0);
        }
        return null;
    }

    @Override
    public boolean verifyLogin(Tenant tenantUser) {
        List<Tenant> tenantList = tenantDao.findByTenantIDAndTenantPwd(tenantUser.getTenantID(),tenantUser.getTenantPwd());
        return tenantList.size() > 0;
    }
}
