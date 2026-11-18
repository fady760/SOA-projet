# Documentation Complète du Projet SOA : Séries & Épisodes (`fady_projet_soa`)

---

## Sommaire
1. [Vue d'Ensemble & Fonctionnalités du Projet](#1-vue-densemble--fonctionnalités-du-projet)
2. [Architecture Globale du Système (SOA / Microservices)](#2-architecture-globale-du-système-soa--microservices)
3. [Exploration Détaillée du Backend (Dossier par Dossier)](#3-exploration-détaillée-du-backend-dossier-par-dossier)
   - [A. Le Microservice Métier : `serie_episode` (Port 8082)](#a-le-microservice-métier--serie_episode-port-8082)
   - [B. Le Microservice d'Authentification : `users-microservice` (Port 8081)](#b-le-microservice-dauthentification--users-microservice-port-8081)
4. [Exploration Détaillée du Frontend Angular (`serie_episode_angular`)](#4-exploration-détaillée-du-frontend-angular-serie_episode_angular)
   - [A. Comprendre l'Architecture Angular à partir de zéro](#a-comprendre-larchitecture-angular-à-partir-de-zéro)
   - [B. Explication de chaque Dossier et Fichier](#b-explication-de-chaque-dossier-et-fichier)
5. [Plongée au Cœur de la Sécurité (Explication en Détail et Accessible)](#5-plongée-au-cœur-de-la-sécurité-explication-en-détail-et-accessible)
   - [Le concept du JWT (JSON Web Token)](#le-concept-du-jwt-json-web-token)
   - [Cycle de vie complet d'une requête sécurisée](#cycle-de-vie-complet-dune-requête-sécurisée)
   - [Exemples Démonstratifs de Protection](#exemples-démonstratifs-de-protection)
6. [Guide de Test Complet (Fonctionnalités, JWT & Sécurité)](#6-guide-de-test-complet-fonctionnalités-jwt--sécurité)

---

## 1. Vue d'Ensemble & Fonctionnalités du Projet

Ce projet est une application web moderne complète (**Full Stack**) conçue selon une architecture orientée services (**SOA / Microservices**). Elle permet la gestion complète de **Séries télévisées** et de leurs **Épisodes** respectifs avec un contrôle d'accès strict par rôles (**RBAC** : *Role-Based Access Control*).

### Ce que l'utilisateur peut faire :

#### 1. Authentification & Gestion de Compte
* **Inscription (`Register`)** : Créer un nouveau compte utilisateur avec validation réactive des champs (nom d'utilisateur, format de l'e-mail, mot de passe d'au moins 6 caractères, confirmation identique).
* **Vérification par E-mail** : Réception d'un code secret à 6 chiffres par e-mail (avec affichage de secours dans la console serveur) valide pendant 15 minutes pour activer le compte.
* **Connexion (`Login`)** : Authentification via le microservice de sécurité avec génération et stockage local d'un jeton JWT (*JSON Web Token*).
* **Déconnexion (`Logout`)** : Nettoyage du jeton et redirection automatique vers la page de connexion.
* **Persistance de Session** : Décodage automatique du jeton au rechargement de la page pour maintenir l'utilisateur connecté sans redemander ses identifiants.

#### 2. Consultation (Disponible pour les utilisateurs `USER` et `ADMIN`)
* **Liste des Épisodes** : Affichage tabulaire élégant de tous les épisodes (ID, titre, numéro, durée en minutes, saison, série rattachée).
* **Recherche par Série** : Sélecteur déroulant de séries permettant de filtrer instantanément les épisodes appartenant à une série sélectionnée.
* **Recherche par Titre (3 modalités interactives)** :
  1. *Via API REST* : Saisie du nom et clic sur un bouton "Rechercher" qui appelle le backend Spring Boot (`/api/episodesByTitre/{titre}`).
  2. *Filtrage instantané (Keyup)* : Filtrage en temps réel dans la mémoire du composant au fur et à mesure de la frappe.
  3. *Filtrage via Pipe Angular* : Utilisation d'un `Pipe` personnalisé (`SearchFilterPipe`) pour filtrer directement les données dans le template HTML.
* **Liste des Séries** : Consultation de toutes les séries enregistrées (ID, titre, genre, année de début).

#### 3. Administration (Réservé exclusivement au rôle `ADMIN`)
* **Ajout d'Épisode** : Formulaire permettant de créer un épisode et de l'associer à une série existante.
* **Modification d'Épisode** : Possibilité de modifier le titre, le numéro, la saison, la durée et la série d'un épisode existant.
* **Suppression d'Épisode** : Suppression avec confirmation préalable.
* **Gestion complète des Séries** : Ajout, modification en ligne (via un composant imbriqué `UpdateSerieComponent`) et suppression de séries entières.

---

## 2. Architecture Globale du Système (SOA / Microservices)

Le système est découpé en **trois applications indépendantes** qui collaborent :

```
                                  +---------------------------------------+
                                  |     Frontend Angular (Port 4200)      |
                                  |        serie_episode_angular          |
                                  +---------------------------------------+
                                        /                           \
               1. Demande d'authentification                         2. Requêtes API métier
               (Login, Register, Validation)                         (GET, POST, PUT, DELETE)
               + Réception du Jeton JWT                              + En-tête : Authorization: Bearer <JWT>
                                      /                               \
                                     v                                 v
          +--------------------------------------+     Secret Partagé    +--------------------------------------+
          |   Microservice Auth (Port 8081)      | <===================> |     Microservice Métier (Port 8082)  |
          |         users-microservice           | HMAC256(SecParams)    |             serie_episode            |
          |--------------------------------------|                       |--------------------------------------|
          | • Port d'écoute : 8081               |                       | • Port d'écoute : 8082               |
          | • Contexte : /users                  |                       | • Contexte : /serie_episode          |
          | • Rôle : Fabrique & Vérifie les JWT  |                       | • Rôle : Gestion Séries/Épisodes     |
          | • Gère les utilisateurs et les rôles |                       | • Filtre JWT & Contrôle d'accès      |
          | • BDD : users_db (MySQL)             |                       | • BDD : serie_episode_db (MySQL)     |
          +--------------------------------------+                       +--------------------------------------+
```

---

## 3. Exploration Détaillée du Backend (Dossier par Dossier)

Le backend est scindé en deux projets Spring Boot distincts.

```
fady_projet_soa/
├── serie_episode/          <-- Microservice Métier (Gestion Séries & Épisodes)
└── users-microservice/     <-- Microservice d'Authentification (Gestion Comptes & JWT)
```

---

### A. Le Microservice Métier : `serie_episode` (Port 8082)

Ce service est un **Serveur de Ressources** (*Resource Server*). Il ne gère pas les mots de passe ; il se contente de vérifier la validité du jeton JWT fourni par le client.

#### 1. Fichiers de Configuration Racine
* `pom.xml` : Définit les dépendances Maven :
  * `spring-boot-starter-web` : Crée l'API REST HTTP.
  * `spring-boot-starter-data-jpa` : Interface ORM avec la base MySQL via Hibernate.
  * `spring-boot-starter-security` : Fournit la chaîne de filtres de sécurité.
  * `java-jwt` (`com.auth0`) : Bibliothèque permettant de décoder et vérifier la signature cryptographique des jetons JWT.
  * `mysql-connector-j` : Pilote JDBC pour MySQL.
  * `lombok` : Génération automatique des getters, setters et constructeurs.
* `src/main/resources/application.properties` :
  * `server.port=8082` : Port d'écoute (change de 8081 à 8082 pour éviter le conflit avec le microservice users).
  * `server.servlet.context-path=/serie_episode` : Préfixe de toutes les URLs du service.
  * `spring.datasource.url=jdbc:mysql://localhost:3306/serie_episode_db...` : Connexion à la base de données métier.
  * `spring.jpa.hibernate.ddl-auto=update` : Met à jour automatiquement la structure des tables MySQL selon les entités Java.

#### 2. Package `entities` (`com.fady.serie_episode.entities`)
* **`Serie.java`** :
  * Représente la table `serie`.
  * Utilise Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`).
  * Contient la relation `@OneToMany(mappedBy = "serie") private List<Episode> episodes;`.
  * `@JsonIgnore` : Empêche la boucle infinie de sérialisation JSON (`Serie` $\rightarrow$ `Episode` $\rightarrow$ `Serie` $\dots$).
* **`Episode.java`** :
  * Représente la table `episode`.
  * Clé primaire auto-incrémentée (`@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`).
  * Relation `@ManyToOne private Serie serie;` qui génère la clé étrangère `serie_id_serie`.
  * Dispose de constructeurs adaptés pour créer des épisodes avec ou sans série liée.
* **`EpisodeProjection.java`** :
  * Projection Spring Data REST permettant d'extraire uniquement le titre de l'épisode si le paramètre `?projection=titreEp` est passé.

#### 3. Package `repos` (`com.fady.serie_episode.repos`)
* **`SerieRepository.java`** : Interface étendant `JpaRepository<Serie, Long>`. Offre sans coder les méthodes CRUD standard (`findAll`, `findById`, `save`, `deleteById`).
* **`EpisodeRepository.java`** : Interface étendant `JpaRepository<Episode, Long>` avec des requêtes personnalisées :
  * `findByTitreEpisodeContains(String titre)` : Recherche partielle (`LIKE %titre%`).
  * `findBySerieIdSerie(Long id)` : Recherche tous les épisodes rattachés à une série donnée.
  * `@Query("select e from Episode e where e.serie = ?1")` : Requête JPQL orientée objet.

#### 4. Package `service` (`com.fady.serie_episode.service`)
* **`EpisodeService.java`** : L'interface métier définissant le contrat des opérations autorisées sur les épisodes.
* **`EpisodeServiceImpl.java`** : Implémente `EpisodeService`. Marqué `@Service`, il injecte `EpisodeRepository` pour exécuter la logique métier et les requêtes.

#### 5. Package `restcontrollers` (`com.fady.serie_episode.restcontrollers`)
* **`EpisodeRESTController.java`** :
  * Exposé sur `/api`.
  * `GET /api` ou `/api/all` : Retourne la liste de tous les épisodes.
  * `GET /api/{id}` ou `/api/getbyid/{id}` : Retourne un épisode précis.
  * `POST /api` ou `/api/addepisode` : Crée un nouvel épisode (sécurisé ADMIN).
  * `PUT /api` ou `/api/updateepisode` : Met à jour un épisode (sécurisé ADMIN).
  * `DELETE /api/{id}` ou `/api/delepisode/{id}` : Supprime un épisode (sécurisé ADMIN).
  * `GET /api/episodesserie/{idSerie}` : Épisodes d'une série donnée.
  * `GET /api/episodesByTitre/{titre}` : Recherche par titre.
* **`SerieRESTController.java`** :
  * Exposé sur `/series`.
  * Fournit les endpoints CRUD pour gérer les séries télévisées.

#### 6. Package `security` (`com.fady.serie_episode.security`)
* **`SecParams.java`** : Contient la constante secrète (`SECRET = "bahrihajer14@gmail.com"`), le préfixe (`"Bearer "`), et la durée de validité (10 jours). **Ce secret doit être strictement identique à celui de `users-microservice`**.
* **`JWTAuthorizationFilter.java`** : Filtre qui s'exécute à chaque requête HTTP reçue :
  1. Lit l'en-tête `Authorization`.
  2. Vérifie la présence du préfixe `Bearer `.
  3. Vérifie cryptographiquement la signature avec l'algorithme `HMAC256` et le secret partagé.
  4. Extrait le nom d'utilisateur (`subject`) et les rôles (`roles`).
  5. Transmet les autorisations à Spring Security (`SecurityContextHolder`).
* **`SecurityConfig.java`** :
  * Configure la session en mode **STATELESS** (sans cookies de session).
  * Désactive le CSRF (inutile avec les jetons JWT).
  * Configure le **CORS** pour autoriser `http://localhost:4200` et exposer l'en-tête `Authorization`.
  * Définit les règles de droits :
    * Toutes les requêtes `GET` sur `/api/**`, `/series/**`, `/rest/**` $\rightarrow$ permises à `ADMIN` et `USER`.
    * Toutes les requêtes `POST`, `PUT`, `DELETE` $\rightarrow$ réservées à `ADMIN`.
    * Place `JWTAuthorizationFilter` avant `UsernamePasswordAuthenticationFilter`.

---

### B. Le Microservice d'Authentification : `users-microservice` (Port 8081)

Ce microservice est le **Serveur d'Authentification** (*Authentication Server*). C'est lui qui gère la création des comptes, le hachage des mots de passe, et la génération des jetons JWT signés.

#### 1. Fichiers de Configuration
* `src/main/resources/application.properties` :
  * `server.port=8081` : Tourne sur le port 8081.
  * `server.servlet.context-path=/users` : Préfixe de toutes les URLs.
  * `spring.datasource.url=.../users_db` : Base de données dédiée aux comptes utilisateurs.
  * Configuration SMTP Gmail pour l'envoi des codes de vérification par mail.

#### 2. Entités (`com.hajer.users.entities`)
* **`User.java`** : Stocke l'ID, le nom d'utilisateur unique, le mot de passe crypté, l'adresse e-mail, le statut actif (`enabled`), et la liste de ses rôles via une table de jointure `user_role`.
* **`Role.java`** : Représente un rôle système (`ADMIN`, `USER`).
* **`VerificationToken.java`** : Stocke un code secret aléatoire à 6 chiffres lié à un utilisateur avec une date d'expiration fixée à 15 minutes.

#### 3. Logique d'Inscription & Vérification (`service`)
* **`UserServiceImpl.java`** :
  * `registerUser(...)` : Vérifie que l'e-mail et le nom d'utilisateur ne sont pas déjà pris, hache le mot de passe avec **BCrypt**, attribue par défaut le rôle `USER`, désactive le compte (`enabled=false`), génère un code à 6 chiffres et tente de l'envoyer par e-mail (avec affichage console en cas de secours).
  * `validateToken(code)` : Vérifie si le code existe et n'a pas expiré. Si tout est valide, il active le compte (`enabled=true`).

#### 4. Filtre d'Authentification (`JWTAuthenticationFilter.java`)
* Déclenché lors d'un `POST` sur `/users/login` :
  1. `attemptAuthentication` : Lit le corps JSON de la requête (`username` et `password`) et le soumet au gestionnaire d'authentification (`AuthenticationManager`).
  2. `successfulAuthentication` : Si les identifiants sont corrects, Spring crée le jeton JWT avec les revendications (*claims*) : sujet = `username`, rôles = `[ADMIN, USER]`, expiration = 10 jours, et signe le tout avec `HMAC256(SECRET)`. Le jeton est retourné dans l'en-tête HTTP `Authorization: Bearer <jeton>`.
  3. `unsuccessfulAuthentication` : Si l'utilisateur est désactivé (e-mail non encore vérifié), retourne un statut `403` avec un message d'erreur clair.

---

## 4. Exploration Détaillée du Frontend Angular (`serie_episode_angular`)

```
fady_projet_soa/
└── serie_episode_angular/
    ├── angular.json
    ├── package.json
    └── src/
        ├── index.html
        ├── main.ts
        ├── styles.css
        ├── environments/
        └── app/
            ├── app.config.ts
            ├── app.routes.ts
            ├── app.component.*
            ├── auth.guard.ts
            ├── episode.guard.ts
            ├── search-filter.pipe.ts
            ├── model/
            ├── services/
            ├── login/
            ├── register/
            ├── verif-email/
            ├── episodes/
            ├── add-episode/
            ├── update-episode/
            ├── liste-series/
            ├── update-serie/
            ├── recherche-par-serie/
            ├── recherche-par-titre/
            └── forbidden/
```

### A. Comprendre l'Architecture Angular à partir de zéro

Si vous débutez avec Angular, voici les concepts fondamentaux expliqués simplement :

1. **SPA (Single Page Application)** : L'application ne recharge **jamais** la page entière dans le navigateur. Le fichier `index.html` est chargé une seule fois. Dès que l'utilisateur clique sur un lien (ex: `/episodes`), Angular remplace dynamiquement le contenu affiché à l'écran grâce au routeur.
2. **Le Composant (`Component`)** : C'est la brique élémentaire de l'interface graphique. Un composant regroupe :
   * Un fichier `.ts` (TypeScript) : La logique, les variables et les fonctions.
   * Un fichier `.html` : La vue (le gabarit visuel).
   * Un fichier `.css` : Les styles spécifiques au composant.
3. **Le Service (`@Injectable`)** : C'est une classe utilitaire partagée. Contrairement aux composants qui se détruisent quand on change de page, un service reste actif en mémoire. Il sert à centraliser la communication HTTP avec le backend et à stocker l'état global (comme l'utilisateur connecté).
4. **L'Injecteur de Dépendances (`Dependency Injection`)** : Vous n'avez pas besoin d'écrire `new EpisodeService()`. Vous déclarez simplement `constructor(private episodeService: EpisodeService)` et Angular vous fournit automatiquement l'instance unique du service.
5. **L'Observateur (`Observable`) et RxJS** : En JavaScript, les requêtes HTTP sont asynchrones (elles prennent du temps). Angular utilise des `Observable`. Pour récupérer le résultat d'un appel HTTP, on s'abonne avec `.subscribe({ next: (data) => ... })`.
6. **Le Routeur (`Router`)** : Associe une URL (`/episodes`) à un composant (`EpisodesComponent`). Dans le template principal, la balise `<router-outlet />` sert de cadre où s'affiche le composant de l'URL active.

---

### B. Explication de chaque Dossier et Fichier

#### 1. Fichiers Fondamentaux
* **`src/index.html`** : Le squelette HTML unique de toute l'application. Il contient `<app-root></app-root>`, qui est la balise d'accueil du composant racine.
* **`src/main.ts`** : Le point d'entrée JavaScript. Il démarre (*bootstraps*) l'application en chargeant `AppComponent` avec la configuration définie dans `app.config.ts`.
* **`src/environments/environment.ts`** : Centralise les adresses IP et ports des APIs :
  ```typescript
  export const environment = {
    apiURL: 'http://localhost:8082/serie_episode/api',
    apiURLSerie: 'http://localhost:8082/serie_episode/series',
  };
  ```
* **`src/app/app.config.ts`** : La configuration globale de l'application Angular 18 (Standalone) :
  * Déclare le routeur (`provideRouter(routes)`).
  * Configure le client HTTP avec intercepteur (`provideHttpClient(withInterceptors([tokenInterceptor]))`).
  * Active les animations et le système de notifications visuelles Toastr (`provideToastr()`).
* **`src/app/app.routes.ts`** : Le plan de circulation de l'application :
  * `/episodes` $\rightarrow$ `EpisodesComponent` (protégé par `authGuard`).
  * `/add-episode` $\rightarrow$ `AddEpisodeComponent` (protégé par `episodeGuard` réservé aux administrateurs).
  * `/updateEpisode/:id` $\rightarrow$ `UpdateEpisodeComponent` (protégé par `episodeGuard`).
  * `/rechercheParSerie` $\rightarrow$ `RechercheParSerieComponent` (protégé par `authGuard`).
  * `/rechercheParTitre` $\rightarrow$ `RechercheParTitreComponent` (protégé par `authGuard`).
  * `/listeSeries` $\rightarrow$ `ListeSeriesComponent` (protégé par `authGuard`).
  * `/login` $\rightarrow$ `LoginComponent` (public).
  * `/register` $\rightarrow$ `RegisterComponent` (public).
  * `/verifEmail` $\rightarrow$ `VerifEmailComponent` (public).
  * `/app-forbidden` $\rightarrow$ `ForbiddenComponent` (accès interdit).

#### 2. Composant Racine (`src/app/app.component.*`)
* **`app.component.ts`** :
  * À l'initialisation (`ngOnInit`), il appelle `authService.loadToken()`. Si aucun jeton n'est trouvé ou s'il est expiré, il redirige automatiquement vers `/login`.
  * Méthode `onLogout()` : Déconnecte l'utilisateur.
* **`app.component.html`** :
  * Affiche une barre de navigation sombre (`navbar-dark bg-dark`).
  * Masque ou affiche les menus selon le statut de connexion (`authService.isloggedIn`).
  * Conditionne les liens d'administration (`Ajouter`, `Gestion des séries`) à la méthode `authService.isAdmin()`.
  * Contient la balise `<router-outlet />` où viennent se brancher toutes les autres pages.

#### 3. Modèles de Données (`src/app/model/`)
* **`episode.model.ts`** : Définit la structure d'un épisode (`idEpisode`, `titreEpisode`, `numeroEpisode`, `duree`, `saison`, `serie`).
* **`serie.model.ts`** : Structure d'une série (`idSerie`, `titre`, `genre`, `anneeDebut`).
* **`serieWrapped.model.ts`** : Modèle enveloppe pour lire les réponses HATEOAS de Spring Data REST (`_embedded.series`).
* **`user.model.ts`** : Structure d'un utilisateur (`username`, `password`, `email`, `enabled`, `roles`).

#### 4. Services Métier et Sécurité (`src/app/services/`)
* **`auth.service.ts`** :
  * `login(user)` : Envoie une requête `POST` vers `http://localhost:8081/users/login`.
  * `saveToken(jwt)` : Stocke le jeton dans le `localStorage` du navigateur sous la clé `'jwt'` et appelle `decodeJWT()`.
  * `decodeJWT()` : Utilise `JwtHelperService` de `@auth0/angular-jwt` pour extraire les rôles et le nom d'utilisateur contenus dans la charge utile du jeton.
  * `isTokenExpired()` : Vérifie si la date d'expiration du jeton est dépassée.
  * `isAdmin()` : Retourne `true` si le tableau des rôles contient `'ADMIN'`.
  * `registerUser(user)` : Envoie la demande d'inscription au microservice d'authentification.
  * `validateEmail(code)` : Valide le code à 6 chiffres.
* **`token.interceptor.ts`** :
  * C'est un **intercepteur HTTP fonctionnel**.
  * Chaque fois qu'une requête HTTP sort de l'application Angular vers un backend, l'intercepteur s'exécute automatiquement.
  * Sauf pour `/login`, `/register` et `/verifyEmail`, il récupère le jeton stocké dans `AuthService` et ajoute automatiquement l'en-tête :
    ```http
    Authorization: Bearer <votre_jeton_jwt>
    ```
  * Grâce à ce fichier, aucun composant n'a besoin d'écrire manuellement l'en-tête d'autorisation !
* **`episode.service.ts`** :
  * Gère tous les appels HTTP vers le microservice métier (`http://localhost:8082/serie_episode/api` et `/series`).
  * Fournit les méthodes CRUD : `listeEpisodes()`, `ajouterEpisode()`, `supprimerEpisode()`, `consulterEpisode()`, `updateEpisode()`, `listeSeries()`, `rechercherParSerie()`, `rechercherParTitre()`, etc.

#### 5. Gardiens de Routes (`Guards`)
* **`auth.guard.ts`** : Empêche un visiteur anonyme d'accéder aux pages privées. Si `authService.isloggedIn` est faux ou si le jeton a expiré, l'utilisateur est renvoyé vers `/login`.
* **`episode.guard.ts`** : Empêche un utilisateur ordinaire (rôle `USER`) d'accéder aux pages de création ou modification d'épisodes. Si l'utilisateur n'est pas `ADMIN`, il est immédiatement redirigé vers `/app-forbidden`.

#### 6. Tubes Personnalisés (`Pipes`)
* **`search-filter.pipe.ts`** : Pipe Angular (`searchFilter`) qui filtre en direct une liste d'épisodes affichée dans un tableau selon le texte saisi dans un champ input sans faire de requête réseau.

#### 7. Composants Applicatifs
* **`login/`** : Formulaire de connexion. Affiche un message d'erreur clair si les identifiants sont erronés ou si le compte est désactivé.
* **`register/`** : Formulaire réactif avec validation stricte à la saisie, spinner de chargement et notification Toastr.
* **`verif-email/`** : Saisie du code à 6 chiffres. En cas de validation réussie, il connecte automatiquement le nouvel utilisateur et le redirige vers la page d'accueil.
* **`episodes/`** : Liste des épisodes avec boutons d'actions "Modifier" et "Supprimer" affichés uniquement si l'utilisateur connecté est administrateur.
* **`add-episode/`** : Formulaire d'ajout avec sélecteur de série.
* **`update-episode/`** : Formulaire de modification pré-rempli avec les données existantes de l'épisode.
* **`liste-series/`** & **`update-serie/`** : Architecture parent-enfant. Le composant `ListeSeriesComponent` affiche la liste des séries et héberge le sous-composant `UpdateSerieComponent` auquel il passe la série sélectionnée via `@Input()` et écoute les modifications via `@Output() EventEmitter`.
* **`recherche-par-serie/`** : Filtre les épisodes selon la série choisie dans la liste déroulante.
* **`recherche-par-titre/`** : Démonstrateur pédagogique des 3 techniques de recherche (API backend, frappe clavier client, pipe template).
* **`forbidden/`** : Page d'avertissement esthétique indiquant à l'utilisateur qu'il ne dispose pas des privilèges nécessaires pour afficher la page.

---

## 5. Plongée au Cœur de la Sécurité (Explication en Détail et Accessible)

### Pourquoi Deux Serveurs Différents ?

Dans les anciennes architectures, un seul serveur gérait à la fois la base de données des utilisateurs, les sessions PHP ou Java, et les pages web. Si ce serveur tombait en panne, tout s'arrêtait.

Dans notre architecture **Microservices / SOA** :
1. Le **Microservice Utilisateurs (8081)** est comme un **Bureau des Passeports**. Son unique mission est de vérifier votre identité et de vous délivrer un passeport inviolable (le jeton JWT).
2. Le **Microservice Séries (8082)** est comme un **Aéroport**. Il ne vous connaît pas personnellement et ne stocke aucun mot de passe. Mais quand vous présentez votre passeport (le jeton JWT), il vérifie le tampon de sécurité. S'il est authentique, il vous laisse passer.

---

### Le concept du JWT (JSON Web Token)

Un jeton JWT ressemble à une longue chaîne de caractères incompréhensible séparée par deux points (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJleHAiOjE3NzA5MjgwMDB9.s7zD_09k...
```

En réalité, il est composé de trois morceaux encodés en Base64 :
1. **L'En-tête (Header)** : Indique l'algorithme de chiffrement (ici `HMAC256`).
2. **La Charge Utile (Payload)** : Les informations sur l'utilisateur :
   * `sub` : Le nom d'utilisateur (ex: `"admin"`).
   * `roles` : Les privilèges attribués (ex: `["ADMIN", "USER"]`).
   * `exp` : La date d'expiration (en millisecondes).
3. **La Signature (Signature)** : C'est le sceau cryptographique calculé par la formule :
   $$\text{Signature} = \text{HMAC-SHA256}(\text{Header} + \text{Payload}, \text{SECRET})$$

> **Pourquoi est-il impossible de tricher ?**  
> Si un pirate modifie le payload dans son navigateur pour changer `"USER"` en `"ADMIN"`, la signature ne correspondra plus avec le `SECRET` que seul le backend connaît. Le backend rejettera instantanément la requête avec une erreur `403 Forbidden` !

---

### Cycle de vie complet d'une requête sécurisée

Voici ce qui se passe pas à pas quand vous utilisez l'application :

```
[Navigateur / Angular]               [Microservice Auth 8081]            [Microservice Métier 8082]
         |                                      |                                     |
         |---- 1. POST /login (user, mdp) ----->|                                     |
         |                                      |-- Vérifie hash BCrypt               |
         |                                      |-- Génère signature HMAC256          |
         |<--- 2. En-tête Authorization: JWT ---|                                     |
         |                                                                            |
         |-- 3. Stocke le JWT dans LocalStorage                                       |
         |-- 4. Décode les rôles pour adapter le menu                                 |
         |                                                                            |
         |---- 5. GET /api (avec en-tête Authorization: Bearer <JWT>) --------------->|
         |        (Injecté automatiquement par token.interceptor.ts)                  |-- JWTAuthorizationFilter
         |                                                                            |-- Vérifie la signature
         |                                                                            |-- Extrait [USER, ADMIN]
         |                                                                            |-- SecurityConfig valide
         |<--- 6. Réponse JSON avec la liste des épisodes ---------------------------|
```

---

### Exemples Démonstratifs de Protection

#### Scénario 1 : Un utilisateur non connecté tente d'accéder aux données
* L'utilisateur tape directement dans l'URL : `http://localhost:4200/episodes`.
* **Action du Frontend** : `auth.guard.ts` intercepte la navigation, constate que le token est absent ou expiré, bloque l'affichage et redirige l'utilisateur vers `/login`.
* Si un pirate tente d'interroger directement le backend avec `curl http://localhost:8082/serie_episode/api` :
  * `JWTAuthorizationFilter` ne trouve pas d'en-tête `Authorization`.
  * `SecurityConfig` constate que la requête n'est pas authentifiée.
  * Le serveur renvoie immédiatement une erreur **`403 Forbidden`**.

#### Scénario 2 : Un utilisateur standard (`fadi`) tente d'effectuer une action d'administrateur
* L'utilisateur `fadi` a uniquement le rôle `USER`.
* **Dans l'interface** : Les boutons "Ajouter", "Modifier", "Supprimer" sont automatiquement masqués grâce aux directives `@if (authService.isAdmin())`.
* S'il force la navigation en tapant `http://localhost:4200/add-episode` dans sa barre d'adresse :
  * `episode.guard.ts` s'exécute, appelle `authService.isAdmin()`, qui renvoie `false`.
  * La route est bloquée et l'utilisateur est téléporté sur `/app-forbidden`.
* S'il essaie de contourner Angular en envoyant directement un `POST /api` via Postman avec son jeton valide :
  * Le backend valide la signature du jeton, mais lit les rôles : `["USER"]`.
  * La règle de `SecurityConfig` stipule : `.requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("ADMIN")`.
  * Le serveur bloque l'opération et retourne **`403 Forbidden`**. Les données de la base ne sont pas altérées.

---

## 6. Guide de Test Complet (Fonctionnalités, JWT & Sécurité)

Suivez ces étapes pour tester toutes les fonctionnalités de votre projet.

### Étape 1 : Démarrer les 3 Applications

Ouvrez **3 terminaux PowerShell distincts** :

#### Terminal 1 — Le Microservice Utilisateurs
```powershell
cd "c:\Users\fadis\Desktop\spiringboot dsi3\fady_projet_soa\users-microservice"
.\mvnw.cmd spring-boot:run
```
*(Attendez l'affichage : `Started UsersMicroserviceApplication in ... seconds` sur le port **8081**)*

#### Terminal 2 — Le Microservice Métier Séries & Épisodes
```powershell
cd "c:\Users\fadis\Desktop\spiringboot dsi3\fady_projet_soa\serie_episode"
.\mvnw.cmd spring-boot:run
```
*(Attendez l'affichage : `Started SerieEpisodeApplication in ... seconds` sur le port **8082**)*

#### Terminal 3 — Le Frontend Angular
```powershell
cd "c:\Users\fadis\Desktop\spiringboot dsi3\fady_projet_soa\serie_episode_angular"
npm start
```
*(Ouvrez votre navigateur sur `http://localhost:4200`)*

---

### Étape 2 : Tester l'Authentification et Inspecter le Jeton JWT

1. Rendez-vous sur `http://localhost:4200`. Vous êtes automatiquement redirigé vers `/login`.
2. Saisissez :
   * Nom d'utilisateur : `admin`
   * Mot de passe : `123`
3. Cliquez sur **Connexion**. Vous arrivez sur la liste des épisodes et le menu affiche `[admin]`.
4. **Comment voir votre Jeton JWT dans le navigateur :**
   * Appuyez sur la touche **F12** (ou clic droit $\rightarrow$ *Inspecter*).
   * Allez dans l'onglet **Application** (ou *Stockage*).
   * Dans le menu de gauche, dépliez **Local Storage** et cliquez sur `http://localhost:4200`.
   * Vous verrez la clé **`jwt`** avec une très longue valeur. C'est votre jeton crypté !
5. **Comment décoder votre Jeton JWT :**
   * Copiez la valeur de ce jeton.
   * Rendez-vous sur le site public [jwt.io](https://jwt.io).
   * Collez le jeton dans la case **Encoded**.
   * Dans la case **Payload**, vous pouvez lire en clair :
     ```json
     {
       "sub": "admin",
       "roles": [
         "ADMIN",
         "USER"
       ],
       "exp": 177195...
     }
     ```
6. **Vérifier l'Intercepteur HTTP dans l'onglet Réseau (Network) :**
   * Toujours dans la console F12, allez dans l'onglet **Network** (Réseau).
   * Cliquez sur le lien "Home" ou "Lister".
   * Cliquez sur la requête réseau nommée `api`.
   * Regardez la section **Request Headers** (En-têtes de la requête) : vous y verrez la ligne ajoutée par votre `token.interceptor.ts` :
     ```http
     Authorization: Bearer eyJhbGciOi...
     ```

---

### Étape 3 : Tester les Gardiens de Route (Guards)

1. **Test du Guard Administrateur :**
   * Cliquez sur **Logout**.
   * Reconnectez-vous avec le compte utilisateur standard :
     * Nom d'utilisateur : `fadi`
     * Mot de passe : `123`
   * Remarquez que les boutons "Supprimer" et "Modifier" ont disparu de la table, et que l'option "Ajouter" n'apparaît plus dans le menu déroulant !
   * Maintenant, essayez de forcer le passage en tapant directement dans la barre d'adresse de votre navigateur :  
     `http://localhost:4200/add-episode`
   * **Résultat** : Angular bloque l'accès et vous affiche l'écran rouge : **"Vous n'êtes pas autorisé…"** (`/app-forbidden`).
2. **Test du Guard d'Authentification :**
   * Cliquez sur **Logout**.
   * Tentez de taper directement dans le navigateur :  
     `http://localhost:4200/episodes`
   * **Résultat** : Vous êtes immédiatement renvoyé vers `/login`.

---

### Étape 4 : Tester l'Inscription d'un Nouveau Compte et la Validation d'E-mail

1. Sur la page de connexion, cliquez sur le lien : **"Vous n'avez pas un compte ? S'inscrire"**.
2. Remplissez le formulaire :
   * Nom d'utilisateur : `nouveau_membre`
   * E-mail : `nouveau@gmail.com`
   * Mot de passe : `secret123`
   * Confirmer Mot de passe : `secret123`
3. Cliquez sur **S'inscrire**.
4. Vous êtes redirigé vers l'écran `/verifEmail` et une notification verte Toastr s'affiche : *"veuillez confirmer votre e-mail"*.
5. **Où trouver le code de vérification :**
   * Regardez dans le **Terminal 1** (`users-microservice`).
   * Vous verrez la ligne de secours imprimée :
     ```
     >>> CODE DE VALIDATION POUR nouveau_membre : 482910
     ```
6. Saisissez ce code à 6 chiffres dans le champ et cliquez sur **Valider**.
7. L'application confirme la validation, connecte automatiquement le compte, et vous redirige vers la page d'accueil !

---

### Étape 5 : Tester les Recherches Multicritères

1. Allez dans **Épisodes** $\rightarrow$ **Recherche Par Série** :
   * Choisissez par exemple *Dark* ou *Euphoria* dans la liste déroulante : seuls les épisodes de cette série s'affichent instantanément.
2. Allez dans **Épisodes** $\rightarrow$ **Recherche Par Titre** :
   * **Méthode 1** : Tapez `Pilot` dans le premier champ et cliquez sur **Rechercher** (appel backend).
   * **Méthode 2** : Tapez une lettre (ex: `a`) dans le champ de frappe directe : le tableau se filtre en temps réel au rythme de vos frappes de touches.
   * **Méthode 3** : Tapez du texte dans le troisième champ pour observer le filtrage géré par le Pipe Angular.
