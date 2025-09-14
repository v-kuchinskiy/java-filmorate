package ru.yandex.practicum.filmorate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Primary
public class DevMode implements ApplicationMode {

    @Value("${film.max-popular-count}")
    private int maxPopularCount;

    @Override
    public boolean isDevelopment() {
        return true;
    }

    @Override
    public boolean isProduction() {
        return false;
    }

    @Override
    public int getMaxPopularCount() {
        return maxPopularCount;
    }
}
