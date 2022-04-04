package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookPublishRequestManager {

    private Queue<BookPublishRequest> bookPublishRequestList;

    @Inject
    public BookPublishRequestManager(Queue<BookPublishRequest> bookPublishRequestList) {
        this.bookPublishRequestList = bookPublishRequestList;
    }

    public void addBookPublishRequest (BookPublishRequest bookPublishRequest) {

        bookPublishRequestList.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
       return bookPublishRequestList.poll();
    }
}
