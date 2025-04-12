package org.aleos.service;

import lombok.RequiredArgsConstructor;
import org.aleos.dao.UserDao;
import org.aleos.exception.InsufficientMeritsException;
import org.aleos.exception.UserNotFoundException;
import org.aleos.model.User;

import java.util.Optional;

@RequiredArgsConstructor
public class StandardUserService implements UserService {

    private final UserDao userDao;

    private static void checkMeritsSufficiency(long remainingMerits) {
        if (remainingMerits < 0) {
            throw new InsufficientMeritsException("Insufficient merits");
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public void transferMerits(long fromUserId, long toUserId, long merits) {
        var fromUser = fetchUserOrThrow(fromUserId);
        var toUser = fetchUserOrThrow(toUserId);

        long remainingMerits = fromUser.getMerits() - merits;
        checkMeritsSufficiency(remainingMerits);
        long updatedMerits = toUser.getMerits() + merits;

        userDao.updateMerits(fromUserId, remainingMerits);
        userDao.updateMerits(toUserId, updatedMerits);
    }

    private User fetchUserOrThrow(long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }
}
