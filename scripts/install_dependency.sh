#!/bin/bash
echo "Installing jdk"

cmd="yum -y install java-1.7.0-openjdk-devel"
echo $cmd
$cmd
