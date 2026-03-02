FROM gcr.io/distroless/java21-debian12
COPY build/libs/javalin.jar /app/app.jar
COPY build/libs/libs /app/libs
WORKDIR /app
CMD ["app.jar"]
