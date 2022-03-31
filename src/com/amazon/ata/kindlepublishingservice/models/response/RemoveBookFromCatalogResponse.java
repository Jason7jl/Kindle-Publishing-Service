package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.models.Book;

import java.util.Objects;

public class RemoveBookFromCatalogResponse {

    private Book book;

    public RemoveBookFromCatalogResponse() {
    }

    public RemoveBookFromCatalogResponse(Builder builder) {
        this.book = builder().book;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static Builder builder() {return new Builder();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveBookFromCatalogResponse that = (RemoveBookFromCatalogResponse) o;
        return getBook().equals(that.getBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBook());
    }

    public  static final class Builder {
        private Book book;

        public Builder withBook(Book bookToUse) {
            this.book = bookToUse;
            return this;
        }
       public RemoveBookFromCatalogResponse build() {
            return new RemoveBookFromCatalogResponse(this);
       }
    }

}
