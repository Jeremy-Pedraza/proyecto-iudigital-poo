package cooperativa.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cooperativa.exceptions.SaldoInsuficienteException;
import cooperativa.models.Cuenta;

public class Retiro implements Transaccion {

    private final Cuenta cuenta;
    private final BigDecimal monto;
    private final LocalDateTime fecha;

    @Override
    public String toString() {
        return "Retiro{" +
                "cuenta=" + cuenta.getNumeroCuenta() +
                ", monto=" + monto +
                ", fecha=" + fecha +
                '}';
    }

    public Retiro(Cuenta cuenta, BigDecimal monto) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        this.cuenta = cuenta;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
    }

    @Override
    public void ejecutar() throws SaldoInsuficienteException {
        cuenta.retirar(monto);
        cuenta.registrarTransaccion(this);
    }

    @Override
    public BigDecimal getMonto() {
        return monto;
    }

    // Getters
    public Cuenta getCuenta() {
        return cuenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}