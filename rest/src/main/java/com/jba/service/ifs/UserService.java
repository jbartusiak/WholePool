package com.jba.service.ifs;

import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.preferences.entity.UsersPreference;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    List<User> getAllUsers();
    User getUser(int id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User updateUser(User user);
    boolean verifyPasswordHash(User user, String hash);
    User addNewUser(User user);
    Map<String, String> getUsersPreferences(User user);
    UsersPreference addPreference(User user, Preference preference, String value);
    UsersPreference updatePreference(User user, Preference preference, String value);
    boolean deletePreference(UsersPreference usersPreference);
}
