import time


class Report:
    def __init__(self, latitude, longitude, citizen_name):
        self.__id = str(time.time())
        self.__time = time.strftime("%l:%M%p %Z on %b %d, %Y")
        self.__latitude = latitude
        self.__longitude = longitude
        self.__citizen_name = citizen_name
        self.__alert_level = 0

    # ------------------------------ PUBLIC METHODS ------------------------------ #
    def get_id(self):
        return self.__id

    def get_time(self):
        return self.__time

    def get_latitude(self):
        return self.__latitude

    def get_longitude(self):
        return self.__longitude

    def get_first_name(self):
        name = self.__citizen_name.split()
        return name[0] if len(name) > 1 else name

    def get_last_name(self):
        name = self.__citizen_name.split()
        return " ".join(name[1:]) if len(name) > 1 else "?"

    def get_alert_level(self):
        return self.__alert_level

    def set_id(self, id):
        self.__id = id

    def set_time(self, time):
        self.__time = time

    def set_latitude(self, latitude):
        self.__latitude = latitude

    def set_longitude(self, longitude):
        self.__longitude = longitude

    def set_citizen_name(self, citizen_name):
        self.__citizen_name = citizen_name

    def set_alert_level(self, alert_level):
        self.__alert_level = alert_level
