# chatroom

# build
<code>
./gradlew clean build
</code>

# deploy
* install Tomcat 7, you can download tomcat 7 from https://tomcat.apache.org/download-70.cgi
* remove existing app(including tomcat's example apps): rm -rf $TOMCAT_ROOT/webapps/*
* copy mainWebApp/build/libs/mainWebApp.war to $TOMCAT_ROOT/webapps/ROOT.war
* start tomcat

# test
It has been deployed at http://104.167.113.195:8080/demo.html

