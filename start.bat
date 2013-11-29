del RUNNING_PID
java -cp "lib\*;" -Dhttp.port=9000 -Dconfig.file=application.conf play.core.server.NettyServer