#!/bin/bash
echo "Starting apiserver"

cmd="cd .."
echo $cmd
$cmd

startcmd="./activator stop"
echo $startcmd
${startcmd}
