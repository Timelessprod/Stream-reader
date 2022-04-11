###### tags: `SPARK` `Projet`

# Synthèse des consignes

👉 [Le sujet du projet](https://docs.google.com/document/d/1HhRYSRrJ0bu8Qb4HncIYzhKcCU-pHknSEd40RgFnJis/edit)

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

## Question 1

> Avons nous vraiment besoin du Big Data pour ce problème?

Etant donné les remarques ci-dessous :
* La grande quantité de données à stocker (en un an, environ 365 * 200Gb = 73Tb de données stockées)
* Un accès rapide à ces données (tout particulièrement pour les alertes)
* Différents types de données
    * Chaine de caractère : Nom
    * Float : Longitude, latitude, PeaceScore
    * Liste de chaine de caractère : Mots entendus

on en déduit qu'une architecture de type Big Data est nécessaire à ce projet.

> Quels priorités choisir parmi les trois du thèorème CAP?

Analysons chaque composante CAP d'après la situation décrite dans le sujet :
* **Consistance (C)** : Elle n'est utile uniquement que dans l'éventualité où les drones seraient amener à coordonner leurs mouvements entre eux.
* **Disponibilité (A)** : Elle est importante dans tous les cas. En effet, si le système ne réagissait plus suffisament rapidement, les rapports des drones auraient tendance à s'accumuler avant d'être traité. On peut cependant supposer que certaines périodes (la nuit notamment) sont propices à une diminution du nombre de requêtes, et que le faible nombre de données générées par chaque drone indépendament peut être stocké dans l'appareil en attendant d'être traité.
* **Résistance au pannes (P)** : Elle est indispensable au projet, car le système d'alerte doit toujours être opérationnel dans une certaine mesure.

On déduit de l'analyse qu'il faudrait s'orienter vers une architecture *CP* ou *AP* afin de respecter les demandes. Nous allons cependant préférer l'architecture *AP*  car il ne nous est pas demandé de gérer la communication des drones entre eux, ce qui nous permet de négliger la consistance.

## Question 3

> Quelle(s) erreur(s) de Peaceland peut expliquer la tentative ratée ?

Le cahier des charges de Peaceland est très compréhensible mais manque de précision sur certains points qui peuvent expliquer l'échec de l'équipe de Data-Scientist:
* Le budget disponible n'est pas calirement précisé
    * On pourrait tenter d'atteindre les nécessités du cahier des charge en minimisant au maximum le coût de mise en place et d'entretien mais certaines nécessités sont assez floues. *Exemple* : "Les alertes doivent être déclenchées le **plus vite possible**". On aurait préféré *"Une alerte doit pouvoir être traitée en 1 seconde."*

La tentative ratée aurait été causée par la quantité de données qui ont surchargées le programme fait par l'équipe précédante. Ils n'auraient pas construit le programme pour être capable de traiter le scaling que pouvait subir leur programme. 
Ce scaling pouvait être causé par :
* certains jours où beaucoup de monde sortait, donc plus de données à traiter comparé à d'habitude (exemple : jour de marché)
* une augmentation de la population, causée par une augmentation de la natalité grâce à la paix constante dans le pays
  
## Question 4

> Quelles informations, oubliées dans le rapport du drone, pourraient augmenter l'efficacité des observateurs de Peaceland ?

Le rapport contient déjà :
* L'ID du Peacewatcher
* Sa position (longitude, latitude)
* Le nom des personnes observées ainsi que leur Peacescore actuel
* La liste de mots entendus
  
Pour augmenter l'efficacité des observateurs, on pourrait permettre aux PeaceMakers d'**anticiper les lieux d'intervention** et s'en rapprochant **en ajoutant la moyenne de Peacescore** ainsi que son évolution (i.e. sa dérivée). Cela permetrait d'anticiper les mouvements de foule ou autres paniques générales.
Car le Peacescore d'un individu est souvent influencé par celui de ceux autour de lui, connaitre la moyenne d'une zone permet d'agir en conséquence.