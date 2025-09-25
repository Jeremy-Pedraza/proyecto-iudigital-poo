package cooperativa.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Socio {

    private final String id;
    private final String nombre;
    private final String cedula;
    private final List<Cuenta> cuentas;

    @Override
    public String toString() {
        return "Socio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", cuentas=" + cuentas +
                '}';
    }

    public Socio(String nombre, String cedula) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede ser nula o vacía");
        }
        // Generamos un ID único para el socio
        this.id = java.util.UUID.randomUUID().toString();
        this.nombre = nombre;
        this.cedula = cedula;
        this.cuentas = new ArrayList<>();
    }

    // Utils
    protected void validarRepetida(Cuenta cuenta) {
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta().equals(cuenta.getNumeroCuenta())) {
                throw new IllegalArgumentException("La cuenta ya existe para este socio");
            }
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public List<Cuenta> getCuentas() {
        return Collections.unmodifiableList(cuentas);
    }

    public void agregarCuenta(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
        // Validamos que no este repetida, si es así, lanza excepción
        validarRepetida(cuenta);
        // Si pasa la validación, la agregamos
        cuentas.add(cuenta);
    }

    public Optional<Cuenta> buscarCuenta(String numeroCuenta) {
        return cuentas.stream()
                .filter(cuenta -> cuenta.getNumeroCuenta().equals(numeroCuenta))
                .findFirst();
    }

}
