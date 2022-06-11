from asyncore import read
import json
import csv

FILE_PATH = "s3.json"
CSV_PATH = "s3.csv"
HEADER = ["reportId", "peaceWatcherId", "time", "latitude", "longitude", "heardWords", "citizenId", "score"]

csv_file = open(CSV_PATH, 'w', encoding='UTF8')
writer = csv.writer(csv_file)
writer.writerow(HEADER)

with open(FILE_PATH, "r") as f:
    content = json.loads(f.read())

for report in content["reports"]:
    word_str = "|".join(report["heardWords"])
    for peace_dic in report["peaceScores"]:
        citizenId = peace_dic["citizenId"]
        score = peace_dic["score"]
        csv_row = [report["reportId"], report["peaceWatcherId"], report["time"], report["latitude"], report["longitude"], word_str, citizenId, score]
        writer.writerow(csv_row)

csv_file.close()

