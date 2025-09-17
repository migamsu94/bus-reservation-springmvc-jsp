# Build stage (Maven + JDK 11)
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn -B -DskipTests clean package

# Run stage (Tomcat 9 + JDK 11)
FROM tomcat:9.0-jdk11-temurin
ENV TZ=Asia/Seoul \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Duser.timezone=Asia/Seoul"

RUN rm -rf /usr/local/tomcat/webapps/*
# 산출물 이름이 달라도 *.war 로 매칭되니 OK
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/koBus.war

EXPOSE 8080
CMD ["catalina.sh", "run"]