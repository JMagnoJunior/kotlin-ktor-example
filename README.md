
That is my first example using kotlin + ktor framework.


running this project on dev mode (hot-deploy):
 `gradle -t build `

Running this project on docker:
```
 $ ./gradlew flywayMigrate -i
 $ ./gradlew build
 $ docker build -t perfect-restaurant .
 $ docker run -m512M --cpus 2 -it -p 8080:8080 --rm perfect-restaurant
```
  
