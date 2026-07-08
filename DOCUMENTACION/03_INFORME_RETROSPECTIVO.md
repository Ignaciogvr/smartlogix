# Informe Retrospectivo — SmartLogix
## DSY1106 Desarrollo Fullstack III — Evaluación Final Transversal

---

## A. Diseño de la Arquitectura de Microservicios

### Respuesta a los requerimientos del cliente

SmartLogix es una plataforma de comercio electrónico que requería gestionar de forma independiente los dominios de **usuarios, inventario, pedidos y logística**. La arquitectura de microservicios fue la decisión más adecuada porque cada dominio tiene distintos volúmenes de tráfico, modelos de datos y requerimientos de escalabilidad.

Se implementaron 5 servicios especializados:

| Servicio | Puerto | Dominio |
|---|---|---|
| BFF (Backend for Frontend) | 8080 | Orquestación y agregación |
| Usuarios Service | 8081 | Autenticación y perfiles |
| Pedidos Service | 8082 | Carrito, checkout y órdenes |
| Inventory Service | 8083 | Productos y stock |
| Envío Service | 8084 | Logística y tracking |

### Patrones arquitectónicos aplicados

**BFF (Backend for Frontend):** El frontend Angular no se comunica directamente con los microservicios sino a través del BFF, que agrega, transforma y simplifica las respuestas. Esto desacopla el frontend de los contratos internos de los servicios.

**Database per Service:** Cada microservicio tiene su propia base de datos PostgreSQL. Esto garantiza autonomía de datos, permite escalar cada base de datos de forma independiente y evita el acoplamiento entre servicios a nivel de datos.

**Event-Driven con Outbox Pattern:** La comunicación asíncrona entre servicios se realiza mediante Apache Kafka. Se usa el patrón Outbox para garantizar que los eventos se publiquen incluso ante fallos de red: el evento se guarda en la misma transacción de base de datos que la entidad, y un scheduler lo publica a Kafka.

**Circuit Breaker (Resilience4J):** El BFF implementa Circuit Breakers para cada microservicio, evitando que un fallo en un servicio downstream provoque cascada de fallos hacia el frontend.

### Alineación con principios éticos y de sostenibilidad

- **Seguridad y privacidad:** Auth0 gestiona la autenticación. Los tokens JWT son validados en cada servicio. Los datos sensibles de usuarios no se replican entre servicios, solo se comparte el identificador.
- **Escalabilidad sostenible:** La arquitectura permite escalar solo los servicios con mayor demanda (ej. Inventory en momentos de alta concurrencia), reduciendo el uso innecesario de recursos.
- **Responsabilidad en los datos:** Cada servicio es dueño exclusivo de sus datos, cumpliendo con el principio de minimización de datos.

---

## B. Decisiones en el Desarrollo de Componentes Backend y Frontend

### Backend — Decisiones técnicas

**Spring Boot 3.x como base:** Se eligió por su ecosistema maduro, integración nativa con Kafka, JPA, OAuth2 y Actuator. La versión 3.x requiere Java 17+, lo que habilitó el uso de records y otras características modernas.

**WebFlux en el BFF:** El BFF usa programación reactiva (WebFlux + WebClient) para realizar llamadas paralelas a los microservicios downstream, reduciendo la latencia de las respuestas agregadas.

**Auth0 para autenticación:** Se optó por Auth0 como proveedor de identidad porque elimina la necesidad de implementar y mantener un servidor de autenticación propio. Los tokens JWT con scopes y roles se validan en cada servicio.

**Flyway para migraciones:** Cada servicio tiene su historial de migraciones versionadas (V1, V2, V3), lo que permite reproducir el estado exacto de la base de datos en cualquier entorno.

**Ajustes identificados durante el desarrollo:**
- Inicialmente el BFF intentaba comunicarse con los microservicios de forma síncrona para todo. Se identificó que para operaciones de solo lectura del catálogo (pública, sin autenticación) conviene cachear en el BFF.
- El endpoint `/api/inventory/{id}` en Envíos apuntaba a una ruta incorrecta. Se corrigió a `/productos/stock/{id}` al hacer la auditoría de clientes inter-servicios.

### Frontend — Decisiones técnicas

**Angular 17+ con SSR (Server Side Rendering):** Se eligió Angular por su arquitectura orientada a componentes y su integración nativa con TypeScript. El SSR mejora el SEO y la velocidad de carga inicial.

**Arquitectura por features:** El frontend está organizado por dominio de negocio (`client/`, `vendedor/`, `admin/`), lo que facilita la asignación de trabajo por equipo y el mantenimiento futuro.

**Auth0 Angular SDK:** La autenticación en el frontend utiliza el SDK oficial de Auth0, que maneja automáticamente el flujo OAuth2, la renovación de tokens y el almacenamiento seguro.

**Ajustes identificados:**
- Se implementó un `ConfirmService` para modales de confirmación reutilizables, evitando duplicación de lógica.
- Los pipes `currency.pipe.ts` y `estado.pipe.ts` se extrajeron como pipes compartidos para estandarizar la presentación de precios y estados de pedidos en toda la aplicación.

---

