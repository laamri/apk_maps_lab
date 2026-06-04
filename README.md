# LAB 12 : Localisation temps réel via GPS et Google Maps

Ce projet est une application Android complète permettant de suivre la position GPS d'un appareil en temps réel, de stocker ces données dans une base de données MySQL via une API PHP, et de les afficher sur une carte Google Maps.

## 🚀 Fonctionnalités
- **Suivi GPS :** Capture de la latitude et longitude toutes les 10 secondes (optimisé pour éviter les plantages).
- **Backend PHP/MySQL :** Une API REST simple pour l'insertion et la récupération des positions.
- **Google Maps :** Affichage de tous les points enregistrés sous forme de marqueurs sur une carte interactive.
- **Identifiant Unique :** Utilisation de l'Android ID pour distinguer les appareils.

---

## 🛠️ Architecture du Projet

### 1. Partie Android (Java)
- **MainActivity :** Gère les permissions, l'écoute du signal GPS/Network et l'envoi des données via **Volley**.
- **MapsActivity :** Récupère les données JSON du serveur et les affiche sur Google Maps.
- **Permissions :** `ACCESS_FINE_LOCATION`, `INTERNET`, `READ_PHONE_STATE`.

### 2. Partie Backend (PHP)
Située dans le dossier `php_backend/` du projet, elle doit être déplacée vers votre serveur local (XAMPP/WAMP).
- `classe/Position.php` : Modèle de données.
- `connexion/Connexion.php` : Gestion de la connexion PDO.
- `service/PositionService.php` : Logique CRUD (Insert/Select).
- `createPosition.php` : Point d'entrée pour l'insertion (POST).
- `showPositions.php` : Point d'entrée pour la carte (JSON).

---

## ⚙️ Configuration et Installation

### Prérequis
1. **XAMPP** ou **WAMP** installé.
2. **Clé API Google Maps** (activez "Maps SDK for Android" sur Google Cloud Console).
3. Android Studio.

### Étapes
1. **Base de données :**
   - Créez une base `localisation` dans phpMyAdmin.
   - Exécutez le script SQL présent dans l'énoncé pour créer la table `position`.

2. **Serveur PHP :**
   - Copiez le contenu du dossier `php_backend` vers `C:\xampp\htdocs\localisation\`.
   - Vérifiez l'accès via `http://localhost/localisation/showPositions.php`.

3. **Android Studio :**
   - Dans `MainActivity.java` et `MapsActivity.java`, l'IP est configurée sur `10.0.2.2` (idéal pour l'émulateur). Pour un vrai téléphone, utilisez votre IP locale (`ipconfig`).
   - Insérez votre clé API dans le `AndroidManifest.xml` au niveau de `com.google.android.geo.API_KEY`.

---

## 🧪 Comment tester ?
1. Lancez l'application sur l'émulateur.
2. Ouvrez les **Extended Controls (...)** -> **Location**.
3. Recherchez une ville et cliquez sur **Set Location**.
4. Attendez le message "Enregistré !" ou vérifiez votre base de données MySQL.
5. Cliquez sur **AFFICHER MAP** pour voir vos points sur la carte.

---

## 📝 Auteur
*TP Programmation Mobile - Android Java*
