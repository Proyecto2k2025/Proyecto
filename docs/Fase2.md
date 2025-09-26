#  FASE 2: Viabilidad del proyecto Gestor de E-Stock 

## 1.1 Requisitos funcionales 

* Registro e inicio de sesión de usuarios.

* Gestión de productos: añadir, editar, borrar y consultar inventario.

* Avisos de stock bajo.

* Gestión de pedidos con diferentes estados.

* Listado de clientes con datos básicos.

* Buscador simple para productos y pedidos.

* Exportar informes básicos a PDF.

## 1.2  Requisitos no funcionales

* Interfaz sencilla y responsive (usable desde PC y móvil).

* Seguridad básica.

* Código claro y documentado para facilitar el mantenimiento.

* Tiempo de respuesta aceptable.

* Servicio estable, con copias de seguridad manuales o automáticas.

## 2. Estudio del Arte

Actualmente existen plataformas como Shopify, WooCommerce, Odoo o Zoho Inventory. Todas son completas, pero también más complejas y costosas de lo que un pequeño comercio necesita.

**Gestor de E-Stock**  no busca competir con ellas, sino ser una alternativa más simple y ligera, que cubra lo esencial: inventario, pedidos y clientes, sin necesidad de configuraciones avanzadas ni costes altos.

## 3. Justificación de viabilidad

### 3.1 Viabilidad técnica

Para no sobrecargar el desarrollo, se usarán tecnologías sencillas y accesibles:

Backend y API: Java(Spring Boot).

Base de datos: MySQL.

Frontend web: Kotlin o HTML, CSS y JavaScript .


### 3.2 Viabilidad económica

+ Servidor local o VPS económico (10-15 € al mes).

+ Dominio y certificado SSL ( 10-15 € al año).

+ Desarrollo hecho por mí, sin coste adicional.

### 3.3 Viabilidad operativa

<ins>El sistema se puede desarrollar por fases:</ins> Primero un módulo sencillo de inventario y pedidos. Después, si queda tiempo, añadire clientes e informes.

<ins>Mantenimiento fácil: </ins>copias de seguridad de la base de datos y actualizaciones ocasionales del servidor.

<ins>Interfaz clara para el usuario:</ins> sin curva de aprendizaje complicada.