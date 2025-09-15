package ru.yandex.practicum.filmorate.config;

public interface ApplicationMode {
    boolean isDevelopment();

    boolean isProduction();

    int getMaxPopularCount();
}
