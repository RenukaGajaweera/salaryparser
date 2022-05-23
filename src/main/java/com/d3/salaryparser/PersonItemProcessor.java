package com.d3.salaryparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(final Person person) throws ProcessingException {
		final Integer employeeId = person.getEmployeeId();
		final String firstName = person.getFirstName();
		final String lastName = person.getLastName();
		final Integer departmentId = person.getDepartmentId();
		final Date salaryDate = person.getSalaryDate();
		final Float salary = person.getSalary();

		final Person transformedPerson = new Person();
		transformedPerson.setEmployeeId(employeeId);
		transformedPerson.setFirstName(firstName);
		transformedPerson.setLastName(lastName);
		transformedPerson.setDepartmentId(departmentId);
		transformedPerson.setSalaryDate(salaryDate);
		transformedPerson.setSalary(salary);

		if (transformedPerson.getDepartmentId() < 1001 || person.getDepartmentId() > 1003) {
			throw new ProcessingException();
		}
		return transformedPerson;
	}

}
