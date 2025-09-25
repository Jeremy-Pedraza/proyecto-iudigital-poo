package cooperativa.core;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cooperativa.exceptions.CuentaDuplicadaExceptions;
import cooperativa.exceptions.CuentaNoEncontradaException;
import cooperativa.exceptions.SocioNoEncontradoException;

import cooperativa.models.Cuenta;
import cooperativa.models.Socio;
import cooperativa.models.CuentaAhorros;

import cooperativa.transactions.Transaccion;

public class Cooperativa {

    private final String nombre;
    private final String direccion;
    private final Map<String, Socio> socios;
    private final Map<String, Cuenta> cuentas;
    private final List<Transaccion> historialTransacciones;

    @Override
    public String toString() {
        return "Cooperativa{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", socios=" + socios.values() +
                ", cuentas=" + cuentas.values() +
                ", historialTransacciones=" + historialTransacciones +
                '}';
    }

    public Cooperativa(String nombre, String direccion) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la cooperativa no puede ser nulo o vacío.");
        }
        if (direccion == null || direccion.isEmpty()) {
            throw new IllegalArgumentException("La dirección de la cooperativa no puede ser nula o vacía.");
        }
        this.nombre = nombre;
        this.direccion = direccion;
        this.socios = new HashMap<>();
        this.cuentas = new HashMap<>();
        this.historialTransacciones = new ArrayList<>();
    }

    // Utils => validadores
    private void validarSocioNuevo(Socio socio) {
        if (socio == null) {
            throw new IllegalArgumentException("El socio no puede ser nulo.");
        }
        if (socios.containsKey(socio.getId())) {
            throw new IllegalArgumentException("El socio ya existe en la cooperativa.");
        }
    }

    // Métodos para manejar socios
    public void agregarSocio(Socio socio) {
        validarSocioNuevo(socio);
        socios.put(socio.getId(), socio);
    }

    public List<Socio> listarSocios() {
        return new ArrayList<>(socios.values());
    }

    public Socio buscarSocioPorCedula(String cedula) throws SocioNoEncontradoException {

            for (Socio socio : socios.values()) {
                if (socio.getCedula().equals(cedula)) {
                    return socio;
                }
            }
            throw new SocioNoEncontradoException("Socio con cédula " + cedula + " no encontrado.");


    }

    // Métodos para manejar cuentas
    public void agregarCuentaASocio(String cedula, Cuenta cuenta)
            throws SocioNoEncontradoException, CuentaDuplicadaExceptions {
        try {
            Socio socio = buscarSocioPorCedula(cedula);
            if (socio == null) {
                throw new SocioNoEncontradoException("Socio con cédula " + cedula + " no encontrado.");
            }

            if (cuentas.containsKey(cuenta.getNumeroCuenta())) {
                throw new CuentaDuplicadaExceptions("La cuenta ya existe en la cooperativa.");
            }
            // Validamos que la cuenta no esté repetida para el socio
            if (socio.buscarCuenta(cuenta.getNumeroCuenta()).isPresent()) {
                throw new CuentaDuplicadaExceptions("La cuenta ya existe para este socio.");
            }
            // Agregamos la cuenta al socio
            socio.agregarCuenta(cuenta);
            // Agregamos la cuenta al mapa de cuentas de la cooperativa
            cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Cuenta buscarCuentaPorNumero(String numeroCuenta) throws CuentaNoEncontradaException {
        try {
            if (numeroCuenta == null || numeroCuenta.isEmpty()) {
                throw new IllegalArgumentException("El número de cuenta no puede ser nulo o vacío.");
            }
            Cuenta cuenta = cuentas.get(numeroCuenta);
            if (cuenta == null) {
                throw new CuentaNoEncontradaException("Cuenta con número " + numeroCuenta + " no encontrada.");
            }
            return cuenta;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void ejecutarTransaccion(Transaccion transaccion) throws Exception {
        try {
            if (transaccion == null) {
                throw new IllegalArgumentException("La transacción no puede ser nula.");
            }
            transaccion.ejecutar();
            historialTransacciones.add(transaccion);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    // Métodos para reportes y estadísticas
    public Stream<String> listarNombresSocios() {
        return socios.values().stream().map(Socio::getNombre);
    }

    public Stream<Cuenta> listarCuentas(){
        return listarSocios().stream()
                .flatMap(socio -> socio.getCuentas().stream());
    }

    public List<Cuenta> cuentasConSaldoMayorA(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto debe ser mayor o igual a cero.");
        }
        return listarCuentas()
                .filter(cuenta -> cuenta.getSaldo().compareTo(monto) > 0)
                .collect(Collectors.toList());
    }

    public BigDecimal saldoTotalEnCuentas() {
        return listarCuentas()                
                .map(Cuenta::getSaldo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    
    public void aplicarInteresAnualCuentasAhorros() {
        listarCuentas()
                .filter(cuenta -> cuenta instanceof CuentaAhorros)
                .map(cuenta -> (CuentaAhorros) cuenta)
                .forEach(CuentaAhorros::aplicarInteresAnualCuentas);
    }

    // Getters
    public String getNombre() {
        return nombre;
    }
    public String getDireccion() {
        return direccion;
    }
    public List<Transaccion> getHistorialTransacciones() {
        return Collections.unmodifiableList(historialTransacciones);
    }
}