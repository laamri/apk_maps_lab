# MapApplication - Lab 13 : Localisation avec OpenStreetMap

## 📝 Description
Cette application Android (Java) permet de suivre la position géographique de l'utilisateur en temps réel, d'enregistrer ces coordonnées dans une base de données MySQL via une API PHP, et d'afficher l'historique des positions sur une carte interactive utilisant **OpenStreetMap (OSMDroid)**.

---

## 🎯 Objectifs Pédagogiques
1.  **Gestion des Permissions** : Demander et vérifier les permissions de localisation au runtime (Android 6.0+).
2.  **Services de Localisation** : Utiliser le `LocationManager` pour recevoir des mises à jour GPS.
3.  **Communication Réseau** : Utiliser la bibliothèque **Volley** pour envoyer (POST) et recevoir (GET) des données JSON.
4.  **Intégration de Cartographie** : Utiliser **OSMDroid** comme alternative gratuite à Google Maps.
5.  **Architecture Client-Serveur** : Connecter une application mobile à un backend PHP/MySQL local (XAMPP).

---

## 🏗️ Architecture du Projet

### 1. Partie Android (Client)
*   **MainActivity** : Gère l'interface d'accueil, les permissions et le suivi GPS en arrière-plan.
*   **GoogleMapActivity** : Affiche la carte et récupère les points enregistrés pour les transformer en marqueurs.
*   **Volley** : Gère les files d'attente de requêtes HTTP.
*   **OSMDroid** : Librairie de rendu de cartes OpenStreetMap.

### 2. Partie Backend (Serveur)
*   **createPosition.php** : Reçoit les coordonnées GPS (lat, lon, date, imei) et les insère dans MySQL.
*   **getPosition.php** : Retourne la liste de toutes les positions enregistrées au format JSON.
*   **Base de données** : Table `positions` stockant les trajectoires.

---

## 🛠️ Détails Techniques et Méthodes Clés

### MainActivity.java
*   `startLocationUpdates()` : Configure le `LocationManager` pour demander une mise à jour toutes les 60 secondes ou tous les 150 mètres.
*   `onLocationChanged(Location location)` : Callback déclenché à chaque mouvement, qui appelle `addPosition()`.
*   `addPosition(double lat, double lon)` : Crée une `StringRequest` (POST) pour envoyer les données au serveur. Utilise `Settings.Secure.ANDROID_ID` pour identifier l'appareil.

### GoogleMapActivity.java
*   `Configuration.getInstance().load(...)` : Initialise le cache d'OSMDroid.
*   `setUserAgentValue(...)` : **Crucial** pour éviter d'être bloqué par les serveurs de tuiles d'OpenStreetMap.
*   `loadPositions()` : Effectue une `JsonObjectRequest` (GET) vers le serveur et itère sur le tableau JSON pour créer des objets `Marker`.
*   `drawableToBitmap(Drawable drawable)` : Méthode utilitaire pour convertir les icônes Vectorielles (XML) en Bitmap pour les marqueurs de la carte.

---

## 🚀 Configuration et Installation

### Prérequis
*   Android Studio
*   XAMPP / WAMP
*   Émulateur Android avec accès Internet

### Étapes
1.  **Base de données** : Créer une BDD `map_project` et importer la table `positions` (voir SQL dans le dossier `php_backend`).
2.  **API PHP** : Copier le dossier `php_backend` dans `C:\xampp\htdocs\map_project\`.
3.  **Android** :
    *   Faire un **Gradle Sync** pour installer les dépendances.
    *   Vérifier que l'adresse IP dans le code est `10.0.2.2` (IP spéciale pour accéder au localhost de l'ordinateur depuis l'émulateur).
4.  **Permissions** : Accepter la localisation au premier lancement de l'application.

---

## 🔒 Sécurité
*   **Network Security Config** : Un fichier `xml/network_security_config.xml` est configuré pour autoriser le trafic en clair (HTTP) vers le serveur local, ce qui est nécessaire car Android bloque le HTTP par défaut depuis la version 9.

---

## 👤 Auteur
Projet réalisé dans le cadre du cours **Programmation Mobile : Android avec Java**.
