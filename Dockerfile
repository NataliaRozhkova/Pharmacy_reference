FROM cortinico/java8-32bit  AS scratch

ARG name=pharmacy_reference
ARG port=8090

ENV NAME=$name

ENV TZ=Asia/Tashkent

COPY ./build/libs/pharmacy_reference-0.0.1-SNAPSHOT.jar /app/service-pharmacy_reference.jar
EXPOSE $port

WORKDIR /app

ENTRYPOINT java -jar /app/service-$NAME.jar

FROM scratch
