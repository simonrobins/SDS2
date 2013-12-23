del RUNNING_PID
java -cp "lib\*;" -Dhttp.port=9001 -Dconfig.file=conf\application.conf play.core.server.NettyServer