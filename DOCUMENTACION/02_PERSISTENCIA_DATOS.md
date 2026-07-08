# Estrategia de Persistencia de Datos - SmartLogix

## 📋 Resumen Ejecutivo

El sistema SmartLogix implementa una estrategia de persistencia robusta y escalable utilizando:
1. **JPA (Java Persistence API)** para operaciones CRUD estándar
2. **Stored Procedures** para lógica compleja de negocio
3. **Flyway** para versionamiento y migraciones de base de datos
4. **PostgreSQL** como motor de base de datos relacional
5. **Patrón Database per Service** para independencia entre microservicios

## 🗄️ Arquitectura de Base de Datos

### Principio: Database per Service

Cada microservicio tiene su propia base de datos independiente, lo que garantiza:
- **Autonomía**: Cada servicio puede evolucionar independientemente
- **Aislamiento de fallos**: Problemas en una BD no afectan otras
- **Escalabilidad**: Cada BD puede escalarse según necesidades específicas
- **Tecnología flexible**: Cada servicio puede usar el motor de BD más apropiado

```
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│  Usuarios Service│     │  Pedidos Service │     │ Inventory Service│
└────────┬─────────┘     └────────┬─────────┘     └────────┬─────────┘
         │                        │                        │
         ▼                        ▼                        ▼
┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐
│  PostgreSQL      │     │  PostgreSQL      │     │  PostgreSQL      │
│  smartlogix_     │     │  smartlogix_     │     │  smartlogix_     │
│  usuarios        │     │  pedidos         │     │  inventory       │
│  Port: 5433      │     │  Port: 5434      │     │  Port: 5435      │
└──────────────────┘     └──────────────────┘     └──────────────────┘
```

## 🔧 1. JPA (Java Persistence API)

### Configuración

Todos los servicios utilizan Spring Data JPA para simplificar el acceso a datos:

```yaml
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5433/smartlogix_usuarios
spring.datasource.username=smartlogix_user
spring.datasource.password=smartlogix_pass

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

### Entidades y Relaciones

#### Usuarios Service

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol; // CLIENTE, VENDEDOR, CHOFER, ADMIN
    
    @Column(name = "auth0_id", unique = true)
    private String auth0Id;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Getters y setters
}
```

#### Pedidos Service

```java
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado; // PENDIENTE, CONFIRMADO, EN_PREPARACION, etc.
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "direccion_envio")
    private String direccionEnvio;
    
    // Getters y setters
}

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(nullable = false)
    private String nombreProducto;
    
    // Getters y setters
}
```

#### Inventory Service

```java
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(name = "stock_reservado", nullable = false)
    private Integer stockReservado = 0;
    
    @Column(name = "vendedor_id", nullable = false)
    private Long vendedorId;
    
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "en_liquidacion")
    private Boolean enLiquidacion = false;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();
    
    // Getters y setters
}

@Entity
@Table(name = "reservas_stock")
public class ReservaStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "pedido_id", unique = true)
    private Long pedidoId;
    
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado; // ACTIVA, CONFIRMADA, EXPIRADA, CANCELADA
    
    // Getters y setters
}
```

#### Envío Service

```java
@Entity
@Table(name = "envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "pedido_id", unique = true, nullable = false)
    private Long pedidoId;
    
    @Column(name = "chofer_id")
    private Long choferId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado; // PENDIENTE, ASIGNADO, EN_CAMINO, ENTREGADO
    
    @Column(name = "direccion_origen", nullable = false)
    private String direccionOrigen;
    
    @Column(name = "direccion_destino", nullable = false)
    private String direccionDestino;
    
    @Column(name = "costo_envio", precision = 10, scale = 2)
    private BigDecimal costoEnvio;
    
    @Column(name = "codigo_tracking", unique = true)
    private String codigoTracking;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    // Getters y setters
}
```

### Repositorios JPA

