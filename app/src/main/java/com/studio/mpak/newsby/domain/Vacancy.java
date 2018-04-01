package com.studio.mpak.newsby.domain;

public class Vacancy {

    String position;
    String salary;

    public Vacancy(Vacancy vacancy) {
        this.position = vacancy.getPosition();
        this.salary = vacancy.getSalary();
    }


    public Vacancy(String position, String salary) {
        this.position = position;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "position='" + position + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }

    public boolean contains(String query) {
        return position.toLowerCase().contains(query) || salary.toLowerCase().contains(query);
    }
}
