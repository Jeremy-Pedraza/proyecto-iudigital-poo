<h1>CoopRKC — Prototipo de Consola (POO + Programación Funcional en Java)</h1>

<blockquote>
  <p><strong>S25 - Evidencia de Aprendizaje · Unidad 2: Programación Funcional en Java</strong><br>
  <strong>Actividad 1 — Grupo 42</strong><br>
  Estiven Andrés Trujillo Montiel · Jeremy Iván Pedraza Hernández · Hahiler Esteban Guevara Estrada<br>
  <strong>Docente:</strong> Ramiro Antonio Giraldo Escobar<br>
  <strong>Curso:</strong> Programación Orientada a Objetos II — PREICA2502B010090<br>
  <strong>Programa:</strong> Ingeniería de Software y Datos — Facultad de Ingeniería y Ciencias Agropecuarias<br>
  <strong>Institución Universitaria Digital de Antioquia</strong> — <strong>2025</strong></p>
</blockquote>

<hr>

<h2>📌 Descripción</h2>
<p>Aplicación <strong>100% consola</strong> que modela una cooperativa de ahorro y crédito. Permite:</p>
<ul>
  <li>Registrar socios y abrir <strong>cuentas de ahorros</strong>.</li>
  <li>Ejecutar <strong>depósitos</strong> y <strong>retiros</strong> (con validación de saldo).</li>
  <li><strong>Registrar transacciones</strong> por cuenta y a nivel global.</li>
  <li>Aplicar <strong>interés</strong> a todas las cuentas de ahorro.</li>
  <li>Usar <strong>programación funcional</strong> (lambdas, streams y method references) para listar, filtrar y reducir datos.</li>
</ul>

<h2>🧱 Arquitectura (paquetes y responsabilidades)</h2>
<pre>
src/
└─ cooperativa/
   ├─ app/
   │  └─ Main.java                   # Punto de entrada (menú CLI + flujos)
   ├─ core/
   │  └─ Cooperativa.java            # Orquestador: socios, cuentas, transacciones, PF
   ├─ exceptions/
   │  ├─ CuentaDuplicadaException.java
   │  ├─ CuentaNoEncontradaException.java
   │  ├─ SaldoInsuficienteException.java
   │  └─ SocioNoEncontradoException.java
   ├─ models/
   │  ├─ Cuenta.java                 # Clase abstracta (número, saldo, apertura, historial)
   │  ├─ CuentaAhorros.java          # Subclase: interés anual + aplicar interés
   │  └─ Socio.java                  # Socio: nombre, cédula, cuentas
   └─ transactions/
      ├─ Transaccion.java            # Interfaz: ejecutar() y getMonto()
      ├─ Deposito.java               # Implementación de Transaccion
      └─ Retiro.java                 # Implementación de Transaccion
</pre>

<h3>Pilares de POO aplicados</h3>
<ul>
  <li><strong>Abstracción &amp; Encapsulamiento:</strong> <code>Cuenta</code> (campos privados, API pública controlada).</li>
  <li><strong>Herencia:</strong> <code>CuentaAhorros extends Cuenta</code>.</li>
  <li><strong>Polimorfismo:</strong> <code>Transaccion</code> (interfaz) y sus implementaciones <code>Deposito</code>/<code>Retiro</code>.</li>
</ul>

<h2>⚙️ Funcionalidades clave</h2>
<ul>
  <li><strong>Socios:</strong> alta, búsqueda por cédula, listado.</li>
  <li><strong>Cuentas:</strong> alta validando <em>unicidad</em> global y por socio.</li>
  <li><strong>Transacciones:</strong> ejecución polimórfica, historial global y por cuenta.</li>
  <li><strong>Intereses:</strong> aplicar interés anual a todas las <code>CuentaAhorros</code>.</li>
  <li><strong>Programación Funcional:</strong> 
    <ul>
      <li><code>map + forEach</code> → listar nombres de socios.</li>
      <li><code>filter + collect</code> → cuentas con saldo &gt; monto.</li>
      <li><code>reduce</code> → total de dinero en cooperativa.</li>
    </ul>
  </li>
</ul>

