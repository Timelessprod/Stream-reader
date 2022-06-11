# ---------------------------------------------------------------------------- #
#                                    IMPORTS                                   #
# ---------------------------------------------------------------------------- #

import logging
from kafka import KafkaConsumer
import json

from config import parameters, bad_words, classical_words, good_words
from Alert import Alert
from Report import Report

# ---------------------------------------------------------------------------- #
#                                  PARAMETERS                                  #
# ---------------------------------------------------------------------------- #
KAFKA_ADDRESS = parameters["kafka_adress"]
KAFKA_PORT = parameters["kafka_port"]

# ---------------------------------------------------------------------------- #
#                             KAFKA INITIALISATION                             #
# ---------------------------------------------------------------------------- #

consumer = None
while consumer is None:
    try:
        consumer = KafkaConsumer(
            "drone-report",
            bootstrap_servers=["{}:{}".format(KAFKA_ADDRESS, KAFKA_PORT)],
        )
    except:
        logging.warning("Could not connect to Kafka")

# ---------------------------------------------------------------------------- #
#                                   FUNCTIONS                                  #
# ---------------------------------------------------------------------------- #
def get_alert_and_report():
    global consumer
    try:
        msg = json.loads(next(consumer).value)

        for report in msg["peaceScores"]:
            if report["score"] > 50:
                return Alert(
                    msg["reportId"],
                    msg["latitude"],
                    msg["longitude"],
                    report["citizenId"],
                    str(report["score"]) + "%",
                )
        return Report(msg)
    except:
        logging.warning("Could not connect to Kafka")

    return None
