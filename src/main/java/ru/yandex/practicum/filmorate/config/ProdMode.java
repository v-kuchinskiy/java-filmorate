package ru.yandex.practicum.filmorate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProdMode implements ApplicationMode {

    @Value("${film.max-popular-count}")
    private int maxPopularCount;

    @Override
    public boolean isDevelopment() {
        return false;
    }

    @Override
    public boolean isProduction() {
        return true;
    }

    @Override
    public int getMaxPopularCount() {
        return maxPopularCount;
    }
}
