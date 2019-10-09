# Resultados estadísticas
#[Reporte log apache] - Crear reporte log apache:
#1.- Descripción General:
Se realiza un script, que lee el log de apache, busca dentro de los registros el
request "POST /provisioning/rpki-updown HTTP/1.1" y según eso clasifica
las ip que accedieron y en que fecha y contabiliza los registros.
-Nombre del script:
"repoapache.php"

#2.- Ej. de reporte:
Resumen:

Total provisioning/rpki-updown: 12422

IP's:
    [2a03:b0c0:2:f0::1d6:7001 ] => 12414
    [167.71.69.230 ] => 8

IP's por fecha de acceso:

    [2a03:b0c0:2:f0::1d6:7001  [26/Sep/2019] => 8
    [2a03:b0c0:2:f0::1d6:7001  [27/Sep/2019] => 903
    [2a03:b0c0:2:f0::1d6:7001  [28/Sep/2019] => 2284
    [2a03:b0c0:2:f0::1d6:7001  [29/Sep/2019] => 2309
    [167.71.69.230  [29/Sep/2019] => 5
    [2a03:b0c0:2:f0::1d6:7001  [30/Sep/2019] => 2441
    [167.71.69.230  [30/Sep/2019] => 1
    [167.71.69.230  [01/Oct/2019] => 1
    [2a03:b0c0:2:f0::1d6:7001  [01/Oct/2019] => 2474
    [2a03:b0c0:2:f0::1d6:7001  [02/Oct/2019] => 1995
    [167.71.69.230  [02/Oct/2019] => 1

#3.- Notas de la version v.b.01
-Script escrito en php para ser ejecutado en terminal
  ej: "php repoapache.php"
-El log debe estar en el mismo directorio del Script
-el nombre del log: "rpki-demo-ssl-access_log"
