package cooperativa.transactions;

import java.math.BigDecimal;

public interface Transaccion {
    void ejecutar() throws Exception;

    BigDecimal getMonto();
}