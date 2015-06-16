#!/bin/bash
echo "Stopping apiserver"

cmd="cd /home/apiserver"
echo $cmd
$cmd

cmd="ls"
echo $cmd
$cmd

stopcmd="./activator stop"
echo $stopcmd
${stopcmd}
