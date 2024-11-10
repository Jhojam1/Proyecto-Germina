package com.Germina.Domain.Services;

import com.Germina.Persistence.Entities.ConfigHr;
import com.Germina.Persistence.Repositories.ConfigHrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Service
public class ConfigHrService {

    @Autowired
    private ConfigHrRepository configHrRepository;

    private static final String CUTOFF_TIME_KEY = "order.cutoff-time";

    public LocalTime getCutoffTime() {
        Optional<ConfigHr> cutoffTimeConfig = configHrRepository.findById(CUTOFF_TIME_KEY);
        return cutoffTimeConfig.map(config -> LocalTime.parse(config.getValue())).orElse(LocalTime.of(11, 0)); // Valor por defecto 11:00 si no existe en la base de datos
    }

    public void updateCutoffTime(String newTime) {
        ConfigHr cutoffTimeConfig = configHrRepository.findById(CUTOFF_TIME_KEY)
                .orElse(new ConfigHr(CUTOFF_TIME_KEY, newTime));
        cutoffTimeConfig.setValue(newTime);
        configHrRepository.save(cutoffTimeConfig);
    }
}
