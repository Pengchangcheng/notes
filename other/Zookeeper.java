1.配置文件 conf/zoo.cfg
tickTime=2000
dataDir=/Users/tom/zookeeper
clientPort=2181
tickTime是zookeeper中的基本时间单元，单位是毫秒。
datadir是zookeeper持久化数据存放的目录。
clientPort是zookeeper监听客户端连接的端口，默认是2181.
------------------------------------------------------------------------------------------------------
2.命令
启动 zkServer.sh start/start-foreground/stop/restart/status/upgrade/print-cmd
检查是否启动成功 
echo ruok | nc localhost 2181
imok
------------------------------------------------------------------------------------------------------
3.后面讲的有点复杂，懒得看