Spring Data JPA genera automáticamente las consultas básicas:

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByAuth0Id(String auth0Id);
    List<Usuario> findByRol(Rol rol);
    List<Usuario> findByActivoTrue();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    List<Usuario> findActiveByRol(@Param("rol") Rol rol);
}

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioId(Long usuarioId);
    List<Pedido> findByEstado(EstadoPedido estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.usuarioId = :usuarioId ORDER BY p.fechaCreacion DESC")
    List<Pedido> findByUsuarioIdOrderByFechaDesc(@Param("usuarioId") Long usuarioId);
}

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    List<Producto> findByVendedorId(Long vendedorId);
    List<Producto> findByEnLiquidacionTrue();
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stock > 0")
    List<Producto> findAvailableProducts();
}
```

### Transacciones

Se utiliza `@Transactional` para garantizar consistencia ACID:

```java
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {
    
    @Override
    @Transactional
    public Pedido crearPedido(PedidoRequest request) {
        // 1. Validar usuario
        Usuario usuario = usuarioClient.getUsuario(request.getUsuarioId());
        
        // 2. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuario.getId());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFechaCreacion(LocalDateTime.now());
        
        // 3. Agregar detalles
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedidoRequest detalle : request.getDetalles()) {
            DetallePedido dp = new DetallePedido();
            dp.setPedido(pedido);
            dp.setProductoId(detalle.getProductoId());
            dp.setCantidad(detalle.getCantidad());
            dp.setPrecioUnitario(detalle.getPrecio());
            pedido.getDetalles().add(dp);
            
            BigDecimal subtotal = detalle.getPrecio()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
        }
        
        pedido.setTotal(total);
        
        // 4. Guardar (cascada guarda detalles también)
        Pedido savedPedido = pedidoRepository.save(pedido);
        
        // 5. Registrar evento en outbox
        registrarEventoOutbox(savedPedido);
        
        return savedPedido;
    }
    
    // Si hay excepción, toda la transacción se revierte
}
```

## 🗃️ 2. Stored Procedures

Para lógica compleja o que requiere alto rendimiento, se utilizan procedimientos almacenados:

### Envío Service - Asignación Automática de Choferes

```sql
-- V2__create_stored_procedures.sql

CREATE OR REPLACE FUNCTION asignar_chofer_disponible(p_envio_id BIGINT)
RETURNS void AS $$
DECLARE
    v_chofer_id BIGINT;
BEGIN
    -- Buscar chofer disponible con menos envíos activos
    SELECT id INTO v_chofer_id
    FROM usuarios u
    WHERE u.rol = 'CHOFER' 
      AND u.estado = 'DISPONIBLE'
      AND NOT EXISTS (
          SELECT 1 FROM envios e
          WHERE e.chofer_id = u.id
            AND e.estado IN ('ASIGNADO', 'EN_CAMINO')
      )
    ORDER BY RANDOM()
    LIMIT 1;
    
    IF v_chofer_id IS NULL THEN
        RAISE EXCEPTION 'No hay choferes disponibles';
    END IF;
    
    -- Asignar chofer al envío
    UPDATE envios
    SET chofer_id = v_chofer_id,
        estado = 'ASIGNADO',
        fecha_asignacion = NOW()
    WHERE id = p_envio_id;
    
    -- Actualizar estado del chofer
    UPDATE usuarios
    SET estado = 'OCUPADO'
    WHERE id = v_chofer_id;
    
END;
$$ LANGUAGE plpgsql;
```

### Inventory Service - Actualización Atómica de Stock

```sql
CREATE OR REPLACE FUNCTION actualizar_stock_atomico(
    p_producto_id BIGINT,
    p_cantidad INTEGER,
    p_operacion VARCHAR(10) -- 'INCREMENTAR' o 'DECREMENTAR'
) RETURNS TABLE(nuevo_stock INTEGER, stock_disponible INTEGER) AS $$
DECLARE
    v_stock_actual INTEGER;
    v_stock_reservado INTEGER;
BEGIN
    -- Bloquear fila para evitar race conditions
    SELECT stock, stock_reservado INTO v_stock_actual, v_stock_reservado
    FROM productos
    WHERE id = p_producto_id
    FOR UPDATE;
    
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Producto no encontrado: %', p_producto_id;
    END IF;
    
    -- Realizar operación según tipo
    IF p_operacion = 'INCREMENTAR' THEN
        UPDATE productos
        SET stock = stock + p_cantidad,
            fecha_actualizacion = NOW()
        WHERE id = p_producto_id;
        
        v_stock_actual := v_stock_actual + p_cantidad;
        
    ELSIF p_operacion = 'DECREMENTAR' THEN
        IF (v_stock_actual - v_stock_reservado) < p_cantidad THEN
            RAISE EXCEPTION 'Stock insuficiente. Disponible: %, Solicitado: %', 
                (v_stock_actual - v_stock_reservado), p_cantidad;
        END IF;
        
        UPDATE productos
        SET stock = stock - p_cantidad,
            fecha_actualizacion = NOW()
        WHERE id = p_producto_id;
        
        v_stock_actual := v_stock_actual - p_cantidad;
    ELSE
        RAISE EXCEPTION 'Operación inválida: %', p_operacion;
    END IF;
    
    -- Retornar valores actualizados
    RETURN QUERY SELECT v_stock_actual, (v_stock_actual - v_stock_reservado);
END;
$$ LANGUAGE plpgsql;
```

### Uso desde Java

```java
@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    
    @Procedure(name = "asignar_chofer_disponible")
    void asignarChoferDisponible(@Param("p_envio_id") Long envioId);
}

// Uso en servicio
@Service
public class EnvioServiceImpl implements EnvioService {
    
