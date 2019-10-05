# Recursos

Aquí se encuentran los recursos necesarios para el desarrollo de la actividad.  

**Tecnologías utilizadas en el desarrollo del API**  
Java 8, SpringBoot, Maven, MySQL, OAuth y Swagger.  

**Swagger Responses**  
Cada operación debe tener al menos una respuesta definida. Por lo menos una respuesta satisfactoria, basándonos en el flujo ideal o Happy Path.  
Está compuesta por el HTTP Status Code y la data que se va a retornar en el response body o en los headers.  

**HTTP Status Code**  
https://www.restapitutorial.com/httpstatuscodes.html

**Link Swagger-ui de desarrollo**  
http://localhost:8080/lacnic/0.9/swagger-ui.html  

**Credenciales de Prueba durante desarrollo:**

**OrgId:** UY-ORG2-LACNIC  
**User:** apilacnic@gmail.com   
**Pass:** Apilacnic**  
**ClientID:** 4vscKpOltDsinfQZR9b7YboPqycOb5rR   
**ClientSecret:** RZlLVYpPvBfX9e6AJCkrflfpCA8iE2l5bM3It8XsSwtcZI_GujVxVW6-W9KrKZWZ   

**Cosas a tener en cuenta para el funcionamiento del ambiente de desarollo del API**  
*En el archivo *application.properties* de la carpeta *src/main/resources* hay que sustituir estas variables:  
spring.datasource.url=jdbc:mysql://192.168.10.33/api_milacnic   
spring.datasource.username=user   
spring.datasource.password=pass  
*En el *pom*, en el plugin de configuración en el tag *<jvmArguments>*, sustituir en el *keyStore* y *trustStore* la dirección donde va a estar guardada la carpeta *epp*.  
*Crear carpeta para logs en */var/local/apimilacnic/epp/*  


