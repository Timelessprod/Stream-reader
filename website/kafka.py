# ---------------------------------------------------------------------------- #
#                                    IMPORTS                                   #
# ---------------------------------------------------------------------------- #

from kafka import KafkaConsumer
import json

from config import parameters

# ---------------------------------------------------------------------------- #
#                                  PARAMETERS                                  #
# ---------------------------------------------------------------------------- #
KAFKA_ADDRESS = parameters["kafka_adress"]
KAFKA_PORT = parameters["kafka_port"]

# ---------------------------------------------------------------------------- #
#                                   FUNCTIONS                                  #
# ---------------------------------------------------------------------------- #
def get_alert():
    consumer = KafkaConsumer(
        "alerts", bootstrap_servers=["{}:{}".format(KAFKA_ADDRESS, KAFKA_PORT)]
    )
    new_messages = [json.loads(message)[0] for message in consumer]
    return []
