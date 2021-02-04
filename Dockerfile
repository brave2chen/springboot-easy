FROM hengyunabc/arthas:3.4.4
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir /app
COPY ./target/springboot-easy-1.0.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["/sbin/tini", "--"]
CMD java $JAVA_OPTS -jar /app/app.jar



