FROM openjdk:17
WORKDIR /home/scraper
COPY target/Scraper1337-1.0-SNAPSHOT.jar /home/scraper/app.jar
CMD ["java","-jar","app.jar"]





