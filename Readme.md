# Guide d'utilisation du server d'authentification

# Sommaire
1. [Recuperer token du server](#Recuperer)
   1. [Etape 1 : Recuperer le code du User](#Code)
   2. [Partie a lire pour les modifications sur cette etape. Pour l'utilisation, ignorez la](#CodeMod)
   3. [Etape 2 : Recuperer le token à partir du code](#token)
2. [Faire une requete vers un MS protégé](#reqMS)
3. [Sécuriser un MS](#secuMS)
   1. [Gestion du token](#gestToken)
   2. [Securisation des requetes](#reqSec)
   3. [Partie a lire pour les modifications sur cette etape. Pour l'utilisation, ignorez la](#reqSecMod)
4. [Reste à faire](#WIP)

## Recuperer token du server<a name="Recuperer" />
### Etape 1 : Recuperer le code du User<a name="Code" />
Faire un lien (pas une requete) du style : \
http://127.0.0.1:8000/oauth2/authorize?response_type=code&client_id=frontend&redirect_uri=http://127.0.0.1:4200/authorized&scope=openid read \
Le lien est a modifier selon le besoin, il est composé de plusieurs parties :
- l'URL du serveur oauth et son endpoint : http://127.0.0.1:8000/oauth2/authorize
- La reponse souhaité (dans notre cas code) : response_type=code
- Le client_id pour se connecter au server OAuth2 (voir le fichier AuthorizationServerConfiguration.java -> registeredClientRepository()) : client_id=frontend
- L'URL de redirection apres la requete ( le lien vers la page du front qui gere la reception du code ) ( Ne pas retirer "&scope=openid" du lien ) : redirect_uri=http://127.0.0.1:4200/authorized&scope=openid read

Ce lien redirige vers la page de login du server OAuth2 ( pas du micro service en general donc pour les modifications dans AuthorizationServerConfiguration.java ) dans le cas où l'utilisateur n'est pas connecté ou execute directement la requete.\
Apres que l'utilisateur se soit connecté redirige vers le lien dans la requete avec en parametre get le code de l'utilisateur

### Partie a lire pour les modifications sur cette etape. Pour l'utilisation, ignorez la<a name="CodeMod" />
Je pense qu'il serait preferable de modifier cette méthode.
J'ai pensé à deux solutions : 
- Personnaliser la page de login pour correspondre a notre site
- Trouver un moyens de se connecter à l'aide de requetes pour faire une connection depuis le front directement

### Etape 2 : Recuperer le token à partir du code<a name="token" />
Faire une requete post vers http://127.0.0.1:8000/oauth2/token \
Le Header de la requete doit contenir :
- 'Access-Control-Allow-Origin': '*'
- 'Content-Type': 'application/x-www-form-urlencoded'
- 'Authorization': 'Basic ' + btoa('client1:myClientSecretValue') ( important : mettre identifiant et secret dans la fonction btoa )

Le Body doit etre de type URLSearchParams et contenir :
- 'grant_type' : 'authorization_code'
- 'code' : {le code obtenu à l'etape 1}
- 'redirect_uri', 'http://127.0.0.1:4200/authorized' ( l'URL doit etre identique à celui qui à fait la requete )

Le token récuperé sera de la forme suivante : \
{\
&emsp; access_token : string;\
&emsp; expires_in : number;\
&emsp; id_token : string;\
&emsp; refresh_token : string;\
&emsp; scope : string;\
&emsp; token_type : string;\
}

## Faire une requete vers un MS protégé<a name="reqMS" />
Pour faire une requete vers un MS protege, il suffit simplement d'ajouter dans le Header de la requete :\
'Authorization': 'Bearer {access_token}'

## Sécuriser un MS<a name="secuMS" />

### Gestion du token<a name="gestToken" />
Recuperer les fichiers : https://drive.google.com/file/d/1E4T1fj714EnJypYCE2-vY_OirvF6nzF_/view?usp=sharing \
Placer application.yaml à la racine du projet\
Placer les deux autres fichiers dans un package config dans le dossier du main l'application

Modifications à effectuer sur les fichiers : \
Changer le issuer-uri dans application.yaml et dans WebSecurity.java pour correspondre à celui dans le server OAuth ( fichier AuthorizationServerConfiguration.java -> authorizationServerSettings)\
Changer l'audiences dans application.yaml pour correspondre au nom du client du server OAuth voulut ( fichier AuthorizationServerConfiguration.java -> registeredClientRepository )

### Securisation des requetes<a name="reqSec" />
Pour sécuriser une requete, ajouter l'annotation \
@PreAuthorize("hasRole('USER')")

### Partie a lire pour les modifications sur cette etape. Pour l'utilisation, ignorez la<a name="reqSecMod" />
Actuellement les requetes necessitent un token pour passer la securite, cependant on va avoir besoins de pouvoir faire des requetes sans se connecter.\
Pour resoudre ce probleme j'ai trouvé deux potentiel solutions :
- désactiver complétement la sécurité sur les requetes concernés mais je pense que cela peut mener à d'autres problemes de securité,
- soit créer un user public à partir d'un identifiant commun ayant le role non connecte.

## Reste à faire<a name="WIP" />
Apporter les modifications sur les étapes mentionnes
Changer le clientId et clientSecret dans AuthorizationServerConfiguration -> registeredClientRepository | A regarder car quand changer n'ouvre plus la page de login pour les requetes
Changer les options du CORS dans SpringSecurityConfiguration -> simpleCorsFilter pour matcher uniquement avec les origines necessaires
Recuperer les users de puis la bdd et les mettres dans SpringSecurityConfiguration -> users