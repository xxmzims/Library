package ru.ugrinovich.Library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title shouldn't be empty")
    @Size(min = 2, max=100, message="title should be between 2 and 100 characters")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "year")
    @Min(value = 1500, message = "year should be greater then 1500")
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id",referencedColumnName = "id")
    private Person owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_take_book")
    private Date dateTakeBook;

    @Transient
    private boolean isExpired = false;

    public Book() {
    }


    public Book(String title, String author, int year, Person owner) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.owner = owner;
    }
    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public Date getDateTakeBook() {
        return dateTakeBook;
    }

    public void setDateTakeBook(Date dateTakeBook) {
        this.dateTakeBook = dateTakeBook;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

