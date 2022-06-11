# ---------------------------------------------------------------------------- #
#                                    IMPORTS                                   #
# ---------------------------------------------------------------------------- #
import logging
import flask
import kafka_consumer
import turbo_flask
import threading
import time

from Alert import Alert
from Report import Report
from config import parameters

# ---------------------------------------------------------------------------- #
#                                  PARAMETERS                                  #
# ---------------------------------------------------------------------------- #
UPDATE_TIME = parameters["web_update_time"]
REPORT_NUMBER = parameters["report_number"]

# ---------------------------------------------------------------------------- #
#                                INITIALISATION                                #
# ---------------------------------------------------------------------------- #

if __name__ == "app" or __name__ == "__main__":
    app = flask.Flask(__name__)
    turbo = turbo_flask.Turbo(app)
else:
    logging.error("This file is not meant to be imported")
    exit(1)

alert_list = []
report_list = []


def update_load():
    global alert_list, report_list
    with app.app_context():
        while True:
            time.sleep(UPDATE_TIME)
            # alert_list.append(Alert(str(time.time()), 0, 0, "Nom du citoyen!"))
            # report_list.append(
            #     Report({"test": 1, "bebe": "truc", "poulpe": [1, 2, "pouce"]})
            # )
            res = kafka_consumer.get_alert_and_report()
            if res is not None:
                alert_list.extend(res[0])
                report_list.append(res[1])
            report_list = report_list[-REPORT_NUMBER:]
            turbo.push(
                turbo.update(flask.render_template("alert_list.html"), "alert_list_div")
            )
            turbo.push(
                turbo.update(
                    flask.render_template("report_list.html"), "report_list_div"
                )
            )


# ---------------------------------------------------------------------------- #
#                                   CALLBACKS                                  #
# ---------------------------------------------------------------------------- #
@app.route("/")
def index():
    return flask.render_template("index.html")


@app.route("/delete/<int:id>")
def delete(id):
    global alert_list
    alert_list = list(filter(lambda x: x.get_id() != id, alert_list))
    return flask.redirect(flask.url_for("index"))


@app.context_processor
def update_dynamic():
    return {"alert_list": alert_list, "report_list": report_list}


@app.before_first_request
def before_first_request():
    threading.Thread(target=update_load).start()
