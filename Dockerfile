FROM java:8
LABEL maintainer="Błażej Łach"
COPY . /
WORKDIR /
RUN javac DockerConnectMySQL.java
CMD ["java", "-classpath", "mysql-connector-java-8.0.13.jar:.","DockerConnectMySQL"]
