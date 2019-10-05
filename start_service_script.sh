./gradlew flywayMigrate -i
./gradlew build
docker build -t perfect-restaurant .
docker run -m512M --cpus 2 -it -p 8080:8080 --rm perfect-restaurant