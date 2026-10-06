# Contenedor ejecutor: JDK 21 + el wrapper del proyecto (el código se monta desde el host)
FROM eclipse-temurin:21-jdk

# Carpeta de caché de Gradle: permisos abiertos para poder correr con el usuario del anfitrión
ENV GRADLE_USER_HOME=/gradle-cache
RUN mkdir -p /gradle-cache && chmod 777 /gradle-cache

WORKDIR /workspace
