.PHONY: run start_kafka create_topic producer consumer clean

TOPIC_REPORT="drone-report"
TOPIC_ALERT="drone-alert"

run: start_kafka create_topic consumer producer

# danger ca crée maybe des zombies donc a pas trop lancer sinon reboot PC
start_kafka:
	zookeeper-server-start.sh config/zookeeper.properties &
	kafka-server-start.sh config/server.properties &

create_topic:
	kafka-topics.sh --create --topic $(TOPIC_REPORT) --if-not-exists --bootstrap-server localhost:9092
	kafka-topics.sh --create --topic $(TOPIC_ALERT) --if-not-exists --bootstrap-server localhost:9092

producer:
	cd producer && sbt "run ../json/s1.json"

consumer:
	cd consumer && sbt run

# mettre un truc pour tuer les zombies ?
clean:
	rm -rf /tmp/kafka-logs /tmp/zookeeper