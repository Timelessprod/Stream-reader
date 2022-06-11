from pyspark.sql import SparkSession
import matplotlib.pyplot as plt
from datetime import datetime
import calendar


def get_data():
    spark = SparkSession.builder().appName("analyzer").getOrCreate()
    df = spark.read.csv("hdfs://localhost:9000/drone-reports")
    return df

def get_only_alerts(df):
    return df.filter(df.peaceScores < 50)

def graph_location(df):
    df = get_only_alerts(df)
    fig, ax = plt.subplots()
    ax.scatter(df.longitude, df.latitude)
    fig.show()

def get_hour(df):
    df = datetime.strptime(df.timestamp, "%Y-%m-%d %H:%M:%S").hour
    heights = [0 for i in range(25)]
    for elem in df:
        heights[elem.tointeger] += 1
    return heights

def graph_time_day(df):
    df = get_only_alerts(df)
    fig, ax = plt.subplots()
    columns = ["%ih".format(x) for x in range(25)]
    heights = get_hour(df)
    ax.bar(columns, heights)
    fig.show()

def get_day(df):
    df = datetime.strptime(df.timestamp, "%Y-%m-%d %H:%M:%S")
    heights = [0 for i in range(7)]
    for elem in df:
        heights[elem.weekday()] += 1
    return heights

def graph_time_week(df):
    df = get_only_alerts(df)
    fig, ax = plt.subplots()
    columns = ["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"]
    heights = get_day(df)
    ax.bar(columns, heights)
    fig.show()

def graph_evol_peacescore(df):
    df.withColumn("weekday", calendar.day_name[df.timestamp.weekday()])
    df = df.groupby("weekday").agg({"peaceScore":"avg"})
    fig, ax = plt.subplots()
    ax.bar(df.weekday, df.peaceScore)
    fig.show()


if __name__ == "__main__":
    df = get_data()
    graph_location(df)
    graph_time_day(df)
    graph_time_week(df)
    graph_evol_peacescore(df)