#!/bin/bash
echo "Stopping apiserver"

#cmd="cd .."
PORT_NUMBER=9000
cmd="lsof -i tcp:${PORT_NUMBER} | awk 'NR!=1 {print \$2}' | xargs kill "
echo $cmd
lsof -i tcp:${PORT_NUMBER} | awk 'NR!=1 {print $2}' | xargs kill 
