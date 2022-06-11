from faker import Faker
import json
import random

PEACE_SCORE_SCENARIO_PEACE = (0, 60)
PEACE_SCORE_SCENARIO_MEDIUM = (0, 80)
PEACE_SCORE_SCENARIO_HATE = (50, 100)
SCENARIO_LIST = [PEACE_SCORE_SCENARIO_PEACE, PEACE_SCORE_SCENARIO_MEDIUM, PEACE_SCORE_SCENARIO_HATE]

MAX_ID = 1_000_000

# List of very bad words
bad_words = ["fuck", "shit", "ass", "bitch", "dummy", "idiot", "bastard", "piss", "motherfucker", "kill", "arse", "bugger", "crap", "damn", "hell", "slut", "nigga", "cunt"]

# List of 20 very good words
good_words = ["good", "nice", "great", "awesome", "amazing", "wonderful", "fantastic", "beautiful", "perfect", "excellent", "super", "wonderful", "best", "excellent", "perfect", "super", "wonderful", "best", "excellent", "perfect", "super"]

# List of 20 random words
classic_words = ["flower", "door", "cat", "hello", "world", "dog", "car", "house", "tree", "computer", "mouse", "keyboard", "table", "window", "desk", "chair", "bed", "road", "street", "music", "school", "home", "vegetables", "food"]

# scenario 1 : very good situation
word_list_scenario_peace = random.sample(bad_words, 2) + random.sample(good_words, 21) + random.sample(classic_words, 15)

# scenario 2 : very bad situation
word_list_scenario_hate = random.sample(bad_words, 18) + random.sample(good_words, 2) + random.sample(classic_words, 15)

# scenario 3 : classic situation
word_list_scenario_medium = random.sample(bad_words, 5) + random.sample(good_words, 5) + random.sample(classic_words, 24)

fake = Faker()

def generate_peace_score_list(peace_score_range : tuple, max_citizen : int = 10) -> list:
    peace_score_list = []
    for _ in range(random.randint(1, max_citizen)):
        peace_score_dict = {
            "citizenId": random.randint(0, MAX_ID),
            "score": random.randint(peace_score_range[0], peace_score_range[1])
        }
        peace_score_list.append(peace_score_dict)
    
    return peace_score_list

def generate_single_report(latitude : float, longitude : float, word_list : list, peace_score_list : list):
    report = {
        "reportId": random.randint(0, MAX_ID),
        "peaceWatcherId": random.randint(0, MAX_ID),
        "time": str(fake.date_time_this_year()),
        "latitude": latitude,
        "longitude": longitude,
        "heardWords": word_list,
        "peaceScores": peace_score_list
    }
    return report

def generate_multi_report(n_report : int, peace_freq : float, medium_freq : float, hate_freq : float) -> dict:
    scenario_list = random.choices(SCENARIO_LIST, weights=[peace_freq, medium_freq, hate_freq], k=n_report)
    report_list = []
    for scenario in scenario_list:
        if scenario == PEACE_SCORE_SCENARIO_PEACE:
            latitude = random.uniform(0, 2)
            longitude = random.uniform(0, 2)
            word_list = [random.choice(word_list_scenario_peace) for _ in range(random.randint(1, 10))]
            peace_score_list = generate_peace_score_list(PEACE_SCORE_SCENARIO_PEACE)
        elif scenario == PEACE_SCORE_SCENARIO_MEDIUM:
            latitude = random.uniform(0, 2)
            longitude = random.uniform(0, 2)
            word_list = [random.choice(word_list_scenario_medium) for _ in range(random.randint(1, 10))]
            peace_score_list = generate_peace_score_list(PEACE_SCORE_SCENARIO_MEDIUM)
        elif scenario == PEACE_SCORE_SCENARIO_HATE:
            latitude = random.uniform(0, 1.2)
            longitude = random.uniform(0, 1.2)
            word_list = [random.choice(word_list_scenario_hate) for _ in range(random.randint(1, 10))]
            peace_score_list = generate_peace_score_list(PEACE_SCORE_SCENARIO_HATE)
        
        report = generate_single_report(latitude, longitude, word_list, peace_score_list)
        report_list.append(report)
    
    return {"reports": report_list}

def write_json_to_file(name, peace_freq : float, medium_freq : float, hate_freq : float):
    with open("json/" + name + ".json", "w") as f:
        report_list = generate_multi_report(n_report=1_000, peace_freq=peace_freq, medium_freq=medium_freq, hate_freq=hate_freq)
        f.write(json.dumps(report_list))


if __name__ == "__main__":
    # scenario 1 : very good situation
    write_json_to_file("s1", peace_freq=0.8, medium_freq=0.1, hate_freq=0.1)
    # scenario 2 : very bad situation
    write_json_to_file("s2", peace_freq=0.1, medium_freq=0.2, hate_freq=0.7)
    # scenario 3 : classic situation
    write_json_to_file("s3", peace_freq=0.3, medium_freq=0.4, hate_freq=0.3)