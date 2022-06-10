import flask
import turbo_flask
import threading
import time

from Alert import Alert

app = flask.Flask(__name__)
turbo = turbo_flask.Turbo(app)

alert_list = [Alert(message="A supprimer!")]


@app.route("/")
def index():
    return flask.render_template("index.html")


@app.route("/delete/<id>")
def delete(id):
    global alert_list
    alert_list = list(filter(lambda x: x.id != id, alert_list))
    return flask.redirect("/", code=302)


def update_load():
    with app.app_context():
        while True:
            time.sleep(4)
            turbo.push(
                turbo.update(flask.render_template("alert_list.html"), "alert_list_div")
            )


@app.context_processor
def update_alert():
    alert_list.append(Alert(message="Test Alerte"))
    print(len(alert_list))
    return {"alert_list": alert_list}


@app.before_first_request
def before_first_request():
    threading.Thread(target=update_load).start()
