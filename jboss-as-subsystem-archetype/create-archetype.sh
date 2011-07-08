#!/bin/sh

# This script generates an archetype into target/archetype from the source project.
#
# Two optional commands can be provided (in the order listed):
#
# install - which will automatically install the generated archetype in the local repository
# generate - generate a project from the archetype into target/generated-project
#

ARCHETYPE_BUILD_DIR=target/generated-sources/archetype
ARCHETYPE_DIR=target/archetype
ARCHETYPE_RESOURCES_DIR=$ARCHETYPE_DIR/src/main/resources/archetype-resources

echo Generating archetype from project into $ARCHETYPE_BUILD_DIR...
if [ ! -z `which markdown` ]; then
    markdown readme.md -f readme.html
else
    echo markdown cannot be found, skipping generation of readme.html
fi

mvn clean archetype:create-from-project -Darchetype.properties=archetype.properties
echo Relocating generated archetype project to $ARCHETYPE_DIR...
rsync -az --exclude `basename $0` --exclude-from=archetype-excludes.txt $ARCHETYPE_BUILD_DIR/src $ARCHETYPE_DIR/

cp -f archetype-pom.xml $ARCHETYPE_DIR/pom.xml
mvn -f $ARCHETYPE_DIR/pom.xml clean

echo Patching generated archetype...
# could also use col -b
touch $ARCHETYPE_RESOURCES_DIR/pom.xml
touch $ARCHETYPE_RESOURCES_DIR/readme.md
#sed -i 's;<name>jboss-as-subsystem-src</name>;<name>${name}</name>;' $ARCHETYPE_RESOURCES_DIR/pom.xml
touch $ARCHETYPE_RESOURCES_DIR/pom.xml
#sed -i 's;eclipse-dot-files/\?;;' $ARCHETYPE_DIR/src/main/resources/META-INF/maven/archetype-metadata.xml
cp -f archetype-metadata.xml $ARCHETYPE_DIR/src/main/resources/META-INF/maven/archetype-metadata.xml
touch $ARCHETYPE_DIR/src/main/resources/META-INF/maven/archetype-metadata.xml
cp archetype-test.properties $ARCHETYPE_DIR/src/test/resources/projects/basic/archetype.properties

#rsync -az --exclude .svn eclipse-dot-files/ $ARCHETYPE_RESOURCES_DIR/
# fix the archetype plugin being an idiot
find $ARCHETYPE_RESOURCES_DIR -type f -exec sed -i -e 's;packageInPathFormat;package;g' {} \;
# remove local file that NetBeans add to source project
rm -f $ARCHETYPE_RESOURCES_DIR/src/main/webapp/WEB-INF/sun-web.xml
rm -f readme.html
# could set the release version at this point, if specified
if [ ! -z $1 ] && [ "$1" = "install" ]; then
    echo Installing archetype...
    shift
    mvn -f $ARCHETYPE_DIR/pom.xml install
fi

if [ ! -z $1 ] && [ "$1" = "generate" ]; then
    echo Generating project from archetype...
    cd target
    mvn archetype:generate -B -DarchetypeCatalog=local \
        -DarchetypeArtifactId=jboss-javaee6-webapp -DarchetypeGroupId=org.jboss.spec.archetypes -DarchetypeVersion=7.0.0-SNAPSHOT \
        -DartifactId=example-project -DgroupId=com.acme -Dpackage=com.acme.example -Dversion=1.0.0-SNAPSHOT -Dname="Java EE 6 webapp project"
fi
