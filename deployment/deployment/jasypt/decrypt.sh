#!/bin/sh

#$ ./decrypt.sh
#
#USAGE: decrypt.sh [ARGUMENTS]
#
#  * Arguments must apply to format:
#
#      "arg1=value1 arg2=value2 arg3=value3 ..."
#
#  * Required arguments:
#
#      input
#      password
#
#  * Optional arguments:
#
#      verbose
#      algorithm
#      keyObtentionIterations
#      saltGeneratorClassName
#      providerName
#      providerClassName
#      stringOutputType
#
# EXAMPLE :
# $ ./decrypt.sh input="k1AwOd5XuW4VfPQtEXEdVlMnaNn19hivMbn1G4JQgq/jArjtKqryXksYX4Hl6A0e" password=MYPAS_WORD



SCRIPT_NAME=decrypt.sh
EXECUTABLE_CLASS=org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI
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
