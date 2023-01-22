FROM mvn_java17 as build

COPY . /opt/build

RUN cd /opt/build && mvn clean install

FROM amazoncorretto:17

EXPOSE 8080

COPY --from=build /opt/build/target/*.jar /opt/

ENTRYPOINT ["java", "-jar", "opt/gibdd-0.0.1.jar"]