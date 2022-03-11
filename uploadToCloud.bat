@echo off
scp -i key/id_rsa target\api-0.0.1-SNAPSHOT.jar root@137.184.121.135:/artifact
echo sango-api /Q

