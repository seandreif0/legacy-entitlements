package com.meridian.ent.dao;

import java.util.List;

import com.meridian.ent.model.Entitlement;

public interface EntitlementDao {

    List<Entitlement> findByUser(String userId);

    boolean hasAccess(String userId, String datasetCode);

    void insert(Entitlement e);

    int deleteBatchGrants(String userId);

    int revokeAll(String userId);
}
