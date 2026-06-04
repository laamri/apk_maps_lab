# LAB 11 : GPS et Map (Google Maps Activity)

Ce projet est une application Android développée en Java permettant d'intégrer Google Maps et de suivre la position de l'utilisateur en temps réel.

## 🚀 Fonctionnalités
- **Affichage Google Maps** : Intégration complète de la carte via `SupportMapFragment`.
- **Localisation en temps réel** : Utilisation du `LocationManager` pour écouter les changements de position.
- **Gestion des Permissions** : Demande dynamique (Runtime Permissions) pour l'accès à la position (`ACCESS_FINE_LOCATION`).
- **Marqueur Dynamique** : Un marqueur unique qui se déplace sur la carte selon les mouvements de l'utilisateur.
- **Suivi Caméra** : La caméra se centre et zoome automatiquement sur la nouvelle position avec une animation fluide.
- **Gestion du GPS** : Détection automatique si le GPS est désactivé avec affichage d'une boîte de dialogue pour renvoyer vers les réglages système.

## demo vedio



https://github.com/user-attachments/assets/816ee38d-9eae-4359-ac73-a833959addf2




## 🛠️ Installation & Configuration

### 1. Clé API Google Maps
Pour que la carte s'affiche, vous devez configurer une clé API :
1. Allez sur la [Google Cloud Console](https://console.cloud.google.com/).
2. Activez le **Maps SDK for Android**.
3. Créez une clé API.
4. Dans le projet, ouvrez `app/src/main/AndroidManifest.xml` et remplacez la valeur de `com.google.android.geo.API_KEY` par votre clé.

### 2. Permissions
L'application requiert les permissions suivantes (déjà configurées dans le Manifest) :
- `INTERNET`
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`

## 📖 Explication du Code (Détails Techniques)

### MapsActivity.java
- **`onMapReady(GoogleMap googleMap)`** : Point d'entrée quand la carte est prête. On y initialise le `LocationManager`.
- **`startLocationUpdates()`** : Vérifie si l'utilisateur a autorisé la localisation. Si oui, elle enregistre un écouteur (`requestLocationUpdates`) sur le fournisseur `NETWORK_PROVIDER` (ou `GPS_PROVIDER`).
- **`updateMarker(Location location)`** : Cette méthode est appelée à chaque changement de position. Elle vérifie si un marqueur existe déjà :
    - Si non, elle le crée (`addMarker`).
    - Si oui, elle met juste à jour sa position (`setPosition`).
- **`animateCamera()`** : Permet de suivre l'utilisateur sans mouvement brusque de la carte.
- **`buildAlertMessageNoGps()`** : Une boîte de dialogue `AlertDialog` qui empêche l'utilisation tant que le GPS n'est pas activé, guidant l'utilisateur vers les paramètres.

### Gestion des Permissions
Le code utilise `ActivityCompat.requestPermissions` et gère le retour dans `onRequestPermissionsResult`. Si l'utilisateur accepte, la localisation démarre immédiatement sans redémarrer l'application.

## 📱 Tests
- Utilisez un appareil réel pour une meilleure précision GPS.
- Sur l'émulateur, utilisez les "Extended Controls" (section Location) pour simuler des trajets en envoyant différents points GPS vers l'application.
