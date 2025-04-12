package org.aleos.service;

import org.aleos.transaction.Transaction;
import org.aleos.model.User;

import java.util.Optional;

public interface UserService {

    @Transaction
    Optional<User> findById(long id);

    @Transaction
    void transferMerits(long fromUserId, long toUserId, long merits);
}
