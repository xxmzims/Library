package ru.ugrinovich.Library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ugrinovich.Library.models.Book;
import ru.ugrinovich.Library.models.Person;
import ru.ugrinovich.Library.repositories.BookRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private BookRepository bookRepository;

    public BooksService() {
    }

    @Autowired
    public BooksService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Boolean checkExpireDate(int id) {
        Date date = bookRepository.findById(id).get().getDateTakeBook();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);
        Date date10DaysAgo = cal.getTime();
        System.out.println(date10DaysAgo.after(date));
        return date10DaysAgo.after(date);
    }

    public List<Book> findAll(int page, int bookOnPage, Boolean sortByYear) {
        List<Book> books;
        if (sortByYear) {
            books = bookRepository.findAll(PageRequest.of(page, bookOnPage, Sort.by("year"))).getContent();
        } else {
            books = bookRepository.findAll(PageRequest.of(page, bookOnPage)).getContent();
        }

        for (Book book : books) {
            if (book.getOwner() != null)
                book.setExpired(checkExpireDate(book.getId()));
        }
        return books;
    }

    public Book findById(int id) {

        Optional<Book> optionalBook = bookRepository.findById(id);

        return optionalBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void assignBook(int id, Person person) {
        Book book = bookRepository.findById(id).get();
        book.setOwner(person);
        book.setDateTakeBook(new Date());
        bookRepository.save(book);

    }

    @Transactional
    public void release(int book_id) {
        Book book = bookRepository.findById(book_id).get();
        book.setOwner(null);
        book.setDateTakeBook(null);
        bookRepository.save(book);

    }

    public List<Book> search(String startingWith) {
        System.out.println(bookRepository.findByTitleStartingWith(startingWith));
        return bookRepository.findByTitleStartingWith(startingWith);
    }


}
