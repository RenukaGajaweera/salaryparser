package com.d3.salaryparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport implements SkipListener<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	private static ArrayList<String> errors = new ArrayList<>();

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("\n");
		log.info("******************************************************");
		log.info("**************Begin Job d3 Salary Parser**************");
		log.info("******************************************************");
		log.info("\n");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("\n");
			log.info("Number of errors: " + String.valueOf(errors.size()));
			log.info("Lines with errors:");
			errors.forEach(error -> log.info(error));
			log.info("\n");
			log.info("Average salary per employee");

			jdbcTemplate.query("SELECT * FROM people",
				(rs, row) -> {
				Person person = new Person();
				person.setEmployeeId(rs.getInt(2));
				person.setFirstName(rs.getString(3));
				person.setLastName(rs.getString(4));
				person.setDepartmentId(rs.getInt(5));
				person.setSalaryDate(rs.getDate(6));
				person.setSalary(rs.getFloat(7));
				return person;
			}
			).forEach(person -> log.info(person.getFirstName() + " " + person.getLastName() + ": " + person.getSalary() + " EUR"));
		}
		if(jobExecution.getStatus() == BatchStatus.FAILED) {
			log.info("Job execution failed");
		}
		log.info("******************************************************");
		log.info("**************Ending Job d3 Salary Parser**************");
		log.info("******************************************************");
	}

	@Override
	public void onSkipInRead(Throwable throwable) {
		FlatFileParseException flatFileParseException = (FlatFileParseException) throwable;
		errors.add(flatFileParseException.getInput());
	}

	@Override
	public void onSkipInWrite(Person person, Throwable throwable) {
		errors.add(person.toString());
	}

	@Override
	public void onSkipInProcess(Person person, Throwable throwable) {
		errors.add(person.toString());
	}
}
