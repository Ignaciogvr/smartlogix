# SmartLogix - Script de inicio desde cero (PowerShell)
# Ejecutar desde la carpeta raiz del proyecto: smartlogix\
# Requisitos: Docker Desktop instalado y corriendo

param(
    [switch]$NoCache,
    [switch]$SkipTests
)

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   SmartLogix - Arranque completo" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

# 1. Bajar todo (limpio)
Write-Host "`n[1/5] Bajando contenedores y volumes..." -ForegroundColor Yellow
docker compose down -v

# 2. Build
if ($NoCache) {
    Write-Host "`n[2/5] Compilando imagenes (sin cache - puede tardar 5-10 min)..." -ForegroundColor Yellow
    docker compose build --no-cache
} else {
    Write-Host "`n[2/5] Compilando imagenes..." -ForegroundColor Yellow
    docker compose build
}

# 3. Levantar
Write-Host "`n[3/5] Levantando servicios..." -ForegroundColor Yellow
docker compose up -d

# 4. Esperar a que PostgreSQL este listo
Write-Host "`n[4/5] Esperando que PostgreSQL este HEALTHY..." -ForegroundColor Yellow
$maxWait = 60
$waited = 0
do {
    Start-Sleep -Seconds 3
    $waited += 3
    $status = docker inspect postgres --format "{{.State.Health.Status}}" 2>$null
    Write-Host "  PostgreSQL status: $status ($waited s)"
} while ($status -ne "healthy" -and $waited -lt $maxWait)

if ($status -ne "healthy") {
    Write-Host "ERROR: PostgreSQL no arranco en tiempo. Revisa: docker logs postgres" -ForegroundColor Red
    exit 1
}

# 5. Insertar datos de prueba
Write-Host "`n[5/5] Insertando datos de prueba (seed)..." -ForegroundColor Yellow
Start-Sleep -Seconds 5  # Esperar migraciones Flyway
docker exec postgres psql -U postgres -f /dev/stdin < .\scripts\seed-data.sql
# Alternativa si lo anterior falla en Windows:
docker exec -i postgres psql -U postgres -c "
\connect inventory_db
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion)
VALUES
  ('Laptop ProBook G5','Laptop empresarial Intel Core i7, 16GB RAM, 512GB SSD',1200.00,50,'ELECTRONICA',4.5,120,85,'ACTIVO',NOW(),NOW()),
  ('Mouse Inalambrico M305','Mouse ergonomico inalambrico con receptor USB nano',25.00,200,'ACCESORIOS',4.2,340,290,'ACTIVO',NOW(),NOW()),
  ('Teclado Mecanico K2','Teclado mecanico retroiluminado RGB switches blue',89.00,75,'ACCESORIOS',4.7,89,60,'ACTIVO',NOW(),NOW()),
  ('Monitor UltraWide 34','Monitor curvo 34 IPS 3440x1440 144Hz',650.00,20,'ELECTRONICA',4.8,45,30,'ACTIVO',NOW(),NOW()),
  ('Auriculares WH-1000XM5','Auriculares premium con cancelacion de ruido activa',350.00,35,'AUDIO',4.9,230,180,'ACTIVO',NOW(),NOW())
ON CONFLICT (nombre) DO NOTHING;
" 2>$null

docker exec -i postgres psql -U postgres -d usuarios_db -c "
INSERT INTO usuarios (nombre, email, auth0_id, estado)
VALUES ('Admin SmartLogix', 'admin@smartlogix.com', 'OmTPVf437mhIS422gzlQEiE3ftSqwFOc@clients', 'ACTIVO')
ON CONFLICT (auth0_id) DO NOTHING;
" 2>$null

Write-Host "`n============================================" -ForegroundColor Green
Write-Host "   SISTEMA LISTO" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "Servicios disponibles:" -ForegroundColor White
Write-Host "  BFF (Gateway):      http://localhost:8080" -ForegroundColor Cyan
Write-Host "  Inventory Service:  http://localhost:8081" -ForegroundColor Cyan
Write-Host "  Pedidos Service:    http://localhost:8082" -ForegroundColor Cyan
Write-Host "  Usuarios Service:   http://localhost:8083" -ForegroundColor Cyan
Write-Host "  Envio Service:      http://localhost:8084" -ForegroundColor Cyan
Write-Host ""
Write-Host "Para ejecutar pruebas E2E:" -ForegroundColor Yellow
Write-Host "  .\scripts\test-e2e.ps1"

if (-not $SkipTests) {
    Write-Host "`n[BONUS] Ejecutando pruebas E2E..." -ForegroundColor Yellow
    if (Test-Path ".\scripts\test-e2e.ps1") {
        & .\scripts\test-e2e.ps1
    }
}
