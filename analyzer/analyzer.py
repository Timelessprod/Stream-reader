from pyspark.sql import SparkSession
import matplotlib.pyplot as plt
from datetime import datetime
import calendar
import pyspark.sql.functions as F
import pandas as pd

def rename_columns(df, columns):
    if isinstance(columns, dict):
        return df.select(*[F.col(col_name).alias(columns.get(col_name, col_name)) for col_name in df.columns])
    else:
        raise ValueError("fail")

def get_data():
    spark = SparkSession.builder.appName("analyzer").getOrCreate()
    df = spark.read.csv("hdfs://localhost:9000/drone-reports")
    names = {"_c0": "reportId", "_c1": "peaceWatcherId", "_c2": "timestamp", "_c3": "longitude", "_c4": "latitude", "_c5": "hearWords", "_c6": "citizenId", "_c7": "peaceScores"}
    df = df.transform(lambda df: rename_columns(df, names))
    return df.toPandas()

def get_only_alerts(df):
    return df[df["peaceScores"].astype(int) > 50]

def graph_location(df):
    df = get_only_alerts(df)
    fig, ax = plt.subplots()
    ax.scatter(df.longitude.astype(float), df.latitude.astype(float))
    fig.show()

def get_hour(df):
    df = pd.datetime(df.timestamp.astype(str), format = "%Y-%m-%d %H:%M:%S").hour
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
    df = datetime.strptime(df.timestamp.astype(str), "%Y-%m-%d %H:%M:%S")
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
    ax.bar(df.weekday, df.peaceScores)
    fig.show()


if __name__ == "__main__":
    df = get_data()
    graph_location(df)
    graph_time_day(df)
    graph_time_week(df)
    graph_evol_peacescore(df)