~/Downloads/apache-tomcat-8.5.5/bin/shutdown.sh
pkill -f '/usr/bin/java'
~/Downloads/apache-maven-3.3.9/bin/mvn clean install
~/Downloads/apache-tomcat-8.5.5/bin/startup.sh
~/Downloads/apache-maven-3.3.9/bin/mvn tomcat7:redeploy -Durl=127.0.0.1:8080 -Duser=admin -Dpassword=admin

