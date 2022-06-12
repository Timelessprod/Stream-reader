# Projet SPARK - Rétablir la paix à Peaceland
![](https://i.imgur.com/QnCbuqn.png)
## 1re partie
Dans cette partie, nous répondons à un ensemble de questions et on propose in fine une architecture pour le projet.
👉 [Sujet - 1re partie](https://docs.google.com/document/d/1HhRYSRrJ0bu8Qb4HncIYzhKcCU-pHknSEd40RgFnJis/edit)
👉 [Rapport - 1re partie](report.md)
👉 [Slides - 1re soutenance](https://epitafr-my.sharepoint.com/:p:/g/personal/corentin_duchene_epita_fr/Ef8EHwd_AyBEsPu17OsMEhwBQUCb9UU_JXsujEhEOKtmTQ?e=DnoBEk)

## 2e partie : le PoC
Dans cette partie, on propose une implémentation de l'architecture adaptée au projet.
👉 [Sujet - 2e partie](https://epitafr-my.sharepoint.com/:b:/g/personal/corentin_duchene_epita_fr/EVKM1gdUW1FDsERujF8ZAKUB_3VB_9Yprsm8oEMu7kxC6w?e=XdFEbo)
👉 [Slides - 2e soutenance](https://epitafr-my.sharepoint.com/:p:/g/personal/corentin_duchene_epita_fr/EUKFKppFublFg4hQwvsx-wsBuAqYnEoO0drIjZz4oM6PGA?e=rOsyU5)

## Composition du groupe :
| Nom | @epita.fr | Github |
| - | - | - |
| Erwan Goudard | `erwan.goudard` | `Grouane` |
| Adrien Merat | `adrien.merat` | `Timelessprod` |
| Corentin Duchêne| `corentin.duchene` | `Nigiva` |
| Henri Jamet | `henri.jamet` | `hjamet` |

## Pour lancer le projet
1. Installer ***Kafka***
2. Installer ***HDFS***
3. Installer ***Poetry***
4. Lancer Kafka : `start_kafka`
5. Créer les topics dans Kafka : `make create_topic`
6. Lancer le Producer avec le scénario 1 (citoyens heureux) : `make producer`
    Pour lancer le Producer avec d'autres scénarios comme 
    * 2 (citoyens en colère) : `cd producer && sbt "run ../json/s2.json"`
    * 3 (citoyens stressés) : `cd producer && sbt "run ../json/s3.json"`
7. Lancer le Consumer (HDFS) : `make consumer`
8. Lancer le site web de gestion des alertes PUPII (PeaceLandWatcher Ultimate Premium Imperator Interface) :
   * `cd website/`
   * `poetry shell` (on utilise Python 3.9.7 via `pyenv`)
   * `flask run`
