#!/bin/bash
echo "Stopping apiserver"

cmd="cd .."
echo $cmd
$cmd

startcmd="./activator stop"
echo $startcmd
${startcmd}
