package cooperativa.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.RoundingMode;

public class CuentaAhorros extends Cuenta {

    private final BigDecimal tasaInteresAnual;

    @Override
    public String getTipoCuenta() {
        return "AHORROS";
    }

    @Override
    public String toString() {
        return "CuentaAhorros{" +
                "numeroCuenta='" + getNumeroCuenta() + '\'' +
                ", saldo=" + getSaldo() +
                ", fechaApertura=" + getFechaApertura() +
                ", tasaInteresAnual=" + tasaInteresAnual +
                '}';
    }

    public CuentaAhorros(String numeroCuenta, BigDecimal saldoInicial, LocalDateTime fechaApertura,
            BigDecimal tasaInteresAnual) {

        super(numeroCuenta, saldoInicial, fechaApertura);

        if (tasaInteresAnual == null || tasaInteresAnual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La tasa de interés anual no puede ser nula o negativa.");
        }

        this.tasaInteresAnual = tasaInteresAnual;
    }

    // Método para calcular y aplicar intereses anuales
    public void aplicarInteresAnualCuentas() {

        // Fórmula: Interés Anual = Saldo * Tasa Anual / 100
        BigDecimal interesAnual = getSaldo()
                .multiply(tasaInteresAnual)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        // Actualizamos el saldo
        depositar(interesAnual);
        //

    }

    // Getters and Setters
    public BigDecimal getTasaInteresAnual() {
        return tasaInteresAnual;
    }

}