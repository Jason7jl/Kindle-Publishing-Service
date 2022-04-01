package com.amazon.ata.kindlepublishingservice.publishing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BookPublishRequestManager {

    Queue<BookPublishRequest> bookPublishRequestList;

    public BookPublishRequestManager(Queue<BookPublishRequest> bookPublishRequest) {
        this.bookPublishRequestList = bookPublishRequestList;
    }

    public void addBookPublishRequest (BookPublishRequest bookPublishRequest) {

        bookPublishRequestList.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
       return bookPublishRequestList.poll();
    }
}
