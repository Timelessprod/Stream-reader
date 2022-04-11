###### tags: `SPARK` `Projet`

# Synthèse des consignes


https://docs.google.com/document/d/1HhRYSRrJ0bu8Qb4HncIYzhKcCU-pHknSEd40RgFnJis/edit


## Objectif de l'architecture Big Data
* Stocker les données de chaque PeaceWatcher
* Déclencher des alertes
* Permettre aux PeaceMakers d'analyser les données des PeaceWatchers

## Drones
* Un rapport chaque minute
    * Id du PeaceWatcher
    * Location du PeaceWatcher (latitude, longitude)
    * Nom des citoyens à proximité avec leur *PeaceScore* du moment
    * Liste des mots entendu par le PeaceWatcher

## Alerte
* Générées le plus vite possible
    * Location du PeaceWatcher (latitude, longitude)
    * Nom du citoyen qui l'a déclenché.

## Statistiques
* Toutes les données doivent être conservées
* ~200Gb de données générées par jour
* < 1% d'alertes parmi les rapports

## Questions
1. Quelles sont les contraintes techniques/business auxquelles le composant de stockage de données de l'architecture du programme doit répondre pour satisfaire le besoin décrit par le client dans le paragraphe "Statistiques" ? 
De quel(s) type(s) de composant(s) (listés dans le cours) l'architecture aura-t-elle besoin ?
2. Quelle contrainte métier l'architecture doit-elle satisfaire pour répondre à l'exigence décrite dans le paragraphe "Alerte" ? Quel composant choisir ?
3. Quelle(s) erreur(s) de Peaceland peut expliquer la tentative ratée ?
4. Peaceland a probablement oublié certaines informations techniques dans le rapport envoyé par le drone. À l'avenir, cette information pourrait aider Peaceland à rendre ses observateurs de la paix beaucoup plus efficaces. Quelles informations ?



## Question 3

> Quelle(s) erreur(s) de Peaceland peut expliquer la tentative ratée ?

Le cahier des charges de Peaceland est très compréhensible mais manque de précision sur certains points qui peuvent expliquer l'échec de l'équipe de Data-Scientist:
* Le budget disponible n'est pas calirement précisé
    * On pourrait tenter d'atteindre les nécessités du cahier des charge en minimisant au maximum le coût de mise en place et d'entretien mais certaines nécessités sont assez floues. *Exemple* : "Les alertes doivent être déclenchées le **plus vite possible**". On aurait préféré *"Une alerte doit pouvoir être traitée en 1 seconde."*

La tentative ratée aurait été causée par la quantité de données qui ont surchargées le programme fait par l'équipe précédante. Ils n'auraient pas construit le programme pour être capable de traiter le scaling que pouvait subir leur programme. 
Ce scaling pouvait être causé par :
* certains jours où beaucoup de monde sortait, donc plus de données à traiter comparé à d'habitude (exemple : jour de marché)
* une augmentation de la population, causée par une augmentation de la natalité grâce à la paix constante dans le pays
