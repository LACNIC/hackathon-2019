# Up/Down

La certificación de recursos numéricos de internet, se realiza a través del protocolo RPKI, el cual es un protocolo de infraestructura de clave pública de recursos que permite validar el ASN(Número de Sistema Autónomo) que originó una ruta o prefijo IP en internet, dicho estándar opera a través de dos modos, uno hosted en el cual LACNIC emite los certificados de recursos y almacena tanto claves públicas como privadas. Estos certificados se emiten a demanda de las organizaciones y son estas las que realizan operaciones por medio de una interfaz web provista por LACNIC. El segundo modo es el delegated, el cual se basa en el estándar updown RPKI, este es un protocolo de aprovisionamiento de certificados RPKI basado en una simple interacción request / response, dónde el cliente o parte ‘down’, envía solicitudes al servidor o parte ‘up’, para luego este procesar, generar y enviar la respuesta.

Las operaciones que se pueden realizar mediante Updown son:

- Issue: Por medio de este comando, el cliente envía una solicitud al server, para que le emita un certificado con los recursos especificados en dicha solicitud.

- Issue response: Por medio de este comando, el server responde a la solicitud (issue) de emisión de certificado hecha por el cliente. Acá el server debe validar los recursos solicitados y luego de esto, emitir el certificado con los recursos especificados.

- List: Por medio de este comando, el cliente envía una solicitud al server, para que le emita un listado con todos los certificados vigentes, que le han sido creados anteriormente.

- List response: Por medio de este comando, el server responde a la solicitud (list) de emisión del listado de todos los certificados vigentes que se tienen de dicho cliente.

- Revoke: Por medio de este comando, el cliente envía una solicitud al server, para que revoque todos los certificados asociados a un hash de la clave pública que se envía en esta solicitud.

- Revoke response: Por medio de este comando, el server responde a la solicitud (revoke) de revocación de certificados, con el mismo hash de la clave pública que se envió en la solicitud.

Por medio de updown, una organización podrá tener su propio certificado firmado por la CA de LACNIC. En este sentido la organización debe contar con mecanismos para el despliegue de su propia infraestructura RPKI, como el modo hosted donde deberá emitir certificados y tener un repositorio en el cual se garantice la integridad y no repudio de la información publicada y de igual manera esta organización podrá desplegar RPKI mediante updown asumiendo las responsabilidades descritas anteriormente.

------------------
Presentación updown

https://docs.google.com/presentation/d/1E-KKzhLfiGP5BEBTi5BzLs_FJmdpqnp4AKrIGE9tT60/edit?usp=sharing


------
Trello

https://trello.com/b/wG3iS6Sp/updown
