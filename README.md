# API REST usando Spring - Challenge Foro Alura 🚀

## Descripción del Proyecto 📝
El Foro Alura es una plataforma digital donde estudiantes y profesores de Alura pueden interactuar, compartir conocimientos y resolver dudas sobre cursos variados. Este proyecto se enfoca en desarrollar una API REST con Spring que gestiona los tópicos del foro, permitiendo a los usuarios:

- **Crear un nuevo tópico** ✨  
- **Mostrar todos los tópicos creados** 📋  
- **Mostrar un tópico específico** 🔍  
- **Actualizar un tópico existente** 🔄  
- **Eliminar un tópico** ❌  

---

## Características Principales 🌟

- **Autenticación de Usuario** 🔒: Implementación de autenticación robusta para asegurar que solo usuarios autenticados puedan gestionar recursos.
- **Gestión Completa de Recursos**: Capacidad de mostrar, crear, editar y eliminar:
  - Tópicos 📚  
  - Respuestas 💬  
  - Cursos 🎓  
  - Usuarios 👥  
- **Base de Datos**: Utilización de MySQL para el almacenamiento persistente de datos.

---

## Tecnologías Utilizadas 🛠

- **Java 17** ☕: Versión de Java para el desarrollo del backend.  
- **JPA Hibernate** 🗄: Para la gestión de persistencia de datos y ORM.  
- **Spring Framework**:
  - **Spring Boot** 🚀: Facilita el desarrollo de aplicaciones Spring.  
  - **Spring MVC** 🌐: Para manejar solicitudes HTTP y devolver datos en formato JSON.  
  - **Spring Data JPA** 🧑‍💻: Abstracción adicional sobre JPA y simplificación del acceso a datos.  
  - **Spring Security** 🛡: Para la autenticación y autorización de usuarios.  
- **JWT (JSON Web Token)** 🔑: Para la gestión segura de sesiones de usuario.  
- **MySQL** 🐬: Como sistema de gestión de bases de datos relacionales.  
- **IntelliJ IDEA** 🖥: IDE para el desarrollo de Java.  

---

## Estructura del Proyecto 📂

/src/main/java: Código fuente Java, incluyendo:

- Controladores: Para manejar solicitudes HTTP.
- Servicios: Lógica de negocio del sistema.
- Modelos: Entidades de la base de datos.
- Repositorios: Interfaces para operaciones CRUD con la base de datos. /src/main/resources: Recursos del proyecto:
- application.properties: Configuración de Spring Boot.
- SQL scripts: Para la inicialización y migración de la base de datos. /src/test/java: Tests unitarios y de integración.


---

## Configuración del Entorno ⚙

1. **Java JDK 17**: Asegúrese de tener instalado Java Development Kit 17.  
2. **MySQL**: Instale y configure MySQL Server.  
3. **IntelliJ IDEA**: Descargue e instale IntelliJ IDEA Community o Ultimate.  

---

## Instalación 📦

1. Clonar el repositorio:
   ```sh
   git clone https://github.com/zLeynerMC/Challenge-ForoHub-Alura
    ```
2. Configurar application.properties con su configuración de MySQL.

3. Construir el proyecto con Maven:
   ```sh
   mvn clean install

4. Ejecutar la aplicación desde IntelliJ IDEA o mediante el comando en la terminal:
   ```sh
   mvn spring-boot:run
   ```

## Contribución 🤝
- Para contribuir, por favor:

- Haga un fork del repositorio.
- Cree una rama nueva para cada característica o corrección de errores.
- Envíe un pull request con una descripción detallada de los cambios.
      

