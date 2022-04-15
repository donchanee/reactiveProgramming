package com.codestates.bta;

import java.util.Locale;

public class Person {
    String name;
    String email;
    String number;

    public Person(String name, String email, String number) {
        this.name = name;
        this.email = email;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Person toUpper() {
        this.name = this.getName().toUpperCase(Locale.ROOT);
        return this;
    }
}
