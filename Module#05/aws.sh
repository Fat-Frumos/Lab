#!/bin/bash

#RDS psql profile=prod

# enter SSH
ssh -i "virginia.pem" ec2-user@compute-1.amazonaws.com:8080/api/

sudo su

sudo yum install -y java-17-amazon-corretto
sudo dnf install postgresql15 -y

java -version
psql -version

psql --host=gift.cpwsodl3pdrd.us-east-1.rds.amazonaws.com --port=5432 --username=

CREATE DATABASE gift;


aws configure
export AWS_PROFILE=Alice
export AWS_DEFAULT_REGION=us-east-1
export AWS_DEFAULT_OUTPUT=yaml
export AWS_ACCESS_KEY_ID=
export AWS_SECRET_ACCESS_KEY=

#copy to S3
aws s3 cp s3://certificate-api-mjc--jar/web-app-1.0.0.jar .

java -jar -Dspring.profiles.active=prod web-app-1.0.0.jar --thin.dryrun

cat /etc/os-release

netstat -ntlp
netstat -nulp
curl ifconfig.me


ifconfig -a
curl ifconfig.me

#copy to S3
aws s3 cp ./web-app/target/web-app-1.0.0.jar s3://certificate-api-mjc--jar/web-app-1.0.0.jar

https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.PostgreSQL.html
https://docs.aws.amazon.com/prescriptive-guidance/latest/patterns/connect-by-using-an-ssh-tunnel-in-pgadmin.html

#copy to folder home
scp -i virginia.pem ./Lab/Module#04/web-app/target/web-app-1.0.0.jar ssh -i "virginia.pem" compute-1.amazonaws.com:/home/ec2-user/

wget https://certificate-api-mjc--jar.s3.eu-north-1.amazonaws.com/web-app-1.0.0.jar

java -jar web-app-1.0.0.jar --thin.dryrun

java -jar ./home/ec2-user/web-app-1.0.0.jar --thin.dryrun


chmod +x aws.sh
mvn -B package --file pom.xml

#S3 bucket

cd /home/ubuntu
aws s3 cp s3://certificate-api-mjc--jar/web-app-1.0.0.jar .

echo -e "[default]\nprofile = Alice\nregion = us-east-1\noutput = yaml" > ~/.aws/config
echo -e "[Alice]\naws_access_key_id = \naws_secret_access_key = " > ~/.aws/credentials

aws configure --profile Admin

export AWS_PROFILE=Alice

psql -U postgres pgec2
\l+
\q

export PGPASSWORD=

psql --host=gift.cpwsodl3pdrd.us-east-1.rds.amazonaws.com --port=5432 --username=postgres

psql -U postgres postgres <E:\Lab\Module#04\web-app\src\main\resources\db\gift.sql

PGPASSWORD= psql -h oregon-postgres.render.com -U phone_h5eq_user phone_h5eq <init.sql

netstat -ntlp
netstat -nulp