    @Override
    @Transactional
    public void asignarChofer(Long envioId) {
        envioRepository.asignarChoferDisponible(envioId);
        log.info("Chofer asignado automáticamente para envío {}", envioId);
    }
}
```

## 🔄 3. Flyway Migrations

Flyway gestiona el versionamiento y evolución del esquema de base de datos:

### Estructura de Migraciones

```
src/main/resources/db/migration/
├── V1__create_initial_schema.sql
├── V2__create_stored_procedures.sql
├── V3__add_indexes.sql
├── V4__insert_sample_data.sql
└── V5__add_audit_columns.sql
```

### Ejemplo de Migración

```sql
-- V1__create_initial_schema.sql
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    auth0_id VARCHAR(255) UNIQUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_usuarios_auth0 ON usuarios(auth0_id);

-- V3__add_indexes.sql
CREATE INDEX idx_pedidos_usuario ON pedidos(usuario_id);
CREATE INDEX idx_pedidos_estado ON pedidos(estado);
CREATE INDEX idx_pedidos_fecha ON pedidos(fecha_creacion DESC);

CREATE INDEX idx_productos_vendedor ON productos(vendedor_id);
CREATE INDEX idx_productos_activo ON productos(activo) WHERE activo = TRUE;
CREATE INDEX idx_productos_liquidacion ON productos(en_liquidacion) WHERE en_liquidacion = TRUE;
```

### Configuración

```yaml
# application.properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

## 📊 4. Patrón Outbox para Eventos

Para garantizar consistencia eventual entre microservicios:

```java
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType; // "Pedido", "Producto", etc.
    
    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;
    
    @Column(name = "event_type", nullable = false)
    private String eventType; // "PedidoCreado", "StockActualizado", etc.
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload; // JSON del evento
    
    @Column(nullable = false)
    private Boolean published = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
}

// Servicio que guarda en outbox
@Service
@Transactional
public class PedidoServiceImpl {
    
    private void registrarEventoOutbox(Pedido pedido) {
        OutboxEvent event = new OutboxEvent();
        event.setAggregateType("Pedido");
        event.setAggregateId(pedido.getId());
        event.setEventType("PedidoCreado");
        event.setPayload(serializeToJson(pedido));
        outboxRepository.save(event);
    }
}

// Scheduler que publica eventos
@Component
public class OutboxScheduler {
    
    @Scheduled(fixedDelay = 5000) // Cada 5 segundos
    public void publishPendingEvents() {
        List<OutboxEvent> pending = outboxRepository
            .findByPublishedFalse();
        
        for (OutboxEvent event : pending) {
            try {
                kafkaProducer.send("pedido-events", event.getPayload());
                event.setPublished(true);
                event.setPublishedAt(LocalDateTime.now());
                outboxRepository.save(event);
            } catch (Exception e) {
                log.error("Error publicando evento {}", event.getId(), e);
                // Se reintentará en la próxima ejecución
            }
        }
    }
}
```

## 🔒 5. Garantías de Consistencia

### Consistencia Fuerte (dentro del servicio)
- Transacciones ACID con `@Transactional`
- Constraints de base de datos (UNIQUE, NOT NULL, FK)
- Validaciones JPA con Bean Validation

### Consistencia Eventual (entre servicios)
- Patrón Outbox para publicación garantizada de eventos
- Consumidores Kafka con retry automático
- Idempotencia en consumidores de eventos

## 📈 6. Optimizaciones de Rendimiento

### Índices Estratégicos
```sql
-- Índices en columnas de búsqueda frecuente
CREATE INDEX idx_productos_nombre ON productos USING GIN (to_tsvector('spanish', nombre));
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_pedidos_usuario_fecha ON pedidos(usuario_id, fecha_creacion DESC);
```

### Connection Pooling
```yaml
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
```

### Lazy/Eager Loading
```java
// Evitar N+1 queries con fetch join
@Query("SELECT p FROM Pedido p JOIN FETCH p.detalles WHERE p.id = :id")
Optional<Pedido> findByIdWithDetalles(@Param("id") Long id);
```

## ✅ Conclusión

La estrategia de persistencia implementada garantiza:

- ✅ **Atomicidad**: Transacciones ACID en cada servicio
- ✅ **Consistencia**: Validaciones y constraints de BD
- ✅ **Aislamiento**: Database per Service
- ✅ **Durabilidad**: PostgreSQL con persistencia garantizada
- ✅ **Escalabilidad**: Índices, pooling, optimizaciones
- ✅ **Mantenibilidad**: Flyway para migraciones versionadas
- ✅ **Flexibilidad**: JPA para CRUD, SPs para lógica compleja
- ✅ **Consistencia eventual**: Patrón Outbox con Kafka
