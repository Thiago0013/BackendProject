# Estágio 1: Build (Você já fez isso, mas vamos garantir a imagem certa)
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Runtime (Onde deu o erro)
# Vamos usar a imagem da Eclipse Temurin, que é a sucessora oficial
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]