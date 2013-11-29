del RUNNING_PID
java -cp "lib\*;" -Dhttp.port=9000 -Dconfig.file=prod.conf play.core.server.NettyServer