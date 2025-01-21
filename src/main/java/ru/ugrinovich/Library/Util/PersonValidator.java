package ru.ugrinovich.Library.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ugrinovich.Library.models.Person;
import ru.ugrinovich.Library.services.PeopleService;

@Component
public class PersonValidator implements Validator {
    private PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleService.findByName(person.getName()).isPresent()){
            errors.rejectValue("name", "", " This user is already exist");
        }
    }

}
