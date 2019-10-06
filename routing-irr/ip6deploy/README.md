# Desplegando IPv6

No vamos a describir aqui la importancia que viene adquiriendo, en la actual coyuntura de Internet, el avanzar con el despliegue de IPv6.

Desplegar IPv6 tiene multiples aristas, pero hay un elemento que es fundamental y previo a cualquier otra iniciativa y es **realizar el anuncio via bgp de los prefijos asignados por LACNIC**.

En este proyecto les proponemos dos modalidades de trabajo:

- Sobre su propia infraestructura
- Sobre un ambiente virtual, **pero utilizando la numeracion real asignada por LACNIC**

El entregable a subir a github son las configuraciones de los routers y los documentos o diagramas en los que se hayan apoyado.

## Anuncio via BGP de los prefijos asignados por LACNIC

- Ambiente virtual (para Vbox): https://s3.amazonaws.com/lacnog-netlab/VMs/mindesktop_gns3_v5.ova
   - Usuario: vagrant / vagrant
- VirtualBox: https://www.virtualbox.org/wiki/Downloads

### LAB de GNS3

- Lab de GNS3: https://github.com/LACNIC/hackathon-2019/blob/master/routing-irr/ip6deploy/ipv6deploy.tar.gz?raw=true

Abrirlo dentro de la carpeta **ANTES DE INICIAR EL GNS3** $HOME/GNS3/projects con el comando:

```
tar xzvf ipv6deploy.tar.gz
```

### Diagrama del Lab

![Lab](https://raw.githubusercontent.com/LACNIC/hackathon-2019/master/routing-irr/ip6deploy/lab.png)

#### Direccionamiento

a. 2001:db8:1::0/64

b. 2001:db8:2::0/64

c. 2001:db8:3::0/64

d. 2001:db8:4::0/64

e. 2001:db8:5::0/64

Servidor Google: 2001:db8:cafe::2