## C. Aplicación de Patrones de Diseño

### Patrones en el Backend

**Strategy Pattern — Cálculo de costos de envío:**
El servicio de envíos implementa distintas estrategias de cotización dependiendo del tipo de envío (express, normal, económico). Cada estrategia encapsula su algoritmo de cálculo.

**Repository Pattern:**
Todos los servicios usan el patrón Repository a través de Spring Data JPA. Los repositorios encapsulan la lógica de acceso a datos, permitiendo cambiar la implementación de persistencia sin afectar la lógica de negocio.

**Outbox Pattern:**
Garantiza la consistencia eventual. En la misma transacción en que se crea/modifica una entidad, se registra un evento en la tabla `outbox_events`. Un scheduler independiente publica los eventos pendientes a Kafka.

```java
@Transactional
public Pedido crearPedido(PedidoRequest request) {
    Pedido pedido = pedidoRepository.save(nuevoPedido);  // Persistir entidad
    outboxRepository.save(buildEvent(pedido));           // Registrar evento
    return pedido;                                        // Atómica: ambos o ninguno
}
```

**Circuit Breaker Pattern:**
Implementado en el BFF con Resilience4J. Si un microservicio falla repetidamente, el circuito se abre y se ejecuta el método fallback, devolviendo datos por defecto en lugar de propagar el error.

```java
@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackProductos")
public List<ProductoDTO> getProductos() {
    return inventoryClient.findAll();
}
public List<ProductoDTO> fallbackProductos(Exception e) {
    return Collections.emptyList(); // Graceful degradation
}
```

**Builder Pattern — DTOs:**
Los DTOs complejos de respuesta (como `EstadoCompletoResponse`) se construyen con el patrón Builder para mejorar la legibilidad y evitar constructores con múltiples parámetros.

### Patrones en el Frontend

**Service Layer Pattern:**
Toda la comunicación con el BFF se centraliza en servicios Angular (`ProductoService`, `PedidoService`, etc.). Los componentes solo consumen datos de los servicios, sin lógica de HTTP.

**Observer Pattern (RxJS):**
Angular usa Observables de RxJS para manejar datos asíncronos. Los componentes se suscriben a los Observables de los servicios para reaccionar a cambios de datos.

**Pipe Pattern:**
Los pipes `currency.pipe.ts` y `estado.pipe.ts` transforman datos en la capa de presentación sin modificar los datos originales.

### Impacto en la solución

Los patrones aplicados contribuyeron a:
- **Modularidad:** Cada componente/servicio tiene una responsabilidad única
- **Reutilización:** Los pipes y servicios compartidos evitan duplicación
- **Testabilidad:** El desacoplamiento facilita el testing unitario con mocks
- **Mantenibilidad:** Los cambios en una capa no afectan otras

---

## D. Estrategia de Branching y Gestión de Versiones

### Estrategia implementada: Feature Branch Workflow

Se adoptó una estrategia de Feature Branches alineada con Git Flow simplificado:

```
main
  └── develop
        ├── feature/bff-service        (Ignaciogvr)
        ├── feature/usuarios-service   (Ignaciogvr)
        ├── feature/pedidos-service    (Ignaciogvr)
        ├── feature/inventory-service  (Ignaciogvr)
        ├── feature/envio-service      (Ignaciogvr)
        └── feature/frontend           (Ignaciogvr)
```

### Descripción del flujo

1. **`main`:** Rama de producción. Solo recibe merges desde `develop` cuando el código está estable y listo para entrega.
2. **`develop`:** Rama de integración. Aquí converge el trabajo de todos los integrantes. Se usa para verificar que los servicios funcionan juntos.
3. **`feature/*`:** Cada microservicio y el frontend tienen su propia rama de feature. El desarrollo de cada servicio se realiza de forma aislada hasta estar listo para integrar.

### Impacto en la organización del equipo

- **Trabajo paralelo sin conflictos:** Cada integrante trabajó en su rama de feature sin interferir en el trabajo de los demás.
- **Integración controlada:** Al mergear a `develop`, se verificaba que los servicios se comunicaran correctamente.
- **Historial claro:** Los commits descriptivos (`feat:`, `fix:`, `chore:`, `refactor:`) permiten entender qué cambió y por qué en cada momento.

### Convención de commits (Conventional Commits)

```
feat(bff-service): implementación del endpoint de checkout
fix(inventory): corregir ruta de stock en cliente de envíos
refactor(pedidos): extraer lógica de carrito a CarritoService
chore: limpieza del repositorio para entrega EFT
```

### Buenas prácticas aplicadas

- No se hizo commit directamente a `main`
- Cada feature branch tiene commits atómicos y descriptivos
- El `.gitignore` excluye archivos de compilación (`target/`), dependencias (`node_modules/`), caches de IDE y archivos de configuración local

---

## E. Integración de Componentes Backend, Frontend y Base de Datos

### Flujo completo de integración

