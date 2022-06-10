.PHONY: run start_kafka create_topic producer consumer clean

TOPIC="drone-report"

run: start_kafka consumer producer

# danger ca crée maybe des zombies donc a pas trop lancer sinon reboot PC
start_kafka:
	zookeeper-server-start.sh config/zookeeper.properties &
	kafka-server-start.sh config/server.properties &

create_topic:
	kafka-topics.sh --create --topic $(TOPIC) --bootstrap-server localhost:9092

producer:
	cd producer && sbt run

consumer:
	cd consumer && sbt run

# mettre un truc pour tuer les zombies ?
clean:
	rm -rf /tmp/kafka-logs /tmp/zookeeper