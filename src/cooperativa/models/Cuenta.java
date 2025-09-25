package cooperativa.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cooperativa.exceptions.SaldoInsuficienteException;
import cooperativa.transactions.Transaccion;

public abstract class Cuenta {

    private final String numeroCuenta;
    private BigDecimal saldo;
    private final LocalDateTime fechaApertura;
    private final List<Transaccion> transacciones;

    public abstract String getTipoCuenta();

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", tipoCuenta=" + getTipoCuenta() +
                '}';
    }

    public Cuenta(String numeroCuenta, BigDecimal saldoInicial, LocalDateTime fechaApertura) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede ser nulo o vacío.");
        }
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser nulo o negativo.");
        }

        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.fechaApertura = (fechaApertura != null) ? fechaApertura : LocalDateTime.now();
        this.transacciones = new ArrayList<>();
    }

    // UTILS => validadores y registradores
    protected void validarMontoPositivo(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
    }

    public void registrarTransaccion(Transaccion t) {
        if (t != null) {
            transacciones.add(t);
        }
    }

    // Getters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public List<Transaccion> getTransacciones() {
        return Collections.unmodifiableList(transacciones);
    }

    // Operaciones
    public void depositar(BigDecimal monto) {
        validarMontoPositivo(monto);
        this.saldo = this.saldo.add(monto);
    }

    public void retirar(BigDecimal monto) throws SaldoInsuficienteException {
        validarMontoPositivo(monto);
        if (saldo.compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro.");
        }
        this.saldo = this.saldo.subtract(monto);
    }
}
