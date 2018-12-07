package com.fable.dynamicDataSource.service;

import com.fable.dynamicDataSource.domain.Tenant;

public interface TenantService {

    Tenant findByTenantID(String tenantID);

    boolean verifyLogin(Tenant tenantUser);
}
