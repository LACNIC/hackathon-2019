# Desplegando IPv6

No vamos a describir aqui la importancia que viene adquiriendo, en la actual coyuntura de Internet, el avanzar con el despliegue de IPv6.

Desplegar IPv6 tiene multiples aristas, pero entre ellas se destacan dos:

1. Anuncio via BGP de los prefijos asignados por LACNIC
2. Habilitacion de servicios (DNS, HTTP) en IPv6

## Anuncio via BGP de los prefijos asignados por LACNIC

- Ambiente virtual (para Vbox): https://s3.amazonaws.com/lacnog-netlab/VMs/mindesktop_gns3_v5.ova
   - Usuario: vagrant / vagrant
- VirtualBox: https://www.virtualbox.org/wiki/Downloads

### LAB de GNS3

- Lab de GNS3: https://github.com/LACNIC/hackathon-2019/blob/master/routing-irr/ip6deploy/ipv6deploy.tar.gz?raw=true

Abrirlo dentro de la carpeta $HOME/GNS3/projects con el comando:

```
tar xzvf ipv6deploy.tar.gz
```

### Diagrama del Lab

![Lab](https://raw.githubusercontent.com/LACNIC/hackathon-2019/master/routing-irr/ip6deploy/lab.png)

