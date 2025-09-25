package cooperativa.app;

import cooperativa.core.Cooperativa;
import cooperativa.exceptions.CuentaDuplicadaExceptions;
import cooperativa.exceptions.CuentaNoEncontradaException;
import cooperativa.exceptions.SocioNoEncontradoException;
import cooperativa.models.Cuenta;
import cooperativa.models.CuentaAhorros;
import cooperativa.models.Socio;
import cooperativa.transactions.Deposito;
import cooperativa.transactions.Retiro;
import cooperativa.transactions.Transaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Cooperativa COOPERATIVA = new Cooperativa("CoopRKC", "Calle PapiQuiero Piña");

    public static void main(String[] args) {
        // Booleano para controlar el bucle principal
        boolean seguir = true;
        while (seguir) {
            imprimirEncabezado();
            mostrarMenu();
            String operacion = SCANNER.nextLine().trim();
            try {
                switch (operacion) {
                    case "1" -> registrarSocio();
                    case "2" -> abrirCuentaAhorros();
                    case "3" -> depositar();
                    case "4" -> retirar();
                    case "5" -> listarNombresSocios();
                    case "6" -> cuentasSaldoMayor();
                    case "7" -> totalDinero();
                    case "8" -> aplicarInteres();
                    case "9" -> verHistorial();
                    case "0" -> seguir = false;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception error) {
                System.out.println("⚠️ Error: " + error.getMessage());
            }
            if (seguir) {
                System.out.println("\nPresiona ENTER para continuar...");
                SCANNER.nextLine();
            }
        }
        System.out.println("¡Hasta luego!");
    }

    private static void imprimirEncabezado() {
        String header = """
            ================================================================================
                S25 - Evidencia de Aprendizaje Unidad 2: Programación Funcional en Java

                Integrantes

                Actividad 1 Grupo 42
                Estiven Andrés Trujillo Montiel
                Jeremy Iván Pedraza Hernández
                Hahiler Esteban Guevara Estrada

                Docente
                Ramiro Antonio Giraldo Escobar

                Programación Orientada a Objetos II
                - PREICA2502B010090
                Ingeniería de Software y Datos
                Facultad de Ingeniería y Ciencias Agropecuarias
                Institución Universitaria Digital de Antioquia.
                2025
            ================================================================================
                """;
        System.out.println(header);
    }

    private static void mostrarMenu() {
        System.out.println("=== CoopRKC - Cooperativa MultiActiva ===");
        System.out.println("1. Registrar socio");
        System.out.println("2. Abrir cuenta de ahorros");
        System.out.println("3. Depósito");
        System.out.println("4. Retiro");
        System.out.println("5. Listar nombres de socios");
        System.out.println("6. Cuentas con saldo > 500000");
        System.out.println("7. Total dinero en cooperativa");
        System.out.println("8. Aplicar interés anual a cuentas de ahorro");
        System.out.println("9. Ver historial global de transacciones");
        System.out.println("0. Salir");
        System.out.print("Selecciona: ");
    }

    // 1) Registrar socio
    private static void registrarSocio() {
        System.out.print("Nombre: ");
        String nombre = SCANNER.nextLine().trim();
        System.out.print("Cédula: ");
        String cedula = SCANNER.nextLine().trim();
        COOPERATIVA.agregarSocio(new Socio(nombre, cedula));
        System.out.println("Socio registrado.");
    }

    // 2) Abrir cuenta de ahorros
    private static void abrirCuentaAhorros() throws SocioNoEncontradoException, CuentaDuplicadaExceptions {
        System.out.print("Cédula del socio: ");
        String cedula = SCANNER.nextLine().trim();
        System.out.print("Número de cuenta: ");
        String numero = SCANNER.nextLine().trim();
        System.out.print("Saldo inicial: ");
        BigDecimal saldoInicial = leerNumeroDecimal();
        System.out.print("Tasa de interés anual (ejemplo. 1.5 para 1.5%): ");
        BigDecimal tasaAnual = leerNumeroDecimal();

        CuentaAhorros cuentaAhorros = new CuentaAhorros(numero, saldoInicial, LocalDateTime.now(), tasaAnual);
        COOPERATIVA.agregarCuentaASocio(cedula, cuentaAhorros);
        System.out.println("Cuenta de ahorros abierta.");
    }

    // 3) Depósito
    private static void depositar() throws CuentaNoEncontradaException, Exception {
        System.out.print("Número de cuenta: ");
        String numero = SCANNER.nextLine().trim();
        System.out.print("Monto a depositar: ");
        BigDecimal monto = leerNumeroDecimal();

        Cuenta cuenta = COOPERATIVA.buscarCuentaPorNumero(numero);
        Transaccion deposito = new Deposito(cuenta, monto);
        COOPERATIVA.ejecutarTransaccion(deposito); // ejecuta y guarda en historial
        System.out.println("Depósito realizado. Saldo: " + cuenta.getSaldo());
    }

    // 4) Retiro (con manejo de errores de saldo insuficiente)
    private static void retirar() throws CuentaNoEncontradaException {
        System.out.print("Número de cuenta: ");
        String numero = SCANNER.nextLine().trim();
        System.out.print("Monto a retirar: ");
        BigDecimal monto = leerNumeroDecimal();

        Cuenta cuenta = COOPERATIVA.buscarCuentaPorNumero(numero);
        Transaccion retiro = new Retiro(cuenta, monto);
        try {
            COOPERATIVA.ejecutarTransaccion(retiro);
            System.out.println("Retiro realizado. Saldo: " + cuenta.getSaldo());
        } catch (Exception e) {
            System.out.println("No se pudo retirar: " + e.getMessage());
        }
    }

    // 5) Listar nombres de socios (streams: map + forEach con method reference)
    private static void listarNombresSocios() {
        System.out.println("\n * Nombres de socios *");
        COOPERATIVA.listarNombresSocios().forEach(System.out::println);
    }

    // 6) Filtrar cuentas con saldo > 500000
    private static void cuentasSaldoMayor() {
        System.out.println("\n * Cuentas con saldo > 500000 *");
        List<Cuenta> lista = COOPERATIVA.cuentasConSaldoMayorA(new BigDecimal("500000"));
        if (lista.isEmpty())
            System.out.println("(sin resultados)");
        else
            lista.forEach(System.out::println);
    }

    // 7) Total de dinero (reduce)
    private static void totalDinero() {
        System.out.println("\n * Total dinero en la cooperativa *");
        System.out.println(COOPERATIVA.saldoTotalEnCuentas());
    }

    // 8) Aplicar interés a todas las cuentas de ahorro
    private static void aplicarInteres() {
        COOPERATIVA.aplicarInteresAnualCuentasAhorros();
        System.out.println("Interés aplicado.");
    }

    // 9) Ver historial global de transacciones
    private static void verHistorial() {
        System.out.println("\n * Historial global de transacciones *");
        var historial = COOPERATIVA.getHistorialTransacciones();
        if (historial.isEmpty())
            System.out.println("(Sin transacciones)");
        else
            historial.forEach(System.out::println);
    }

    // Utilidad: lectura robusta de Decimales (admite coma o punto)
    private static BigDecimal leerNumeroDecimal() {
        String valor = SCANNER.nextLine().trim().replace(",", ".");
        return new BigDecimal(valor);
    }
}
