FROM openjdk:8
COPY build/libs/order-1.0.0.jar order-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/order-1.0.0.jar"]