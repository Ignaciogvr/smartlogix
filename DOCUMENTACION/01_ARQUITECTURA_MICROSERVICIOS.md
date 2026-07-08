# Arquitectura de Microservicios - SmartLogix

## 📐 Descripción General

El sistema SmartLogix implementa una arquitectura de microservicios moderna y escalable que separa las responsabilidades del negocio en servicios independientes y especializados.

## 🏗️ Componentes de la Arquitectura

### 1. **BFF Service** (Backend for Frontend)
- **Puerto:** 8080
- **Responsabilidad:** Capa de agregación y orquestación entre el frontend y los microservicios
- **Tecnologías:** Spring Boot 3.3.5, WebFlux, OAuth2 Resource Server
- **Funcionalidades clave:**
  - Agregación de datos de múltiples servicios
  - Simplificación de llamadas para el frontend
  - Gestión centralizada de autenticación
  - Circuit Breaker con Resilience4J
  - API REST unificada para Angular Frontend

### 2. **Usuarios Service**
- **Puerto:** 8081
- **Responsabilidad:** Gestión completa de usuarios y autenticación
- **Base de Datos:** PostgreSQL (smartlogix_usuarios)
- **Tecnologías:** Spring Boot 3.2.5, JPA, OAuth2, Flyway
- **Funcionalidades clave:**
  - CRUD de usuarios (clientes, vendedores, chofer, admin)
  - Gestión de roles y permisos
  - Integración con Auth0
  - Migraciones de base de datos con Flyway
  - Validaciones de negocio

### 3. **Pedidos Service**
- **Puerto:** 8082
- **Responsabilidad:** Gestión del ciclo de vida de pedidos y carrito de compras
- **Base de Datos:** PostgreSQL (smartlogix_pedidos)
- **Tecnologías:** Spring Boot 3.2.5, JPA, Kafka, OAuth2, Flyway
- **Funcionalidades clave:**
  - Carrito de compras temporal
  - Checkout y creación de pedidos
  - Gestión de estados del pedido (PENDIENTE → CONFIRMADO → EN_PREPARACION → ENVIADO → ENTREGADO)
  - Reserva de stock con Inventory Service
  - Publicación de eventos a Kafka
  - Patrón Outbox para garantizar consistencia eventual
  - Integración con UserClient e InventoryClient

### 4. **Inventory Service**
- **Puerto:** 8083
- **Responsabilidad:** Gestión de productos, stock y reservas
- **Base de Datos:** PostgreSQL (smartlogix_inventory)
- **Tecnologías:** Spring Boot 3.2.5, JPA, Kafka, Circuit Breaker, Flyway
- **Funcionalidades clave:**
  - CRUD de productos
  - Gestión de stock y reservas temporales
  - Liquidaciones automáticas (Scheduler)
  - Publicación de eventos de inventario
  - Gestión de comentarios y calificaciones
  - Banners promocionales
  - Patrón Outbox para eventos

### 5. **Envío Service**
- **Puerto:** 8084
- **Responsabilidad:** Gestión de envíos y logística
- **Base de Datos:** PostgreSQL (smartlogix_envio)
- **Tecnologías:** Spring Boot 3.2.5, JPA, Kafka, OAuth2, Flyway
- **Funcionalidades clave:**
  - Cotización de envíos
  - Asignación de choferes
  - Tracking en tiempo real
  - Gestión de estados de envío
  - Consumo de eventos de pedidos desde Kafka
  - Notificaciones de eventos de envío

## 🔄 Flujo de Comunicación

```
┌─────────────────┐
│                 │
│  Angular Frontend│
│   (Port 4200)   │
│                 │
└────────┬────────┘
         │
         │ HTTP/REST
         ▼
┌─────────────────┐
│                 │
│   BFF Service   │◄────────┐
│   (Port 8080)   │         │
│                 │         │
└────────┬────────┘         │
         │                  │
         │                  │ Circuit Breaker
    ┌────┴────┬─────────┬──┴────┐
    │         │         │       │
    ▼         ▼         ▼       ▼
┌──────┐ ┌────────┐ ┌──────┐ ┌──────┐
│Usuario│ │Pedidos │ │Invent│ │Envío │
│Service│ │Service │ │Service│ │Service│
│ 8081 │ │  8082  │ │ 8083 │ │ 8084 │
└──┬───┘ └───┬────┘ └───┬──┘ └───┬──┘
   │         │           │        │
   │     ┌───┴───────────┴────────┘
   │     │
   ▼     ▼
┌────────────────┐
│                │
│  Apache Kafka  │
│  (Port 9092)   │
│                │
└────────────────┘
   │
   │ Events
   │
   ▼
[Analytics / Logging / Monitoring]
```

## 🗄️ Persistencia de Datos

### Estrategia de Persistencia

Cada microservicio tiene su propia base de datos independiente siguiendo el principio de **Database per Service**:

#### 1. **JPA (Java Persistence API)**
Todos los servicios utilizan Spring Data JPA para:
- Mapeo objeto-relacional (ORM)
- Operaciones CRUD automatizadas
- Relaciones entre entidades (@OneToMany, @ManyToOne, @ManyToMany)
- Validaciones de integridad
- Transacciones con @Transactional

**Ejemplo de Entidad:**
```java
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    
    // ... getters, setters
}
```

#### 2. **Stored Procedures (SPs)**
Se utilizan procedimientos almacenados para operaciones complejas:

