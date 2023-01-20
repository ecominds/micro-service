# Springboot application with Docker
This branch demonestrates to run a springboot REST application with H2 database on docker container

## First, develop a simple springboot application H2 databasse.
1. Clone the already developed application and checkout to branch "boot-rest-api-with-h2"
```
$ git clone https://github.com/ecominds/micro-service.git
$ cd micro-service
$ git checkout boot-rest-api-with-h2
```

2. Create a Dockerfile in root path of the springboot application with the below content
```
# Dockerfile content
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} rest-api.jar
ENTRYPOINT ["java","-jar","/rest-api.jar"]
```

3. Execute the below command to create a docker image
```
$ docker build -t boot-rest-api .
```

```
$ docker images -a
REPOSITORY      TAG       IMAGE ID       CREATED          SIZE
boot-rest-api   latest    d1e68f72ecbc   42 seconds ago   146MB	
```

4. Execute the below command to run the spring-boot web application
```
$ docker run -it -d --name=boot-rest-api -p 80:8080 boot-rest-api
```

```
$ docker ps -a
CONTAINER ID   IMAGE           COMMAND               	  CREATED        STATUS        PORTS                 NAMES
24a796dfb945   boot-rest-api   "java -jar /rest-api.jar"  8 seconds ago  Up 6 seconds  0.0.0.0:80->8080/tcp  boot-rest-api
```

5. Execute inspect and logs command to review the running container status and logs
```
$ docker inspect boot-rest-api
$ docker logs boot-rest-api
```