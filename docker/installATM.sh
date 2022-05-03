#! /bin/sh

echo "Do you want to generate ATM Service Docker image ?"
echo ""
read -p "Enter your choice :" Chs

if [ "$Chs" = "Yes"]

then
	pwd
	cd $home
	# Build the image
	#-----------------
	docker rm -f atm-service-${prj.version}.jar
	docker image rm atm-service-${prj.version}.jar
	dokcer build -t atm-service-${prj.version}.jar
		     -f /docker/Dockerfile_atm-service-${prj.version}.jar

	errorn=$?
	if [ errorn -ne 0 ]
	then
		echo "build image failed"
		exit $errorn
	fi

	mkdir -p $home/DockerFile

	docker run -d --privileged \
		   -v /data  	\
		   --name atm-service-${prj.version}	\

docker exec -t atm-service-${prj.version}
