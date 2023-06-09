package org.vickdlaluzz.examples.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.vickdlaluzz.examples.exception.NotEnoughBalanceException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/*
* @TestInstance(TestInstance.Lifecycle.PER_CLASS)
* @TestInstance(TestInstance.Lifecycle.PER_METHOD)
* */
class CuentaTest {
    Cuenta cuenta1;

    /*
     * @BeforeAll se ejecuta antes de la instancia de la clase test
     * */
    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test\nAdquiriendo recursos");
    }

    /*
     * @BeforeEach se ejecuta antes de cada metodo de test
     * */
    @BeforeEach
    void initMetodoTest() {
        System.out.println("Inicializando el metodo");
        cuenta1 = new Cuenta("Victor", new BigDecimal("2000.01"));
    }

    /*
     * @AfterAll se ejecuta despues de la instancia de la clase test
     * */
    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test\nCerrando recursos");
    }

    /*
     * @AfterAll se ejecuta despues de cada metodo de test
     * */
    @AfterEach
    void tearDown() {
        System.out.println("finalizando el metodo");
    }

    /*
    * Test deshabilitado
    */
    @Test
    @Disabled
    @DisplayName("Testing that the account name is returned")
    void testCuentaName() {

        String esperado = "Victor";
        String real = cuenta1.getPerson();
        // Mensaje personalizado con expresion lamda
        // No se crea el string hasta que el test falla
        Assertions.assertEquals(esperado, real, () -> "El nombre de la cuenta no es el esperado");
    }

    @Test
    @DisplayName("Testing that the balance is returned")
    void testAccountBalance() {
        assertEquals(2000.01, cuenta1.getBalance().doubleValue(), () -> "El valor del saldo no es el esperado");
        assertFalse(cuenta1.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta1.getBalance().compareTo(BigDecimal.ZERO) > 0);
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
        cuenta1.debito(new BigDecimal(100));
        assertNotNull(cuenta1.getBalance());
        assertEquals(1900, cuenta1.getBalance().intValue());
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
    
    /*
    * Test condicionales
    * */

    @Test
    @EnabledOnOs(OS.WINDOWS) // Solo se ejecuta dependiendo el sistema operativo
    void testSoloWindows() {
        System.out.println("Test ejecutado SOLO WINDOWS");
    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC}) // Solo se ejecuta dependiendo el sistema operativo
    void testSoloLinux() {
        System.out.println("Test ejecutado SOLO LINUX");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS) // No se ejecuta dependiendo el sistema operativo
    void testNoWindows() {
        System.out.println("Test ejecutado si no es windows");
    }

    @Test
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_17}) // Solo se ejecuta dependiendo del JRE
    void testOnlyJre() {
        System.out.println("Test ejecutado si es JRE 8  y 17");
    }

    @Test
    void genericTest() {
        Properties props = System.getProperties();
        props.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = "amd64") // Solo se ejecuta si cierta propiedad del sistema coincide con el valor
    void testIfProps() {
        System.out.println("os.arch == amd64");
    }

    @Test
    void printEnviromentVars() {
        System.getenv().forEach((k,v) -> {
            System.out.println(k + ": " + v);
        });
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "USERNAME", matches = "vickdlajluz")
    void testUserName() {
        System.out.println("Solo si USERNAME = vickdlaluz");
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "prod")
    void testEviromentProdDisabled() {
        System.out.println("Solo de desactiva si esta en produccion");
    }

    @Test
    @DisplayName("Testing the balance account with assumptions")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        /*
         * assumeTrue()
         * deshabilita el test si una condicion se cumple, a diferencia de las anotaciones
         * este metodo sirve para evaluar las condiciones de manera programatica
         * */
        assumeTrue(esDev);
        assertNotNull(cuenta1.getBalance());
    }

    @Test
    @DisplayName("Testing the balance account")
    void testSaldoCuentaWithAssumingThat() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        /*
         * assumingThat()
         * recibe como argumentos la condicion y un ejecutable (expresion lambda)
         * no salta toda la prueba, solo el bloque de codigo que esta dentro del lambda
         * */
        assumingThat(esDev, () -> {
            assertNotNull(cuenta1.getBalance());
        });
    }

    @Nested
    @DisplayName("Testing by O.S. conditions")
    class OperativeSystemTest {

        @Test
        @DisplayName("Testing if the OS is windows")
        @EnabledOnOs(OS.WINDOWS)
        void testIfWindows() {
            System.out.println("Tested only if the operative system is Windows");
        }

        @Test
        @DisplayName("Testing only in Linux")
        @EnabledOnOs(OS.LINUX)
        void testIfLinux() {
            System.out.println("Tested only if OS is linux");
        }

        @Test
        @DisplayName("Testing only if not Linux")
        @DisabledOnOs(OS.LINUX)
        void testIfNotLinux() {
            System.out.println("Tested only if OS is not linux");
        }
    }

    @Nested
    @DisplayName("Testing by JRE conditions")
    class JavaVersionTest {
        @Test
        @DisplayName("Test only if JAVA 8")
        @EnabledOnJre(JRE.JAVA_8)
        void testIfJVersion() {
            System.out.println("Only in Java 8");
        }

        @Test
        @DisplayName("Test only if not JAVA 8")
        @DisabledOnJre(JRE.JAVA_8)
        void testIfNotJVersion() {
            System.out.println("Only if Not Java 8");
        }


    }

    @Nested
    @DisplayName("Testing by System properties conditions")
    class SystemPropertiesTest {

        @Test
        void gettingSystemProps() {
            System.getProperties().forEach((k, v) -> {
                System.out.println(k + ": " + v);
            });
        }

        @Test
        @DisplayName("Test if os arch = amd64")
        @EnabledIfSystemProperty(named = "os.arch", matches = "amd64", disabledReason = "O.S. is not amd64 arch")
        void testIfSystemProp() {
        }

        @Test
        @DisplayName("Test if os arch is not amd64")
        @DisabledIfSystemProperty(named = "os.arch", matches = "amd64", disabledReason = "O.S. is amd64 arch")
        void testIfNotSystemProp() {
        }
    }

    @Nested
    class EviromentVariablesTest {
        @Test
        @DisplayName("Test if ENV = DEV")
        @EnabledIfEnvironmentVariable(named = "ENV", matches = "DEV")
        void testIfEnv() {

        }

        @Test
        @DisplayName("Test if ENV != DEV")
        @DisabledIfEnvironmentVariable(named = "ENV", matches = "DEV")
        void testIfNotEnv() {

        }
    }

    @RepeatedTest(value = 10, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")
    void testDebitoRepetitions(RepetitionInfo info) {
        if (info.getCurrentRepetition() == 3) {
            System.out.println("Esta es la tercera repeticion");
        }
    }


    @Tag("Parametrized")
    @Nested
    class ParametrizedTests {

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @DisplayName("Testing using value source")
        @ValueSource(strings = {
                "100",
                "200",
                "300",
                "500"
        })
        void testCreditAccount(String amount) {
            BigDecimal initBalance = cuenta1.getBalance();
            BigDecimal endBalance = cuenta1.getBalance().add(new BigDecimal(amount));
            cuenta1.credito(new BigDecimal(amount));
            assertEquals(endBalance, cuenta1.getBalance());

        }


        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @DisplayName("Testing using csv Source")
        @CsvSource({
                "1,100",
                "2,200",
                "3,300",
                "4,500"
        })
        void testCreditAccountWithCsv(String index, String amount) {
            System.out.println(index + " -> " + amount);
            BigDecimal initBalance = cuenta1.getBalance();
            BigDecimal endBalance = cuenta1.getBalance().add(new BigDecimal(amount));
            cuenta1.credito(new BigDecimal(amount));
            assertEquals(endBalance, cuenta1.getBalance());

        }

        @ParameterizedTest()
        @DisplayName("Testing using a csv file")
        @CsvFileSource(resources = "/data.csv")
        void testWithCsvFile(String index, String name, String initBalance) {
            Cuenta cuenta = new Cuenta(name,new BigDecimal(initBalance));
            assertEquals(new BigDecimal(initBalance), cuenta.getBalance());
        }


    }

    @Tag("Parametrized")
    @ParameterizedTest()
    @DisplayName("Testing using a method")
    @MethodSource("getMontoList")
    void testWithMethod(String monto) {
        cuenta1.credito(new BigDecimal(monto));
    }

    static List<String> getMontoList() {
        return Arrays.asList("100","200","300");
    }

    @Nested
    @Disabled
    class TimeOutTests {
        @Test
        @Timeout(1)
        void testTimeOut() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        @Timeout(value= 500, unit = TimeUnit.MILLISECONDS)
        void testTimeOut2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(500);
        }

        @Test
        void testTimeOutAssertions() {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.MILLISECONDS.sleep(5000);
            });
        }
    }

}
