keytool -genkey -alias selfsigned_localhost_sslserver -keyalg RSA -keysize 2048 -validity 700 -keypass secret  -storepass secret  -keystore ssl-server.jks

keytool -export -keystore ssl-server.jks -alias selfsigned_localhost_sslserver -file local-cert.crt

#centos
echo | openssl s_client -showcerts -servername gnupg.org -connect gnupg.org:443 2>/dev/null | openssl x509 -inform pem -noout -text
