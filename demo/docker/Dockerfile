# Imagen base para el contenedor de compilación
FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder
# Define el directorio de trabajo donde ejecutar comandos
WORKDIR /demo
# Copia las dependencias del proyecto
COPY pom.xml /demo/
# Descarga las dependencias del proyecto
RUN mvn dependency:go-offline
# Copia el código del proyecto
COPY src /demo/src
# Compila proyecto y descarga librerías
RUN mvn -B package -DskipTests
# Imagen base para el contenedor de la aplicación
FROM eclipse-temurin:21-jre
# Define el directorio de trabajo donde se encuentra el JAR
WORKDIR /usr/src/app/
# Copia el JAR del contenedor de compilación
COPY --from=builder /demo/target/*.jar /usr/src/app/app.jar

# Indica el puerto que expone el contenedor
EXPOSE 8443
# Comando que se ejecuta al hacer docker run
CMD [ "java", "-jar", "app.jar" ] 