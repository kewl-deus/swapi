cd build
cd docker
docker build --build-arg JAR_FILE=swapi-spring-webflux-0.0.1-SNAPSHOT.jar -t swapi/swapi-spring-webflux:0.1 .
