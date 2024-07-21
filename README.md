# SocialNetwork
Simple REST social network with upload multipart files in messages, jwt token and oauth2 google spring security 6.1. SocailNewtork nave Websocket Chat One to One and One to Many Chatroom with Kafka message broker.
If Kafka cannot connect and give error: Broker fails because all log dirs have failed
You need:
step 1 : point to a new log directory in server.properties file and save the file

log.dirs=C:\Tools\kafka_2.11-2.1.0\kafka-test-logs

step 2. Edit kafka servet config to new log.dids

step 3 : start the kafka server again

/bin/windows/kafka-server-start.bat /config/server.properties

