package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;
import javax.validation.ValidationException;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // check if the provided bookId exists in the catalog
    public void validateBookExists(String bookId) {
        // if book doesn't exist, throw a BookNotFoundException

        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null) {
            throw new BookNotFoundException(String.format("The book with the %s doesn't exist!", bookId));
        }
    }


    public CatalogItemVersion removeBookFromCatalog(String bookId) {

        if (bookId == null || bookId.isEmpty()) {
            throw new ValidationException();
        }

        CatalogItemVersion inactiveBook = getLatestVersionOfBook(bookId);
        if (inactiveBook == null || inactiveBook.isInactive()) {
            throw new BookNotFoundException(String.format("The book with the %s doesn't exist!", bookId));
        }
        inactiveBook.setInactive(true);
        dynamoDbMapper.save(inactiveBook);

        return inactiveBook;
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook kindleFormattedBook) {

        CatalogItemVersion catalogItemVersion;
        if (kindleFormattedBook.getBookId() == null) {

            catalogItemVersion = new CatalogItemVersion();
            catalogItemVersion.setBookId(KindlePublishingUtils.generateBookId());
            catalogItemVersion.setVersion(1);
            catalogItemVersion.setInactive(false);

        } else {

            catalogItemVersion = getBookFromCatalog(kindleFormattedBook.getBookId());
            catalogItemVersion.setVersion(catalogItemVersion.getVersion() + 1);
            removeBookFromCatalog(kindleFormattedBook.getBookId());
        }

        catalogItemVersion.setTitle(kindleFormattedBook.getTitle());
        catalogItemVersion.setAuthor(kindleFormattedBook.getAuthor());
        catalogItemVersion.setText(kindleFormattedBook.getText());
        catalogItemVersion.setGenre(kindleFormattedBook.getGenre());
        dynamoDbMapper.save(catalogItemVersion);


        return catalogItemVersion;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
}