```
Angular Frontend
      │
      │ HTTP + Bearer Token (Auth0 JWT)
      ▼
BFF Service (8080) — Punto único de entrada
      │
      ├──► Usuarios Service (8081) ──► PostgreSQL smartlogix_usuarios
      ├──► Pedidos Service  (8082) ──► PostgreSQL smartlogix_pedidos
      ├──► Inventory Service (8083) ──► PostgreSQL smartlogix_inventory
      └──► Envío Service   (8084) ──► PostgreSQL smartlogix_envio
                │                              │
                └──────────► Apache Kafka ◄────┘
                              (Eventos entre servicios)
```

### Caso de uso: Flujo de compra completo

1. **Frontend:** Usuario agrega productos al carrito → llama `POST /api/carrito/items`
2. **BFF:** Autentica el token, reenvía a Pedidos Service
3. **Pedidos Service:** Reserva stock en Inventory Service (REST síncrono), crea el pedido en su BD
4. **Outbox Scheduler:** Publica evento `PedidoCreado` a Kafka
5. **Envío Service:** Consume el evento Kafka, crea el envío automáticamente y asigna chofer
6. **Frontend:** Polling o notificación del estado del envío via `GET /api/envios/tracking/{codigo}`

### Desafíos de integración y cómo se resolvieron

**Problema 1 — Autenticación cruzada:** Los microservicios necesitan validar el token JWT del usuario pero también llamarse entre sí. Se resolvió con tokens M2M (Machine-to-Machine) de Auth0 para las comunicaciones inter-servicio.

**Problema 2 — Consistencia de datos:** Cuando se crea un pedido, se debe reservar stock en Inventory Service de forma atómica. Se resolvió con una reserva temporal con expiración y el patrón Outbox para garantizar la publicación del evento.

**Problema 3 — CORS:** El frontend Angular (puerto 4200) necesita llamar al BFF (puerto 8080). Se configuró CORS en el BFF para permitir el origen del frontend tanto en desarrollo como en producción.

**Problema 4 — Variables de entorno:** Cada microservicio requiere configuración diferente según el entorno (local, Docker). Se centralizó en el `.env` raíz y `docker-compose.yml` para facilitar el despliegue.

---

## F. Pruebas Unitarias y Aseguramiento de la Calidad

### Estrategia de pruebas implementada

Se implementaron pruebas unitarias en los 4 microservicios de negocio (excluido BFF que es de orquestación) usando **JUnit 5 + Mockito**, con reporte de cobertura mediante **JaCoCo**.

### Servicios probados

| Servicio | Clases de Test | Cobertura |
|---|---|---|
| `envio-service` | `EnvioServiceImplTest`, `PedidoCanceladoConsumerTest`, `PedidoEstadoActualizadoConsumerTest` | Servicios e integración Kafka |
| `inventory-service` | `ProductoControllerTest`, `ProductoServiceImplTest`, `KafkaConsumerTest`, `KafkaProducerTest` | Controllers, servicios y Kafka |
| `pedidos-service` | `PedidoServiceImplTest`, `CarritoServiceImplTest`, `KafkaProducerServiceTest` | Servicios de negocio |
| `usuarios-service` | `UsuarioServiceImplTest`, `UsuarioControllerTest` | Servicio y controller |

### Ejemplo de prueba unitaria — PedidoServiceImpl

```java
@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void crearPedido_conStockDisponible_debeGuardarYRetornarPedido() {
        // Arrange
        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setItems(List.of(new ItemRequest(1L, 2)));
        when(inventoryClient.verificarStock(1L, 2)).thenReturn(true);
        when(pedidoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Pedido resultado = pedidoService.crearPedido(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crearPedido_sinStock_debeLanzarExcepcion() {
        when(inventoryClient.verificarStock(anyLong(), anyInt())).thenReturn(false);

        assertThrows(StockInsuficienteException.class,
            () -> pedidoService.crearPedido(buildRequest()));
    }
}
```

### Impacto de las pruebas en la calidad

- **Detección temprana de regresiones:** Al ejecutar `test.ps1` después de cada cambio, se detectaron 3 casos donde refactorizaciones rompían lógica existente.
- **Documentación viva:** Las pruebas describen el comportamiento esperado del sistema, funcionando como documentación ejecutable.
- **Confianza en el refactor:** La cobertura de pruebas permitió refactorizar el `CheckoutService` con confianza de que los casos de borde seguían cubiertos.

### Ejecución de pruebas

```powershell
# Ejecutar todas las pruebas y generar reportes JaCoCo
.\test.ps1

# Reportes de cobertura disponibles en:
# cada-servicio/target/site/jacoco/index.html
```

### Dificultades encontradas

- **Mocking de Kafka:** Los consumidores Kafka requieren configuración especial para tests. Se resolvió con `@EmbeddedKafka` en los tests de integración.
- **Llamadas entre servicios:** Los clientes REST (Feign/WebClient) se mockearon con Mockito para aislar las pruebas unitarias de dependencias externas.

### Estrategias para mejorar en el futuro

- Implementar pruebas de contrato (Contract Testing) entre BFF y microservicios usando Pact
- Agregar pruebas de integración con Testcontainers para levantar PostgreSQL y Kafka reales en el pipeline
- Configurar un umbral mínimo de cobertura (≥70%) en el pipeline CI/CD para bloquear merges que bajen la calidad
