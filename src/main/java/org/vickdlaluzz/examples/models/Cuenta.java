package org.vickdlaluzz.examples.models;

import org.vickdlaluzz.examples.exception.NotEnoughBalanceException;

import java.math.BigDecimal;

public class Cuenta {
    private String person;
    private BigDecimal balance;
    private Bank bank;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Cuenta(String person, BigDecimal balance) {
        this.person = person;
        this.balance = balance;
    }
    public String getPerson() { return person; }
    public void setPerson(String person) { this.person = person; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public void debito(BigDecimal monto) {
        BigDecimal newBalance = this.balance.subtract(monto);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughBalanceException("Not enough balance");
        }

        this.balance = newBalance;
    }

    public void credito(BigDecimal monto) {
        this.balance = this.balance.add(monto);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cuenta)) {
            return false;
        }
        Cuenta c = (Cuenta) obj;
        if (this.person == null || this.balance == null) {
            return false;
        }

        return this.person.equals(c.getPerson()) && this.balance.equals(c.getBalance());
    }

}
