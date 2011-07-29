#!/bin/sh

# Go into the liquibase folder
# run this sh script
# copy the new data into from the working.liquibase.xml to a new file (Review the generated code)
# add a include into current.liquibase.xml
# delete working.liquibase.xml
# continue your work ...

if [ -e "$PWD/liquibase" ] ; then 
ROOT=$PWD
else
ROOT=$(dirname $PWD)
fi

USERNAME=bloatit
PASSWORD=passe
URL=jdbc:postgresql://localhost/bloatit

REFERENCE_URL=hibernate:hibernate.cfg.xml
WORK_CHANGELOG_FILE=$ROOT/src/main/resources/liquibase/working.liquibase.xml
CURRENT_CHANGELOG_FILE=$ROOT/src/main/resources/liquibase/current.liquibase.xml


MAVEN_REPO=$HOME/.m2/repository
POSTGRESQL_VERSION=$MAVEN_REPO/postgresql/postgresql/8.4-702.jdbc4/postgresql-8.4-702.jdbc4.jar

CLASSPATH=$ROOT/build/classes/main/:$ROOT/liquibase/liquibase-hibernate-2.0.1-SNAPSHOT.jar:$MAVEN_REPO/org/hibernate/hibernate-core/3.6.1.Final/hibernate-core-3.6.1.Final.jar:$MAVEN_REPO/org/hibernate/hibernate-commons-annotations/3.2.0.Final/hibernate-commons-annotations-3.2.0.Final.jar:$MAVEN_REPO/org/hibernate/javax/persistence/hibernate-jpa-2.0-api/1.0.0.Final/hibernate-jpa-2.0-api-1.0.0.Final.jar:$MAVEN_REPO/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:$POSTGRESQL_VERSION:$MAVEN_REPO/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:$MAVEN_REPO/org/slf4j/slf4j-jdk14/1.6.1/slf4j-jdk14-1.6.1.jar

MIN_CLASSPATH=$ROOT/build/classes/main:$POSTGRESQL_VERSION:$MAVEN_REPO/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:$MAVEN_REPO/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:$MAVEN_REPO/org/slf4j/slf4j-jdk14/1.6.1/slf4j-jdk14-1.6.1.jar

if [ "$1" = "diff" ] ; then
java -jar $ROOT/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar \
        --classpath=$CLASSPATH \
        --changeLogFile=$WORK_CHANGELOG_FILE \
        --url=$URL \
        --username=$USERNAME \
        --password=$PASSWORD \
        --referenceUrl=$REFERENCE_URL \
        diffChangeLog
elif [ "$1" = "update" ] ; then 
java -jar $ROOT/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar \
        --classpath=$MIN_CLASSPATH \
        --changeLogFile=$CURRENT_CHANGELOG_FILE \
        --url=$URL \
        --username=$USERNAME \
        --password=$PASSWORD \
        update
elif [ "$1" = "rollback" ] ; then 
java -jar $ROOT/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar \
        --classpath=$MIN_CLASSPATH \
        --changeLogFile=$CURRENT_CHANGELOG_FILE \
        --url=$URL \
        --username=$USERNAME \
        --password=$PASSWORD \
        rollbackCount "$2"

else 
  echo "usage: $1 { diff | update | rollback count }"
fi
        
