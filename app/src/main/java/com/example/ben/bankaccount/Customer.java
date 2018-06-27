package com.example.ben.bankaccount;

public class Customer {
    private String name;
    private String dob;
    private String balance;

    public Customer(String name, String dob, String balance) {
        super();
        this.name = name;
        this.dob = dob;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public String getBalance() {
        return balance;
    }

    // üzerinde işlem yapılacak customer sınıfı tanımlanıyor
}
