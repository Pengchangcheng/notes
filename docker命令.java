拉取docker镜像docker 
pull image_name 

查看宿主机上的镜像，Docker镜像保存在/var/lib/docker目录下:
docker images 

删除镜像
docker rmi  docker.io/tomcat:7.0.77-jre7 or docker rmi b39c68b7af30

查看当前有哪些容器正在运行
docker ps

查看所有容器
docker ps -a

启动、停止、重启容器命令：
docker start container_name/container_id
docker stop container_name/container_id
docker restart container_name/container_id

后台启动一个容器后，如果想进入到这个容器，可以使用attach命令：
docker attach container_name/container_id


删除容器的命令：
docker rm container_name/container_id

从Docker hub上下载某个镜像:
docker pull centos:latest
------------------------------------------------------------------------------------




