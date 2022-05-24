package com.d3.salaryparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    /**
     * process method will override the parent method. it will transform input type to output type.
     * for this job the input class and the output classes are the same.
     * filtering is done on the transformed object
     * */
    @Override
    public Person process(final Person person) throws ProcessingException {

        final Person transformedPerson = transformPerson(person);
        if (filterPerson(transformedPerson)) {
            throw new ProcessingException();
        }
        return transformedPerson;
    }

    private boolean filterPerson(Person transformedPerson) {
       return transformedPerson.getDepartmentId() < 1001 || transformedPerson.getDepartmentId() > 1003 ? true : false;
    }


    private Person transformPerson(final Person person) {
        final Person transformedPerson = new Person();
        final Integer employeeId = person.getEmployeeId();
        final String firstName = person.getFirstName();
        final String lastName = person.getLastName();
        final Integer departmentId = person.getDepartmentId();
        final Date salaryDate = person.getSalaryDate();
        final Float salary = person.getSalary();

        transformedPerson.setEmployeeId(employeeId);
        transformedPerson.setFirstName(firstName);
        transformedPerson.setLastName(lastName);
        transformedPerson.setDepartmentId(departmentId);
        transformedPerson.setSalaryDate(salaryDate);
        transformedPerson.setSalary(salary);

        return transformedPerson;
    }



}
