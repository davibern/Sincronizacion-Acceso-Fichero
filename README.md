# Sincronizacion-Acceso-Fichero

Este ejercicio realiza los siguiente:

## Paso 1:
Un siministrador accede a un fichero y escribe un número

## Paso 2:
Un lector accede al mismo fichero, extrae el número y vacía el fichero.

## Condiciones:
- El suministrador no puede escribir si el fichero tiene contenido
- El lector no puede extraer si el fichero está vacío

## Sincroninismo:
- Se usa getChannel().lock() de la clase FileLock para cerrar el acceso a los demás procesos.
- Se usa release() para liberar el recurso (en este caso el fichero)

## JDK
Se usa la versión 11

# Fuente:
https://www.youtube.com/watch?v=Js8DhOC_u3w
