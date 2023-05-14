package org.vickdlaluzz.examples.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private List<Cuenta> cuentas;

    public Bank() {
        this.cuentas = new ArrayList<Cuenta>();
    }

    public List<Cuenta> getCuentas() {

        return cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        cuenta.setBank(this);
        cuentas.add(cuenta);
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = (List<Cuenta>) cuentas.stream().map(cuenta -> {
            cuenta.setBank(this);
            return cuenta;
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void transfer(Cuenta origen, Cuenta destino, BigDecimal amount) {
        origen.debito(amount);
        destino.credito(amount);
    }
}
