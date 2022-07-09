1. 配置文件 
    ```
    conf/zoo.cfg
    tickTime=2000 基本时间单元，单位是毫秒
    dataDir=/Users/tom/zookeeper 持久化数据存放的目录
    clientPort=2181 监听客户端连接的端口，默认是2181
    ```

2. 命令
    ```shell
    #启动 
    zkServer.sh start/start-foreground/stop/restart/status/upgrade/print-cmd
    #检查是否启动成功 
    echo ruok | nc localhost 2181
    #imok
    ```
