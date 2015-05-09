#!/bin/bash
echo "Starting apiserver"

cmd="cd .."
echo $cmd
$cmd

startcmd="./activator start"
echo $startcmd
${startcmd} &
