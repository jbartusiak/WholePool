package com.jba.service;

import com.jba.dao2.preferences.entity.Preference;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.service.ifs.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<Preference> getAllPreferences() {
        return userDAO.getAllPreferences();
    }

    @Override
    public Preference getPreferenceById(long id) {
        return userDAO.getPreferenceById(id);
    }
}
