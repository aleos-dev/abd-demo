package org.aleos.dao;

import org.aleos.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    void updateMerits(long userId, long merits);
}
