FROM oracle/graalvm-ce:1.0.0-rc12 as builder

ENV GRAALVM_HOME=$JAVA_HOME
ADD "https://github.com/lihaoyi/mill/releases/download/0.3.6/0.3.6" mill
COPY . .
RUN chmod +x mill
RUN ./mill server.nativeImage

FROM ubuntu:18.04
COPY --from=builder ./out/server/nativeImage/dest/server /usr/local/bin/server
ENTRYPOINT "/usr/local/bin/server"
