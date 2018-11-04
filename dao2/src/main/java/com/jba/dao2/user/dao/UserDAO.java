package com.jba.dao2.user.dao;

import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
public interface UserDAO {
    
    @Transactional
    User getUserById(int id);

    @Transactional
    User getUserByEmail(String email);

    @Transactional
    User getUserByName(String name);

    @Transactional
    boolean verifyUserPasswordHash(User user, String hash);

    @Transactional
    void updateUserData(User user);

    @Transactional
    UsersPreference setPreference(User user, Preference preference, String value);

    @Transactional
    Map<String,String> getUsersPreferences(User user);

    @Transactional
    User addNewUser(User user);

    @Transactional
    void resetPassword(User user, String passwordHash);
    
}
