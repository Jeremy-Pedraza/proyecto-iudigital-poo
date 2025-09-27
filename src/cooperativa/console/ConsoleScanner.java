package cooperativa.console;

import cooperativa.exceptions.ValidationException;
import cooperativa.validation.Validations;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Helpers de consola: piden datos, validan y repiten hasta que el usuario
 * ingrese algo correcto.
 */
public final class ConsoleScanner {
    private final Scanner sc;

    public ConsoleScanner(Scanner sc) {
        this.sc = sc;
    }

    public String pedirOpcionMenu() {
        while (true) {
            System.out.print("Selecciona: ");
            String raw = sc.nextLine();
            try {
                return Validations.opcionMenu(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public String pedirNombre() {
        while (true) {
            System.out.print("Nombre: ");
            String raw = sc.nextLine();
            try {
                return Validations.nombre(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public String pedirCedula() {
        while (true) {
            System.out.print("Cédula: ");
            String raw = sc.nextLine();
            try {
                return Validations.cedula(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public String pedirNumeroCuenta() {
        while (true) {
            System.out.print("Número de cuenta: ");
            String raw = sc.nextLine();
            try {
                return Validations.numeroCuenta(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public BigDecimal pedirMontoPositivo(String label) {
        while (true) {
            System.out.print(label + ": ");
            String raw = sc.nextLine();
            try {
                return Validations.montoPositivo(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    public BigDecimal pedirTasaAnual() {
        while (true) {
            System.out.print("Tasa de interés anual (ej. 1.5 para 1.5%): ");
            String raw = sc.nextLine();
            try {
                return Validations.tasaAnualPorcentaje(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }

    // Si necesitas leer un decimal cualquiera (permite negativos):
    public BigDecimal pedirDecimal(String label) {
        while (true) {
            System.out.print(label + ": ");
            String raw = sc.nextLine();
            try {
                return Validations.decimal(raw);
            } catch (ValidationException e) {
                System.out.println("! " + e.getMessage());
            }
        }
    }
}
