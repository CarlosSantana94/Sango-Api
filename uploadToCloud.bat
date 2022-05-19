@echo off
scp -i key/id_rsa target\api-0.0.1-SNAPSHOT.jar root@137.184.121.135:/artifact
sango-api
ssh -i key/id_rsa root@137.184.121.135

sudo systemctl stop start-api
systemctl start start-api.service
systemctl enable start-api.service
systemctl status start-api

view Log:
journalctl -u start-api.service --no-pager -e -f
