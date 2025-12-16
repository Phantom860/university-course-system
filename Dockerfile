# 使用一个空白的本地镜像作为基础（可以用 scratch 或 debian slim 离线版本）
FROM scratch

WORKDIR /app

# 拷贝本地 JDK
COPY jdk17 /app/jdk17

# 拷贝 Spring Boot jar
COPY target/university-course-system-0.0.1-SNAPSHOT.jar app.jar


# 设置 JAVA_HOME
ENV JAVA_HOME=/app/jdk17
ENV PATH="$JAVA_HOME/bin:$PATH"

# 启动 Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]





