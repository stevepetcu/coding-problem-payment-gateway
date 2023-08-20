# > 1. Build the Java app:
FROM amazoncorretto:17 as build
RUN mkdir /buildDir
COPY . /buildDir
WORKDIR /buildDir

# Skip tests, they should be run in a previous step;
RUN ./gradlew clean build -x test && cp build/libs/payment-gateway-api-0.0.1-SNAPSHOT.jar app.jar && jar -xf app.jar
# > End 1.

# > 2. Run the Java app:
FROM amazoncorretto:17
VOLUME /tmp
ARG DEPENDENCY=/buildDir
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

ENTRYPOINT ["java","-cp","app:app/lib/*","com.stefanpetcu.paymentgatewayapi.PaymentGatewayApiApplication"]
# > End 2.
