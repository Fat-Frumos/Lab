#Java v.17
sudo yum install -y java-17-amazon-corretto
java -version

#v1 Tomcat

wget https://downloads.apache.org/tomcat/tomcat-9/v9.0.52/bin/apache-tomcat-9.0.52.tar.gz

tar -xzvf apache-tomcat-9.0.52.tar.gz
sudo mv apache-tomcat-9.0.52 /usr/local/tomcat9

echo 'export CATALINA_HOME=/usr/local/tomcat9' >> ~/.bashrc
echo 'export PATH=$PATH:$CATALINA_HOME/bin' >> ~/.bashrc

sudo /usr/local/tomcat9/bin/startup.sh

#v2 Tomcat

sudo su
amazon-linux-extras install tomcat9
systemctl start tomcat
systemctl status tomcat

#SonarQube

wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-7.9.1.zip
unzip sonarqube-developer-7.9.1.zip
sudo mv -v sonarqube-7.9.1/* /opt/sonarqube
sudo chown -R sonar:sonar /opt/sonarqube
sudo chmod -R 775 /opt/sonarqube

echo 'export PATH=$PATH:$CATALINA_HOME/bin' >> ~/.bashrc

sudo ./sonar.sh start
