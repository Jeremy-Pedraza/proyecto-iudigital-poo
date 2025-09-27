package cooperativa.validation;

import java.math.BigDecimal;

import cooperativa.exceptions.ValidationException;

public final class Validations {
    private Validations() {
    }

    // Nombre: no vacío, sin solo espacios, normalizado
    public static String nombre(String raw) {
        String v = normalize(raw);
        if (v.isEmpty())
            throw new ValidationException("El nombre es requerido.");
        if (v.length() > 80)
            throw new ValidationException("El nombre es demasiado largo (máx. 80).");
        if (v.matches(".*\\d.*"))
            throw new ValidationException("El nombre no debe contener números.");
        // Normaliza espacios intermedios (varios espacios -> 1 espacio)
        v = v.replaceAll("\\s+", " ").trim();
        return v;
    }

    // Cédula: solo dígitos, longitud razonable (ajusta a tu realidad)
    public static String cedula(String raw) {
        String v = normalize(raw);
        if (!v.matches("\\d{6,12}"))
            throw new ValidationException("Cédula inválida. Debe tener solo dígitos (6 a 12).");
        return v;
    }

    // Número de cuenta: letras/números, 6-20 chars (ajusta si necesitas solo
    // dígitos)
    public static String numeroCuenta(String raw) {
        String v = normalize(raw);
        // Solo dígitos, largo entre 6 y 20
        if (!v.matches("\\d{6,20}"))
            throw new ValidationException("Número de cuenta inválido. Debe tener solo dígitos (6 a 20).");
        return v; // ya no transformamos a mayúsculas, pues solo hay dígitos
    }

    // Decimal genérico (acepta coma o punto). No acepta letras
    public static BigDecimal decimal(String raw) {
        String v = normalize(raw);

        if (!v.matches("-?\\d+(?:[\\.,]\\d+)?"))
            throw new ValidationException("Número inválido. Usa solo dígitos (opcional decimal con . o ,).");

        v = v.replace(",", ".");
        try {
            return new BigDecimal(v);
        } catch (NumberFormatException e) {
            throw new ValidationException("Número inválido.");
        }
    }

    // Monto positivo (> 0)
    public static BigDecimal montoPositivo(String raw) {
        String v = normalize(raw);
        if (!v.matches("^\\d+(?:[\\.,]\\d+)?$"))
            throw new ValidationException("Monto inválido. Usa solo dígitos y un separador decimal opcional (.,).");

        BigDecimal n = new BigDecimal(v.replace(",", "."));
        if (n.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("El monto debe ser mayor que 0.");
        return n;
    }

    // Tasa anual en porcentaje: 0 <= tasa <= 100 (se ingresa como 1.5 = 1.5%)
    public static BigDecimal tasaAnualPorcentaje(String raw) {
        BigDecimal t = decimal(raw);
        if (t.compareTo(BigDecimal.ZERO) < 0 || t.compareTo(new BigDecimal("100")) > 0)
            throw new ValidationException("La tasa debe estar entre 0 y 100.");
        return t;
    }

    // Opción de menú: 0..9
    public static String opcionMenu(String raw) {
        String v = normalize(raw);
        if (!v.matches("^[0-9]$")) // solo un carácter numérico
            throw new ValidationException("Opción inválida. Usa solo un dígito (0-9).");
        return v;
    }

    private static String normalize(String s) {
        return s == null ? "" : s.trim();
    }
}
