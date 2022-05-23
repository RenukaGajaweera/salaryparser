package com.d3.salaryparser;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.beans.PropertyEditor;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${file-location}")
    private String fileLocation;

    @Bean
    public FlatFileItemReader<Person> reader() {
        Map<Class<?>, PropertyEditor> editors = new HashMap<>();
        editors.put(Integer.class, new CustomNumberEditor(Integer.class, NumberFormat.getNumberInstance(), false));
        editors.put(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
        editors.put(Float.class, new CustomNumberEditor(Float.class, NumberFormat.getNumberInstance(), false));
        return new FlatFileItemReaderBuilder<Person>()
                .name("employeeSalaryReader")
                .resource(new FileSystemResource(fileLocation))
                .delimited()
                .names(new String[]{"emp_id", "fist_name", "last_name", "department_id", "salary_date", "salary"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                    setCustomEditors(editors);
                }}).strict(false)
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (emp_id, first_name, last_name, dep_id, salary_date, salary) VALUES (:employeeId, :firstName, :lastName, :departmentId, :salaryDate, :salary)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer, JobCompletionNotificationListener listener) {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(reader())
                .processor(processor())
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(ProcessingException.class)
                .skipLimit(10)
                .noSkip(FileNotFoundException.class)
                .listener(listener)
                .writer(writer)
                .build();
    }

}
