package com.meridian.ent.service;

import java.util.List;

import com.meridian.ent.model.Entitlement;

public interface EntitlementService {

    List<Entitlement> getEntitlements(String userId);

    boolean checkAccess(String userId, String datasetCode);
}
