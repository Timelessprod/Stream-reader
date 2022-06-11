import time


class Alert:
    def __init__(self, id, latitude, longitude, citizen_id, alert_level):
        self.__id = id
        self.__time = time.strftime("%l:%M%p %Z on %b %d, %Y")
        self.__latitude = latitude
        self.__longitude = longitude
        self.__citizen_id = citizen_id
        self.__alert_level = alert_level

    # ------------------------------ PUBLIC METHODS ------------------------------ #
    def get_id(self):
        return self.__id

    def get_time(self):
        return self.__time

    def get_latitude(self):
        return self.__latitude

    def get_longitude(self):
        return self.__longitude

    def get_citizen_id(self):
        return self.__citizen_id

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

    def set_citizen_id(self, citizen_id):
        self.__citizen_id = citizen_id

    def set_alert_level(self, alert_level):
        self.__alert_level = alert_level
