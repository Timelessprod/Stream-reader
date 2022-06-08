.PHONY: run start_kafka create_topic producer clean

TOPIC="drone-report"

run: start_kafka create_topic producer

start_kafka:
	bin/zookeeper-server-start.sh config/zookeeper.properties
	bin/kafka-server-start.sh config/server.properties

create_topic:
	bin/kafka-topics.sh --create --topic $(TOPIC) --bootstrap-server localhost:9092

producer:
	cd producer && sbt run

clean:
	rm -rf /tmp/kafka-logs /tmp/zookeeper