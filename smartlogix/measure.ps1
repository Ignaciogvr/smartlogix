$containers = docker ps -a --format '{{.Names}}'
foreach ($c in $containers) {
    $start = docker inspect -f '{{.State.StartedAt}}' $c
    $health = docker inspect -f '{{if .State.Health}}{{.State.Health.Log}}{{end}}' $c
    $status = docker inspect -f '{{.State.Health.Status}}' $c
    if ($c -match "service" -or $c -match "frontend") {
        $logTime = docker logs $c 2>&1 | Select-String "Started " | Select-Object -Last 1
        Write-Output "$c : $status - $logTime"
    } else {
        Write-Output "$c : $status"
    }
}
