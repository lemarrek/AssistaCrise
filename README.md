# Assista Crise - Plateforme d'Entraide Citoyenne

Ce projet a été réalisé dans le cadre du module **Génie Logiciel - INFO4**

## 📝 Contexte du Projet

Assista Crise est un service né de la nécessité de coordonner la solidarité citoyenne lors de crises majeures (incendies, inondations). La plateforme facilite la mise en relation entre les autorités locales, les sinistrés et les citoyens souhaitant apporter leur aide.

L'objectif de ce Devoir Maison était de modéliser le service, de générer le code initial via **JHipster** et d'analyser la qualité logicielle du système.

## 👥 Auteurs

* **Kelig LE MARREC** - *INFO4*
* **Emilio STIEN** - *INFO4*

## 📂 Structure du Dépôt

Le dépôt est organisé comme suit :

* `/assista-crise-app` : Le code source de l'application monolithique généré par JHipster (Spring Boot / Angular).
* `diag_classes.uml` : Le diagramme de classes au format PlantUML.
* `diag_classes.png` : L'export visuel du diagramme de classes.
* `diag_classes.jdl` : Le fichier JDL (JHipster Domain Language) utilisé pour définir les entités et relations.
* `DM GL - LE MARREC - STIEN.pdf` : Le rapport complet incluant l'analyse métrique et les réponses aux exercices.
* `url.txt` : Lien vers le dépôt Git distant.

## 🛠️ Technologies Utilisées

* **Modélisation** : PlantUML.
* **Génération de code** : [JHipster](https://www.jhipster.tech/) (Version 9.0.0).
* **Backend** : Java, Spring Boot.
* **Conteneurisation** : Docker.
* **Métrologie** : CLOC (Count Lines of Code) et SonarQube.

## 🚀 Installation et Génération

Pour reproduire la génération de l'application à partir du fichier JDL :

1. Assurez-vous que **Docker** est installé et lancé sur votre machine.
2. Placez-vous à la racine du projet.
3. Exécutez la commande suivante :
   ```bash
   docker run -it --rm -v "$PWD":/home/jhipster/app jhipster/jhipster jhipster jdl diag_classes.jdl