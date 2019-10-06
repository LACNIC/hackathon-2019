# Usando el IRR para generar configuracion de routers

El principal uso de la informacion contenida en un IRR es el generar configuracion de routers para poder filtrar prefijos de acuerdo a las politicas de enrutamiento expresadas por cada usuario de IRR.

La informacion de los IRR puede consultarse por diferentes interfaces, pero particularmente la interfaz de puerto 43 es util para realizar automatizacion.

Dependiendo del fabricante los enrutadores utilizan diferentes lenguajes para expresar estos filtros. Un caso "clasico" es el lenguaje que utilza Quagga, derivado de la CLI de Cisco.

## Recursos

### Como consultar RADB:

Como consultar RADB: [https://www.radb.net/support/tutorials/how-to-query-merit-radb.html]

#### Ejemplo:

```
whois -h whois.radb.net 128.223.0.0/16
```

```
route:         128.223.0.0/16
descr:         UONet
               University of Oregon
               Computing Center
               Eugene, OR 97403-1212
               USA
origin:        AS3582
mnt-by:        MAINT-AS3582
changed:       meyer@ns.uoregon.edu 19960222
source:        RADB
```

### Sintaxis de los route-map de Quagga

Sintaxis de los route map de Quagga: https://www.nongnu.org/quagga/docs/docs-multi/Route-Map.html

#### Ejemplo:

