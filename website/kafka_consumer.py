# ---------------------------------------------------------------------------- #
#                                    IMPORTS                                   #
# ---------------------------------------------------------------------------- #

import logging
from kafka import KafkaConsumer
from kafka import KafkaProducer
import json

from config import parameters, bad_words, classical_words, good_words
from Alert import Alert
from Report import Report

# ---------------------------------------------------------------------------- #
#                                  PARAMETERS                                  #
# ---------------------------------------------------------------------------- #
KAFKA_ADDRESS = parameters["kafka_adress"]
KAFKA_PORT = parameters["kafka_port"]
ALERT_THRESHOLD = parameters["alert_threshold"]

# ---------------------------------------------------------------------------- #
#                             KAFKA INITIALISATION                             #
# ---------------------------------------------------------------------------- #

report_consumer = None
while report_consumer is None:
    try:
        report_consumer = KafkaConsumer(
            "drone-report",
            bootstrap_servers=["{}:{}".format(KAFKA_ADDRESS, KAFKA_PORT)],
        )
    except:
        logging.warning("Could not connect to Kafka to create report_consumer")

alert_consumer = None
while alert_consumer is None:
    try:
        alert_consumer = KafkaConsumer(
            "drone-alert",
            bootstrap_servers=["{}:{}".format(KAFKA_ADDRESS, KAFKA_PORT)],
        )
    except:
        logging.warning("Could not connect to Kafka to create alert consumer")

alert_producer = None
while alert_producer is None:
    try:
        alert_producer = KafkaProducer(
            bootstrap_servers="{}:{}".format(KAFKA_ADDRESS, KAFKA_PORT),
            value_serializer=lambda m: json.dumps(m.__dict__).encode("ascii"),
        )
    except:
        logging.warning("Could not connect to Kafka to create alert producer")

# ---------------------------------------------------------------------------- #
#                                   FUNCTIONS                                  #
# ---------------------------------------------------------------------------- #
def get_report():
    global report_consumer, alert_producer
    alert_list = []

    try:
        msg = json.loads(next(report_consumer).value)

        for report in msg["peaceScores"]:
            if report["score"] > ALERT_THRESHOLD:
                alert_list.append(
                    Alert(
                        msg["reportId"],
                        msg["latitude"],
                        msg["longitude"],
                        report["citizenId"],
                        str(report["score"]) + "%",
                    )
                )
        for alert in alert_list:
            alert_producer.send("drone-alert", alert)

        return Report(msg)
    except:
        logging.warning("Could not connect to Kafka to get reports")

    return None


def get_alert():
    global alert_consumer

    try:
        alert = json.loads(next(alert_consumer).value)
        return Alert(
            alert["_Alert__id"],
            alert["_Alert__latitude"],
            alert["_Alert__longitude"],
            alert["_Alert__citizen_id"],
            int(alert["_Alert__alert_level"][:-1]),
        )

    except:
        logging.warning("Could not connect to Kafka to get alerts")

    return None
