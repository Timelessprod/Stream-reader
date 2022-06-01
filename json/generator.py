from faker import Faker
import random

def generate_random_json(n, scenario, scores):
    fake = Faker()
    json = []
    for _ in range(n):
        words = [random.choice(scenario) for _ in range(random.randint(1, 10))]
        peaceScores = [{"citizenId": random.randint(1, 1000000), "score": random.randint(scores[0], scores[1])} for _ in range(random.randint(1, 10))]
        report = {
            "reportId": random.randint(0, 1000000),
            "peaceWatcherId": random.randint(0, 1000000),
            "time": str(fake.date_time_this_year()),
            "latitude": random.uniform(-90, 90),
            "longitude": random.uniform(-180, 180),
            "heardWords": words,
            "peaceScores": peaceScores
        }
        json.append(report)


    return json

# List of very bad words
bad_words = ["fuck", "shit", "ass", "bitch", "dummy", "idiot", "bastard", "piss", "motherfucker", "kill", "arse", "bugger", "crap", "damn", "hell", "slut", "nigga", "cunt"]

# List of 20 very good words
good_words = ["good", "nice", "great", "awesome", "amazing", "wonderful", "fantastic", "beautiful", "perfect", "excellent", "super", "wonderful", "best", "excellent", "perfect", "super", "wonderful", "best", "excellent", "perfect", "super"]

# List of 20 random words
classic_words = ["flower", "door", "cat", "hello", "world", "dog", "car", "house", "tree", "computer", "mouse", "keyboard", "table", "window", "desk", "chair", "bed", "road", "street", "music", "school", "home", "vegetables", "food"]

# scenario 1 : very bad situation
scenario_1 = random.sample(bad_words, 18) + random.sample(good_words, 2) + random.sample(classic_words, 15)

# scenario 2 : very good situation
scenario_2 = random.sample(bad_words, 2) + random.sample(good_words, 21) + random.sample(classic_words, 15)

# scenario 3 : classic situation
scenario_3 = random.sample(bad_words, 5) + random.sample(good_words, 5) + random.sample(classic_words, 24)


def write_json_to_file(name, scenario, scores):
    with open("json/" + name + ".json", "w") as f:
        f.write(str(generate_random_json(1000, scenario, scores)))


if __name__ == "__main__":
    write_json_to_file("s1", scenario_1, (0, 60))
    write_json_to_file("s2", scenario_2, (40, 100))
    write_json_to_file("s3", scenario_3, (0, 100))