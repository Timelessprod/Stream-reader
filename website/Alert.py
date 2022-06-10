import time


class Alert:
    def __init__(self, message):
        self.id = str(time.time())
        self.message = message
