# Springboot application with Docker
This branch demonestrates to run a springboot REST application with `MySQL` database on `docker` container

## A. Clone the micro-service application
1. Clone the already developed application and checkout to branch `boot-rest-api-with-h2-and-docker`
```
$ git clone https://github.com/ecominds/micro-service.git
$ cd micro-service
$ git checkout boot-rest-api-with-h2-and-docker
```

2. Update the h2 database scope to `test` and include mysql dependency in `pom.xml` with `runtime` scope (shown below)
```
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>test</scope>
</dependency>
```

```
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
```

3. Update the mysql jdbc datasource properties in `application.yml`
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/boot_webapp
    driverClassName: com.mysql.cj.jdbc.Driver
    username: app_user
    password: secured_passwd123
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    generate-ddl: true
    hibernate:
        ddl-auto: update
    show-sql: false
    properties:
        hibernate.format_sql: false
```

4. Create an `application.yml` file in `test/resources` path with the below content as `H2 database` is still required for testing during build
```
spring:
  datasource:
    url: jdbc:h2:mem:rest_api_db
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

5. Build the maven project

6. Execute the below command to create a docker image
```
$ docker build -t boot-rest-api .
```

```
$ docker images -a
REPOSITORY      TAG       IMAGE ID       CREATED          SIZE
boot-rest-api   latest    d1e68f72ecbc   42 seconds ago   146MB	
```

7. Execute the below command to run the spring-boot web application
```
$ docker run -it -d --rm --name=rest-api-with-mysql -p 80:8080 boot-rest-api
```

```
$ docker ps -a
CONTAINER ID   IMAGE           COMMAND               	  CREATED        STATUS        PORTS                 NAMES
24a796dfb945   boot-rest-api   "java -jar /rest-api.jar"  8 seconds ago  Up 6 seconds  0.0.0.0:80->8080/tcp  rest-api-with-mysql
```

8. Execute inspect and logs command to review the running container status and logs
```
$ docker inspect rest-api-with-mysql
$ docker logs rest-api-with-mysql
```

## B. Configure and run MySQL docker container
1. Download latest version of MySQL
```
$ docker pull mysql/mysql-server:latest
```
2. Create persistent volume
```
$ docker volume create docker_vol_mysql
$ docker volume ls 
$ docker volume inspect docker_vol_mysql
```
3. Create a custom network that will be used by both mysql and web application
```
$ docker network create dev-env-net
```
4. Start the mysql instance with persistent volume and given custom network and expose on specified port
```
$ docker run --name=docker_mysql --network=dev-env-net -d -p 3306:3306 -v docker_vol_mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=ranjan mysql/mysql-server
```
5. Inspect the running mysql instance
```
$ docker inspect docker_mysql
```
6. Connects to bash mode and create database, users and provision access permission
```
$ docker exec -it docker_mysql bash
$ docker exec -it docker_mysql bash
bash-4.4# mysql -u root -p
Enter password: <ENTER-PASSWD>
```

```
mysql> create database boot_webapp;
Query OK, 1 row affected (0.03 sec)

mysql> CREATE USER 'app_user'@'%' IDENTIFIED BY 'secured_passwd123';
mysql> GRANT ALL PRIVILEGES ON boot_webapp.* TO 'app_user'@'%' WITH GRANT OPTION;
mysql> FLUSH PRIVILEGES;
```

## Setup to connect MySQL docker container from springboot application
The easiest step is to run both MySQL and springboot docker container in the same network

1. Update the mysql jdbc datasource properties in `application.yml` and `build the maven project` so that the generated jar will contain the updated `application.yml` configuration. Ideally, the main change would be URL only if there is no changes in dbname or its credentials
```
#Get IP of the running mysql instance. Ideally, it would be 172.19.0.2
jdbc.urlPath = jdbc:mysql://172.19.0.2:3306/boot_webapp
```

2. Build the docker image
```
$ docker build -t boot-rest-api .
```

3. Execute the below command. Its contains the network name in which mysql container is running
```
$ docker run -it -d --name=rest-api-with-mysql --network=dev-env-net -p 80:8080 boot-rest-api
```

4. verify the logs and test the application

## Use encrypted credential in properties file
The simplest way is to use the Jasypt library in order to improve security of Java applications based on Spring framework.

1. Include the `Jasypt` dependency in `pom.xml`
```
<dependency>
	<groupId>com.github.ulisesbocchio</groupId>
	<artifactId>jasypt-spring-boot-starter</artifactId>
	<version>3.0.3</version>
</dependency>
```

2. Add the `@EnableEncryptableProperties` annotation in starter main class
```
@SpringBootApplication
@Slf4j
@EnableEncryptableProperties
public class AppStarterMainClass
```

3. Change the database password from `plain text password` to `ENC(<encrypted password>)`. See below:
```
password: ENC(bA8MQzQyTI8oD4YOWPeWlhrH1mtlLiTfCIbbbkSTsJkBBrOgFjyIUFYKRqqCo551ExcnDGeJN+m1P3Bg12/yyA==)
```

The above encrypted password of `secured_passwd123` with secret key as `MySecretPwd`

4. Set the secret key in environment properties/JVM parameter/docker runtime properties.

* If running in eclipse or local system, include `-Djasypt.encryptor.password=MySecretPwd` in JVM paramater
* If running in docker container, include `-e "jasypt.encryptor.password=MySecretPwd"` in `docker run` command. See below:
```
$ docker run -it -d --name=rest-api-with-mysql --network=dev-env-net -e "jasypt.encryptor.password=MySecretPwd" -p 80:8080 boot-rest-api
```

### Encrypt credential
Add the below code snippet in main class to encrypt the password
```
@Autowired
private StringEncryptor stringEnc;
```
and
```
System.out.println(stringEnc.encrypt("secured_passwd123"));
```

Note: Kindly note, use the same secret key to encrypt or decrypt the password