**Envío Service** - Asignación automática de choferes:
```sql
CREATE OR REPLACE FUNCTION asignar_chofer_disponible(p_envio_id BIGINT)
RETURNS void AS $$
BEGIN
    UPDATE envios e
    SET chofer_id = (
        SELECT id FROM usuarios
        WHERE rol = 'CHOFER' 
        AND estado = 'DISPONIBLE'
        ORDER BY RANDOM()
        LIMIT 1
    )
    WHERE e.id = p_envio_id;
END;
$$ LANGUAGE plpgsql;
```

**Inventory Service** - Actualización de stock:
```sql
CREATE OR REPLACE FUNCTION actualizar_stock(
    p_producto_id BIGINT,
    p_cantidad INTEGER
) RETURNS void AS $$
BEGIN
    UPDATE productos
    SET stock = stock + p_cantidad
    WHERE id = p_producto_id;
    
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Producto no encontrado: %', p_producto_id;
    END IF;
END;
$$ LANGUAGE plpgsql;
```

#### 3. **Flyway Migrations**
Todos los servicios utilizan Flyway para versionamiento de base de datos:

```
src/main/resources/db/migration/
├── V1__create_initial_schema.sql
├── V2__add_stored_procedures.sql
├── V3__add_indexes.sql
└── V4__insert_sample_data.sql
```

### Bases de Datos

| Servicio | Base de Datos | Puerto | Esquema Principal |
|----------|---------------|--------|-------------------|
| Usuarios | PostgreSQL | 5433 | smartlogix_usuarios |
| Pedidos | PostgreSQL | 5434 | smartlogix_pedidos |
| Inventory | PostgreSQL | 5435 | smartlogix_inventory |
| Envío | PostgreSQL | 5436 | smartlogix_envio |

## 🔐 Seguridad

- **OAuth 2.0 / JWT**: Todos los servicios validan tokens JWT de Auth0
- **Spring Security**: Configuración de seguridad en cada microservicio
- **CORS**: Configurado para permitir llamadas desde Angular Frontend
- **Roles y Permisos**: CLIENTE, VENDEDOR, CHOFER, ADMIN

## 📡 Comunicación Asíncrona

### Apache Kafka
Los servicios se comunican mediante eventos a través de Kafka:

**Topics:**
- `pedido-events` - Eventos del ciclo de vida de pedidos
- `inventory-events` - Eventos de cambios en inventario
- `envio-events` - Eventos de tracking y estado de envíos

**Patrón Outbox:**
```java
@Transactional
public void crearPedido(PedidoRequest request) {
    // 1. Guardar entidad
    Pedido pedido = pedidoRepository.save(nuevoPedido);
    
    // 2. Guardar evento en outbox
    OutboxEvent event = new OutboxEvent();
    event.setAggregateType("Pedido");
    event.setAggregateId(pedido.getId());
    event.setEventType("PedidoCreado");
    event.setPayload(/* JSON del pedido */);
    outboxRepository.save(event);
}

// Scheduler publica eventos pendientes
@Scheduled(fixedDelay = 5000)
public void publishPendingEvents() {
    List<OutboxEvent> pending = outboxRepository.findPending();
    pending.forEach(event -> {
        kafkaProducer.send(event.getTopic(), event.getPayload());
        event.setPublished(true);
        outboxRepository.save(event);
    });
}
```

## ⚡ Resiliencia

### Circuit Breaker (Resilience4J)
Configurado en BFF para proteger contra fallos en cascada:

```java
@CircuitBreaker(name = "inventory", fallbackMethod = "fallbackGetProducts")
public List<ProductoDTO> getProductos() {
    return inventoryClient.findAll();
}

public List<ProductoDTO> fallbackGetProducts(Exception e) {
    log.warn("Circuit breaker activado para inventory service");
    return Collections.emptyList();
}
```

### Retry y Timeout
- Reintentos automáticos con backoff exponencial
- Timeouts configurados para evitar bloqueos

## 📊 Monitoreo

- **Spring Actuator**: Health checks en `/actuator/health`
- **Métricas Prometheus**: Exportadas en `/actuator/prometheus`
- **Logs estructurados**: JSON logs con RequestId para trazabilidad

## 🚀 Escalabilidad

La arquitectura permite:
- **Escalado horizontal**: Cada servicio puede escalar independientemente
- **Independencia tecnológica**: Cada servicio puede usar diferentes tecnologías
- **Deployment independiente**: Los servicios se despliegan sin afectar a otros
- **Fault isolation**: Fallos en un servicio no afectan a otros (Circuit Breaker)

## 🐳 Containerización

Todos los servicios están containerizados con Docker:

```yaml
version: '3.8'
services:
  bff-service:
    build: ./bff-service
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      
  usuarios-service:
    build: ./usuarios-service
    ports:
      - "8081:8081"
    depends_on:
      - postgres-usuarios
      
  # ... otros servicios
```

## 📝 Conclusión

Esta arquitectura de microservicios proporciona:
- ✅ Alta disponibilidad y tolerancia a fallos
- ✅ Escalabilidad independiente por servicio
- ✅ Separación clara de responsabilidades
- ✅ Persistencia de datos garantizada con JPA y SPs
- ✅ Comunicación asíncrona con Kafka
- ✅ Seguridad robusta con OAuth2/JWT
- ✅ Monitoreo y observabilidad completa
