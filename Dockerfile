FROM maven:3.9.9-amazoncorretto-21

WORKDIR /app

COPY . .

RUN mvn clean install

FROM amazoncorretto:21
WORKDIR /app
COPY --from=0 /app/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]