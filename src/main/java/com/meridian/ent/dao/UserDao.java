package com.meridian.ent.dao;

import java.util.Collection;
import java.util.List;

import com.meridian.ent.model.InternalUser;

public interface UserDao {

    InternalUser findById(String userId);

    List<InternalUser> findActive();

    void upsert(InternalUser user);

    /** marks users that were not in the latest feed */
    int terminateMissing(Collection<String> seenUserIds);
}
