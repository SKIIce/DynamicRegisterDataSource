package com.fable.dynamicDataSource.dao;

import com.fable.dynamicDataSource.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantDao extends JpaRepository<Tenant, String> {
    List<Tenant> findByTenantID(String tenantID);
}
