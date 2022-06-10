import flask
import turbo_flask
import threading
import time

app = flask.Flask(__name__)
turbo = turbo_flask.Turbo(app)

alert_list = []


@app.route("/")
def hello_world():
    return flask.render_template("index.html")


def update_load():
    with app.app_context():
        while True:
            time.sleep(0.3)
            turbo.push(
                turbo.update(flask.render_template("alert_list.html"), "alert_list_div")
            )


@app.context_processor
def update_alert():
    alert_list.append("Test Alert!")
    return {"alert_list": alert_list}


@app.before_first_request
def before_first_request():
    threading.Thread(target=update_load).start()
