import flask
import turbo_flask
import threading
import time

from Alert import Alert

app = flask.Flask(__name__)
turbo = turbo_flask.Turbo(app)

alert_list = []


@app.route("/")
def index():
    return flask.render_template("index.html")


@app.route("/delete/<id>")
def delete(id):
    global alert_list
    alert_list = list(filter(lambda x: x.id != id, alert_list))
    return flask.redirect(flask.url_for("index"))


def update_load():
    global alert_list
    with app.app_context():
        while True:
            time.sleep(5)
            alert_list.append(Alert("This is a test alert"))
            turbo.push(
                turbo.update(flask.render_template("alert_list.html"), "alert_list_div")
            )


@app.context_processor
def update_alert():
    return {"alert_list": alert_list}


@app.before_first_request
def before_first_request():
    threading.Thread(target=update_load).start()
