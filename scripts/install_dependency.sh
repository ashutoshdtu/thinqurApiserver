#!/bin/bash
echo "Installing jdk"

cmd="apt-get -y install openjdk-7-jdk openjdk-7-jre"

if which gdebi > /dev/null 2> /dev/null
then cmd="apt-get -y install openjdk-7-jdk openjdk-7-jre"
else cmd="yum -y install java-1.7.0-openjdk-devel"
fi

echo $cmd
$cmd
