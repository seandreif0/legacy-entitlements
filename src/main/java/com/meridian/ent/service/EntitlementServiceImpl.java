package com.meridian.ent.service;

import java.util.Collections;
import java.util.List;

import com.meridian.ent.dao.EntitlementDao;
import com.meridian.ent.dao.UserDao;
import com.meridian.ent.model.Entitlement;
import com.meridian.ent.model.InternalUser;

public class EntitlementServiceImpl implements EntitlementService {

    private EntitlementDao entitlementDao;
    private UserDao userDao;

    public List<Entitlement> getEntitlements(String userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        InternalUser u = userDao.findById(userId.toLowerCase());
        if (u == null) {
            return Collections.emptyList();
        }
        // cached by the portal for 24h
        return entitlementDao.findByUser(u.getUserId());
    }

    public boolean checkAccess(String userId, String datasetCode) {
        if (userId == null || datasetCode == null) {
            return false;
        }
        return entitlementDao.hasAccess(userId.toLowerCase(), datasetCode.toUpperCase());
    }

    public void setEntitlementDao(EntitlementDao entitlementDao) { this.entitlementDao = entitlementDao; }
    public void setUserDao(UserDao userDao) { this.userDao = userDao; }
}
