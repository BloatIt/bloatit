#!/bin/sh

SCRIPT_NAME=digest.sh
EXECUTABLE_CLASS=org.jasypt.intf.cli.JasyptStringDigestCLI
BIN_DIR=`dirname $0`
DIST_DIR=$BIN_DIR/..
LIB_DIR=$HOME/.m2/
EXEC_CLASSPATH="."

EXEC_CLASSPATH=$EXEC_CLASSPATH:/home/thomas/.m2/repository/org/jasypt/jasypt/1.7/jasypt-1.7.jar

JAVA_EXECUTABLE=java
if [ -n "$JAVA_HOME" ]
then
  JAVA_EXECUTABLE=$JAVA_HOME/bin/java
fi

$JAVA_EXECUTABLE -classpath $EXEC_CLASSPATH $EXECUTABLE_CLASS $SCRIPT_NAME "$@"
