#!/bin/bash
echo "Starting apiserver"

cmd="cd /home/apiserver/"
echo $cmd
$cmd

startcmd="./activator start"
echo $startcmd
${startcmd} &
