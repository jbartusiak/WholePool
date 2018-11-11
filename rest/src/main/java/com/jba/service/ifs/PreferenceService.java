package com.jba.service.ifs;

import com.jba.dao2.preferences.entity.Preference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PreferenceService {
    List<Preference> getAllPreferences();
    Preference getPreferenceById(long id);
}
