package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public class BookPublishTask implements  Runnable{

  private BookPublishRequestManager bookPublishRequestManager;
  private CatalogDao catalogDao;
  private PublishingStatusDao publishingStatusDao;

  @Inject
  public BookPublishTask(BookPublishRequestManager bookPublishRequestManager,
                         CatalogDao catalogDao, PublishingStatusDao publishingStatusDao) {
    this.bookPublishRequestManager = bookPublishRequestManager;
    this.catalogDao = catalogDao;
    this.publishingStatusDao = publishingStatusDao;
  }

  @Override
    public void run() {

    BookPublishRequest bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();

    while (bookPublishRequest == null) {
      try {
        Thread.sleep(1000);
        bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();

      } catch(InterruptedException e){
        e.printStackTrace();
      }
    }


    publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
            PublishingRecordStatus.IN_PROGRESS, bookPublishRequest.getBookId());

    KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(bookPublishRequest);


    try {

      CatalogItemVersion catalogItemVersion = catalogDao.createOrUpdateBook(kindleFormattedBook);

      publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
              PublishingRecordStatus.SUCCESSFUL, catalogItemVersion.getBookId());


    } catch (BookNotFoundException e){

      publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
              PublishingRecordStatus.FAILED, bookPublishRequest.getBookId(), e.getMessage());

    }

    }
}