<h2>🧪 Gestión de errores</h2>
<ul>
  <li><strong>Retiros:</strong> <code>SaldoInsuficienteException</code> si no hay fondos.</li>
  <li><strong>Cuentas duplicadas:</strong> <code>CuentaDuplicadaException</code> (global o por socio).</li>
  <li><strong>Cuenta inexistente:</strong> <code>CuentaNoEncontradaException</code>.</li>
  <li><strong>Socio inexistente:</strong> <code>SocioNoEncontradoException</code>.</li>
  <li><strong>Validaciones:</strong> entradas no nulas/ni vacías, montos positivos (<code>IllegalArgumentException</code>).</li>
</ul>
<p><em>Las excepciones se lanzan en dominio y se manejan en <code>Main</code> (capa de UI consola).</em></p>

<h2>🧩 Fragmentos destacados</h2>

<h3>1) Impresión del encabezado del trabajo e integrantes</h3>
<p>Agregar en <code>Main.java</code> y llamar al inicio de <code>main</code>:</p>
<pre><code class="language-java">private static void imprimirEncabezado() {
    String header = """
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
            """;
    System.out.println(header);
}

// en main():
public static void main(String[] args) {
    imprimirEncabezado();
    // ... resto del flujo de menú
}
</code></pre>

<h3>2) Streams (PF) en la clase <code>Cooperativa</code></h3>
<pre><code class="language-java">public Stream&lt;String&gt; listarNombresSocios() { 
    return socios.values().stream().map(Socio::getNombre); 
}

public List&lt;Cuenta&gt; cuentasConSaldoMayorA(BigDecimal monto) {
    return listarCuentas()
        .filter(c -&gt; c.getSaldo().compareTo(monto) &gt; 0)
        .collect(Collectors.toList());
}

public BigDecimal saldoTotalEnCuentas() {
    return listarCuentas().map(Cuenta::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);
}
</code></pre>

<h3>3) Transacciones polimórficas</h3>
<pre><code class="language-java">Transaccion dep = new Deposito(cuenta, new BigDecimal("100000"));
coop.ejecutarTransaccion(dep);

Transaccion ret = new Retiro(cuenta, new BigDecimal("50000"));
try {
    coop.ejecutarTransaccion(ret);
} catch (Exception e) {
    System.out.println("No se pudo retirar: " + e.getMessage());
}
</code></pre>

<h2>▶️ Ejecución (sin Maven)</h2>
<p><strong>Requisitos:</strong> Java 17+ (recomendado; text blocks desde Java 15)</p>

<h3>Linux / macOS</h3>
<pre><code>javac -d out $(find src -name "*.java")
java -cp out cooperativa.app.Main
</code></pre>

<h3>Windows (PowerShell)</h3>
<pre><code>javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp out cooperativa.app.Main
</code></pre>

<h2>🧭 Flujo desde consola </h2>
<ol>
  <li>Registrar socio</li>
  <li>Abrir cuenta de ahorros</li>
  <li>Depósito</li>
  <li>Retiro</li>
  <li>Listar nombres de socios</li>
  <li>Cuentas con saldo &gt; 500000 </li>
  <li>Total dinero en cooperativa </li>
  <li>Aplicar interés anual a cuentas de ahorro</li>
  <li>Ver historial global de transacciones</li>
  <li>Salir</li>
</ol>

<h2>✅ Checklist del enunciado</h2>
<ul>
  <li>[x] POO: abstracción, encapsulamiento, herencia, polimorfismo</li>
  <li>[x] Paquetes organizados (<code>app</code>, <code>core</code>, <code>models</code>, <code>transactions</code>, <code>exceptions</code>)</li>
  <li>[x] Programación funcional: <code>map</code>, <code>filter</code>, <code>reduce</code>, method references</li>
  <li>[x] Gestión de errores (retiros, cuentas duplicadas, búsquedas)</li>
  <li>[x] Registro de transacciones por cuenta y global</li>
  <li>[x] Opción de aplicar interés a cuentas de ahorro</li>
  <li>[x] Encabezado del trabajo (integrantes + docente) impreso en consola</li>
</ul>

<h2>🧹 Convenciones y decisiones</h2>
<ul>
  <li><code>BigDecimal</code> para dinero.</li>
  <li>Validaciones en constructores y métodos públicos.</li>
  <li><code>getTransacciones()</code> devuelve lista inmutable.</li>
  <li>Sin logging ni persistencia (prototipo académico de consola).</li>
</ul>

<h2>📄 Licencia</h2>
<p>Uso académico. Ajusta a la licencia preferida por el equipo (MIT/Apache-2.0).</p>
