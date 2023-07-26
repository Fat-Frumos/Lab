yum -y update
yum -y install httpd
echo"<html>Centos</html>" > /var/www/html/index.html
service httpd start
chkconfig httpd on
cat index.html
echo "Userdata $(date)" >> /var/www/html/log/txt
