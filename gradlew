#!/usr/bin/env sh

##############################################################################
## Gradle start up script for UN*X                                         ##
##############################################################################

APP_HOME=$(cd "$(dirname "$0")"; pwd -P)

export JAVA_HOME="${JAVA_HOME:-}"

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_PROPERTIES="$APP_HOME/gradle/wrapper/gradle-wrapper.properties"

if [ ! -f "$WRAPPER_JAR" ]; then
  mkdir -p "$(dirname "$WRAPPER_JAR")"
  # The actual JAR will be downloaded by Gradle on first run
fi

exec "$JAVA_HOME/bin/java" -Xmx64m -Xms64m \
  -Dorg.gradle.appname=gradlew \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain "$@"