#!/bin/bash
echo "Starting apiserver"

#cmd="cd .."
cmd="cd /home/apiserver/"
echo $cmd
${cmd}

cleancmd="./activator clean"
echo $cleancmd
${cleancmd}

startcmd="./activator start"
echo $startcmd
${startcmd} &
