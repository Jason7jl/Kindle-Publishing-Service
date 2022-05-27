package com.amazon.ata.kindlepublishingservice.models;

import java.util.Objects;

public class RecommendationCacheKey {


    private final String genre;

    public RecommendationCacheKey(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationCacheKey that = (RecommendationCacheKey) o;
        return getGenre().equals(that.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGenre());
    }
}
