package ru.ugrinovich.Library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ugrinovich.Library.models.Book;
import ru.ugrinovich.Library.models.Person;
import ru.ugrinovich.Library.services.BooksService;
import ru.ugrinovich.Library.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BookController {

    private PeopleService peopleService;
    private BooksService booksService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping(params = {"page", "book_per_page", "sort_by_year"})
    public String index(Model model, @RequestParam(value = "page") int page,
                        @RequestParam(value = "sort_by_year") Boolean sortByYear,
                        @RequestParam(value = "book_per_page") int bookOnPage) {
        model.addAttribute("books", booksService.findAll(page, bookOnPage, sortByYear));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("assignToPerson") Person person) {
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("book", booksService.findById(id));
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books?page=0&book_per_page=5&sort_by_year=false";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id, @ModelAttribute("book") Book book) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book")
    @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.save(book);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.deleteById(id);
        return "redirect:/books?page=0&book_per_page=5&sort_by_year=false";
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("assignToPerson") Person person) {
        booksService.assignBook(id, person);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("{id}/release")
    public String release(@PathVariable("id") int book_id) {
        booksService.release(book_id);
        return "redirect:/books/" + book_id;
    }
    @GetMapping("/search")
    public String search(@ModelAttribute("book") Book book){
        return "books/search";
    }
    @GetMapping("/doSearch")
        public String doSearch(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("searchResult", booksService.search(book.getTitle()));
        return "redirect:/books/search";
    }

}
