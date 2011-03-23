#!/bin/sh

# Go into the liquibase folder
# run this sh script
# copy the new data into from the working.liquibase.xml to a new file (Review the generated code)
# add a include into current.liquibase.xml
# delete working.liquibase.xml
# continue your work ...


ROOT=$(dirname $PWD)

USERNAME=bloatit
PASSWORD=passe
URL=jdbc:postgresql://localhost/bloatit

REFERENCE_URL=hibernate:hibernate.cfg.xml
CHANGELOG_FILE=$ROOT/src/main/resources/liquibase/working.liquibase.xml

MAVEN_REPO=$HOME/.m2/repository
CLASSPATH=$ROOT/target/classes:$ROOT/liquibase/liquibase-hibernate-2.0.1-SNAPSHOT.jar:$MAVEN_REPO/org/hibernate/hibernate-core/3.6.1.Final/hibernate-core-3.6.1.Final.jar:$MAVEN_REPO/org/hibernate/hibernate-commons-annotations/3.2.0.Final/hibernate-commons-annotations-3.2.0.Final.jar:$MAVEN_REPO/org/hibernate/javax/persistence/hibernate-jpa-2.0-api/1.0.0.Final/hibernate-jpa-2.0-api-1.0.0.Final.jar:$MAVEN_REPO/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:$MAVEN_REPO/postgresql/postgresql/8.4-701.jdbc4/postgresql-8.4-701.jdbc4.jar:$MAVEN_REPO/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:$MAVEN_REPO/org/slf4j/slf4j-jdk14/1.6.1/slf4j-jdk14-1.6.1.jar

java -jar $ROOT/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar \
        --classpath=$CLASSPATH \
        --changeLogFile=$CHANGELOG_FILE \
        --url=$URL \
        --username=$USERNAME \
        --password=$PASSWORD \
        --referenceUrl=$REFERENCE_URL \
        diffChangeLog

        
