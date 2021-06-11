#!/bin/bash
#deletes the \r and \b in a file
FILE=$1
HELPFILE="help.txt"
tr -d '\b\r' < $FILE > $HELPFILE
cat $HELPFILE > $FILE
rm $HELPFILE