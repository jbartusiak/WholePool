package com.jba.dao2.user.dao;

import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@EnableTransactionManagement
public interface UserDAO {

    @Transactional
    List<User> getAllUsers();

    @Transactional
    User getUserById(int id);

    @Transactional
    List<Preference> getAllPreferences();

    @Transactional
    Preference getPreferenceById(long id);

    @Transactional
    User getUserByEmail(String email);

    @Transactional
    User getUserByName(String name);

    @Transactional
    String getUserPasswordHash(User user);

    @Transactional
    void updateUserData(User user);

    @Transactional
    UsersPreference setPreference(User user, Preference preference, String value);

    @Transactional
    Map<String,String> getUsersPreferences(User user);

    @Transactional
    boolean deletePreference(UsersPreference usersPreference);

    @Transactional
    User addNewUser(User user);

    @Transactional
    void resetPassword(User user, String passwordHash);
    
}
