package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.recommendationsservice.RecommendationsService;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecomendationCacheDao {

    private final LoadingCache<BookGenre, List<BookRecommendation>> recommendationsCache;

    @Inject
    public RecomendationCacheDao(RecommendationsService recommendationsService) {
        this.recommendationsCache = CacheBuilder.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(CacheLoader.from(recommendationsService::getBookRecommendations));
    }


    public List<BookRecommendation> getBookRecommendations(BookGenre genre) {

        return recommendationsCache.getUnchecked(genre);
    }
}

