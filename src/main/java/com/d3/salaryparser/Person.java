package com.d3.salaryparser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Person {

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DELEMETER = ",";
    private Integer employeeId;
    private String lastName;
    private String firstName;
    private Integer departmentId;
    private Date salaryDate;
    private Float salary;

    public Person() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Date getSalaryDate() {
        return salaryDate;
    }

    public String getSalaryDateToString() {
        return DATEFORMAT.format(salaryDate);
    }

    public void setSalaryDate(Date salaryDate) {
        this.salaryDate = salaryDate;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getEmployeeId()).append(DELEMETER)
                .append(getFirstName()).append(DELEMETER)
                .append(getLastName()).append(DELEMETER)
                .append(getDepartmentId()).append(DELEMETER)
                .append(getSalaryDateToString()).append(DELEMETER)
                .append(getSalary());
        return sb.toString();
    }

}
