package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.converters.CatalogItemConverter;
import com.amazon.ata.kindlepublishingservice.converters.RecommendationsCoralConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetBookResponse;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {

    private PublishingStatusDao publishingStatusDao;


    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;

    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {

        List<PublishingStatusItem> publishingStatusItems = publishingStatusDao.getPublishingStatus(publishingStatusRequest.getPublishingRecordId());

        List<PublishingStatusRecord> publishingStatusRecordList = new ArrayList<>();

        for (PublishingStatusItem publishingStatusItem: publishingStatusItems) {
           publishingStatusRecordList.add(new PublishingStatusRecord(publishingStatusItem.getStatus().toString(),
                   publishingStatusItem.getStatusMessage(),publishingStatusItem.getBookId()));

        }
        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(publishingStatusRecordList)
                .build();

    }
}
