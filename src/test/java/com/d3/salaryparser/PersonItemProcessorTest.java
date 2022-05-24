package com.d3.salaryparser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PersonItemProcessorTest {

    @Bean
    public PersonItemProcessor getPersonItemProcessor() {
        return new PersonItemProcessor();
    }

    @Test
    void processTransformTest() throws ProcessingException {
        final Person originalPerson = new Person();
        originalPerson.setEmployeeId(1);
        originalPerson.setFirstName("John");
        originalPerson.setLastName("Doe");
        originalPerson.setDepartmentId(1001);
        originalPerson.setSalaryDate(new Date(2022,06,05));
        originalPerson.setSalary(5000.00f);
        final Person transformedPerson = getPersonItemProcessor().process(originalPerson);

        //assert if all properties are matching but object reference is different
        assertEquals(originalPerson.getEmployeeId(),transformedPerson.getEmployeeId());
        assertEquals(originalPerson.getFirstName(),transformedPerson.getFirstName());
        assertEquals(originalPerson.getLastName(),transformedPerson.getLastName());
        assertEquals(originalPerson.getSalary(),transformedPerson.getSalary());
        assertEquals(originalPerson.getSalaryDateToString(),transformedPerson.getSalaryDateToString());
        assertEquals(originalPerson.getDepartmentId(),transformedPerson.getDepartmentId());
        assertNotEquals(Integer.toHexString(System.identityHashCode(originalPerson)),Integer.toHexString(System.identityHashCode(transformedPerson)));
    }

    @Test
    void processFilterTest1() {
        boolean processingStatus = false;
        final Person originalPerson = new Person();
        originalPerson.setEmployeeId(1);
        originalPerson.setFirstName("John");
        originalPerson.setLastName("Doe");
        originalPerson.setDepartmentId(1001);
        originalPerson.setSalaryDate(new Date(2022,06,05));
        originalPerson.setSalary(5000.00f);
        try {
            final Person transformedPerson = getPersonItemProcessor().process(originalPerson);
            processingStatus = true;
        } catch (ProcessingException e) {
            processingStatus = false;
        } finally {
            assertTrue(processingStatus);
        }
    }

    //process filter test incorrect department id
    @Test
    void processFilterTest2() {
        boolean processingStatus = false;
        final Person originalPerson = new Person();
        originalPerson.setEmployeeId(1);
        originalPerson.setFirstName("John");
        originalPerson.setLastName("Doe");
        originalPerson.setDepartmentId(2000);
        originalPerson.setSalaryDate(new Date(2022,06,05));
        originalPerson.setSalary(5000.00f);
        try {
            final Person transformedPerson = getPersonItemProcessor().process(originalPerson);
            processingStatus = true;
        } catch (ProcessingException e) {
            processingStatus = false;
        } finally {
            assertFalse(processingStatus);
        }
    }

}