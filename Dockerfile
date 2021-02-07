FROM myjdk

WORKDIR /user/project-tracker

COPY /target/demo*.jar app.jar

CMD ["java","-jar","-Dspring.profiles.active='default,docker'","app.jar"]

LABEL manager="sagar machine"
LABEL project="project tracker"