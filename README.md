# Suivi de vos frais
Suivi de fiches de frais via une application android

## Guide d'installation de l'application sur serveur local.  
  
SuiviDeVosFrais nécessite l'installation d'une base de données et d'un d'un script de gestion de données sur serveur.  
**La base de données utilisée est la même que l'application AppliFrais réalisée en amont de cette application Android.**  
Si cette base de donnée n'a pas été installée, vous pourrez récupérer le script avec l'application serveur à l'adresse suivante  
https://github.com/Gdlsd/suividevosfrais-GestionFraisServer  
  
* Décompressez l'archive de "suividevosfrais-GestionFraisServer" dans le dossier www de wamp (C:/wamp64/www)   
* Si besoin, installez la base de données à l'aide du script SQL  
* Récupérez l'archive de SuiviDeVosFrais   
* A l'aide d'un éditeur de code, ouvrez la classe "AccesDistant" et insérez l'URL pointant sur "serveursuivifrais.php"
du fichier serveur dans la variable SERVERADDR (ligne 22) après avoir récupéré votre adresse IP (avec ipconfig)  
Vous devriez avoir une url du type http://<votre IP>/serveursuividevosfrais/serveursuivifrais.php  

L'application peut désormais communiquer avec le serveur  
  
  
## Ouvrir le projet avec Android Studio  
  
* Lancez Android Studio  
* Choisir "Start a new Android Studio project";  
* Dans "Application name", saisir "Suivi de vos frais" et dans "Company domain",  
saisir "emdsgil.cned.fr" puis cliquer ensuite sur "Next"  
* Ensuite, cochez "Phone and Tablet" si ce n'est pas déjà fait et choisir l'API 26 : Android8.0(Oreo) comme "Minimum SDK"  
* Décompressez l'archive dans le répertoire créé par Android Studio  
(normalement C:\Users\<votreCompte>\AndroidStudioProjects\Suividevosfrais\app\src)  
* Cliquez sur le menu "File" puis sur "Sync Projects with Gradle Files"  
* Cliquer sur le menu "Run" et sur "Run 'app'";  
* Cliquer sur "Create New Virtual Device";  
* Choisir la ligne "Phone", "Nexus 5 4,95" 1080x1920 xxhdpi", Next  
* Choisir la ligne avec les données suivantes et cliquez sur Next, puis Finish :  
    Release Name : Oreo  
    API Level : 26  
    ABI : x86  
    Target : Android 8.0 (Google Play)  
* De retour dans la fenêtre "Select Deployment Target", choisir dans "Available Virtual Devices" l'AVD "Nexus 5 API 26" et  
cliquer sur "OK"  
  
L'application existante devrait se lancer  




