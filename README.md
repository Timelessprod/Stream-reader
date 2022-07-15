# SPARK Project - Restore peace to Peaceland

![](https://i.imgur.com/QnCbuqn.png)

## First Part : Theory and architecture of the solution

In this part, we answer a set of questions and we ultimately propose an architecture for the project.
👉 [Subject - 1st part](https://docs.google.com/document/d/1HhRYSRrJ0bu8Qb4HncIYzhKcCU-pHknSEd40RgFnJis/edit)
👉 [Report - 1st part](report.md)
👉 [Slides - 1st defense](https://epitafr-my.sharepoint.com/:p:/g/personal/corentin_duchene_epita_fr/Ef8EHwd_AyBEsPu17OsMEhwBQUCb9UU_JXsujEhEOKtmTQ?e=DnoBEk)

## Second part : The POC

In this part, we propose an implementation of the architecture adapted to the project.
👉 [Subject - 2nd part](https://epitafr-my.sharepoint.com/:b:/g/personal/corentin_duchene_epita_fr/EVKM1gdUW1FDsERujF8ZAKUB_3VB_9Yprsm8oEMu7kxC6w?e=XdFEbo)
👉 [Slides - 2nd defense](https://epitafr-my.sharepoint.com/:p:/g/personal/corentin_duchene_epita_fr/EUKFKppFublFg4hQwvsx-wsBuAqYnEoO0drIjZz4oM6PGA?e=rOsyU5)

## Collaborators of the project

| Name             | Email                     | Github account |
| ---------------- | ------------------------- | -------------- |
| Erwan Goudard    | erwan.goudard@epita.fr    | `Grouane`      |
| Adrien Merat     | adrien.merat@epita.fr     | `Timelessprod` |
| Corentin Duchêne | corentin.duchene@epita.fr | `Nigiva`       |
| Henri Jamet      | henri.jamet@epita.fr      | `hjamet`       |

## How to launch the project

In case you get a an error saying `Connection refused` or if you are using WSL, please restart your ssh service : `sudo service ssh restart`. If you get a warning while executing the consumer or the producer, just press `y` and enter.

1. Install ***ZooKeeper** and ***Kafka***
2. Inatall ***Hadoop***, ***HDFS*** and ***Spark***
3. Install ***Pyenv*** and ***Poetry***

:warning: Be sure to add binaries and shell scripts of the previous packages so they are accessible from anywhere on your computer

4. Open a terminal and launch the ZooKeeper server : `zookeeper-server-start.sh config/zookeeper.properties` (let it run)
5. In another terminal, create the Kafka topic : `kafka-topics.sh --create --topic "drone-report" --bootstrap-server localhost:9092` and then launch the Kafka server : `kafka-server-start.sh config/server.properties` (let it run)
6. In another terminal, start dfs and yarn services : `start-dfs.sh && start-yarn.sh`
7. In another terminal, launch the website for monitoring :
   * `cd website/`
   * `poetry shell` (we use Python 3.9.7 via Pyenv)
   * `flask run`
8. In another terminal, launch the streaming consumer : `cd consumer && sbt run` (let it run)
9. In another terminal, launch the streaming producer (chich will fake data send by drones by reading a json). Depending on the scenario you want to execute, execute one of the command below :
    * For happy citizens, launch : `cd producer && sbt "run ../json/s2.json"` (let it run)
    * For coleric citizens, launch : `cd producer && sbt "run ../json/s2.json"` (let it run)
    * For anxious citizens, launch : `cd producer && sbt "run ../json/s3.json"` (let it run)

10. To check that data is correctly written on HDFS, you can launch `hdfs dfs -ls /drone-reports`. It should print the files containing data.

To stop the application, simply kill all processus running in the opened terminal with `Ctrl+C` and run `stop-dfs.sh && stop-yarn.sh`
