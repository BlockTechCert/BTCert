FROM java:8
VOLUME /tmp
ADD target/Boot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN bash -c 'touch /app.jar'
#ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo/test", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

