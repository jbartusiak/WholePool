package com.jba.service;

import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.dao2.user.enitity.User;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userDAO.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByName(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        userDAO.updateUserData(user);
        return user;
    }

    @Override
    public boolean verifyPasswordHash(User user, String hash) {
        String usersHash = userDAO.getUserPasswordHash(user);
        return usersHash.equals(hash);
    }

    @Override
    public User addNewUser(User user) {
        user.setUserId(0);
        return userDAO.addNewUser(user);
    }

    @Override
    public Map<String, String> getUsersPreferences(User user) {
        return userDAO.getUsersPreferences(user);
    }

    @Override
    public UsersPreference addPreference(User user, Preference preference, String value) {
        preference.setPreferenceId(0);

        return userDAO.setPreference(user, preference, value);
    }

    @Override
    public UsersPreference updatePreference(User user, Preference preference, String value) {
        return userDAO.setPreference(user, preference, value);
    }

    @Override
    public boolean deletePreference(UsersPreference usersPreference) {
        return userDAO.deletePreference(usersPreference);
    }
}
