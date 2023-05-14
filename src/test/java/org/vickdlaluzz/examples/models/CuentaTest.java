package org.vickdlaluzz.examples.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.vickdlaluzz.examples.exception.NotEnoughBalanceException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    /*
    * Test deshabilitado
    */
    @Test
    @Disabled
    @DisplayName("Testing that the account name is returned")
    void testCuentaName() {
        Cuenta cuenta = new Cuenta("Victor", new BigDecimal("1000.1234"));
        String esperado = "Victor";
        String real = cuenta.getPerson();
        // Mensaje personalizado con expresion lamda
        // No se crea el string hasta que el test falla
        Assertions.assertEquals(esperado, real, () -> "El nombre de la cuenta no es el esperado");
    }

    @Test
    @DisplayName("Testing that the balance is returned")
    void testAccountBalance() {
        Cuenta cuenta = new Cuenta("Adolfo", new BigDecimal("332.23890232"));
        assertEquals(332.23890232, cuenta.getBalance().doubleValue(), () -> "El valor del saldo no es el esperado");
        assertFalse(cuenta.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Testing that the equals method is correctly overridden")
    void testAccountReference() {
        Cuenta johnDoe = new Cuenta("John Doe", new BigDecimal("9999.9999"));
        Cuenta johnDoe2 = new Cuenta("John Doe", new BigDecimal("9999.9999"));
        assertEquals(johnDoe2, johnDoe);
    }

    @Test
    @DisplayName("Testing that the amount is substracted when the debit operation is realized")
    void testDebitoAccount() {
        Cuenta cuenta = new Cuenta("Victor", new BigDecimal("99999.999"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getBalance());
        assertEquals(99899, cuenta.getBalance().intValue());
    }

    @Test
    @DisplayName("Testing that the amount is added when the credit operation is realized")
    void testCreditoAccount() {
        Cuenta cuenta = new Cuenta("Victor", new BigDecimal("99999.999"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getBalance());
        assertEquals(100099, cuenta.getBalance().intValue());
    }

    @Test
    @DisplayName("Testing that the exception is thrown when the balance is not enough")
    void testNotEnoughBalanceException() {
        Cuenta cuenta = new Cuenta("Victor", new BigDecimal("1.0"));
        // Testing exception
        Exception exception = assertThrows(NotEnoughBalanceException.class, () -> {
            cuenta.debito(new BigDecimal("2.0"));
        });
        String actualMessage = exception.getMessage();
        String expectedMessage = "Not enough balance";
        // Testing message
        assertEquals(actualMessage, expectedMessage);
    }


    @Test
    @DisplayName("Testing transfer method")
    void testTransfer() {
        Cuenta cuenta1 = new Cuenta("Victor", new BigDecimal("2000.00"));
        Cuenta cuenta2 = new Cuenta("Adolfo", new BigDecimal("1000.00"));
        Bank bank = new Bank();
        bank.setName("Santander");
        bank.transfer(cuenta1, cuenta2, new BigDecimal("1000.00"));
        assertEquals("1000.00", cuenta1.getBalance().toPlainString());
        assertEquals("2000.00", cuenta2.getBalance().toPlainString());
    }

    @Test
    @DisplayName("Testing transfer method and bank-account relationship")
    void testRelacionBancoCuenta() {
        Cuenta cuenta1 = new Cuenta("Victor", new BigDecimal("2000.00"));
        Cuenta cuenta2 = new Cuenta("Adolfo", new BigDecimal("1000.00"));
        Bank bank = new Bank();
        bank.addCuenta(cuenta1);
        bank.addCuenta(cuenta2);
        bank.setName("Santander");
        bank.transfer(cuenta1, cuenta2, new BigDecimal("1000.00"));
        // Agrupa un numero determinado de asserts
        assertAll(
                /*
                * Si no se agrupan los asserts solo salta un el primer error
                * y los demas asserts no se ejecutan
                * al agrupar asserts se ejecuta cada uno de ellos y muestra todos los
                * errores de la prueba.
                * */
                ()-> assertEquals("1000.00", cuenta1.getBalance().toPlainString(), () -> "El monto no se resto correctamente de la cuenta origen"),
                ()-> assertEquals("2000.00", cuenta2.getBalance().toPlainString(), () -> "El monto no se sumo correctamente a la cuenta destino"),
                ()-> assertEquals(2, bank.getCuentas().size(), () -> "No se asignaron las cuentas al banco correctamente"),
                ()-> assertEquals("Santander", cuenta1.getBank().getName(), () -> "No se asigno el banco a las cuentas correctamente")
        );
    }
}
