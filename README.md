# Testez-une-application-full-stack_Kilian-Leybros

# Cloner le projet

Git clone : https://github.com/KilianLeybros/Testez-une-application-full-stack_Kilian-Leybros.git

## Back

Le projet est réalisé sous java 11 et utilise maven, le jdk 11 et maven est nécessaire pour lancer le projet.

Se rendre à la racine du projet backend (/back) et installer les dépendances grâce à maven :

> `mvn clean install`

### Test

Pour lancer les tests :

> `mvn clean verify`

Une fois les tests terminés, les rapports de couverture Jacoco seront disponible ici :

> `back/target/site/jacoco-integration-test-coverage-report/index.html` => integration
> `back/target/site/jacoco-merged-test-coverage-report/index.html` => merged
> `back/target/site/jacoco-unit-test-coverage-report/index.html` => unitaire

### Démarrer le projet back

Si vous avez besoin de démarrer le back, il vous faudra installer MySQL server version 8 ainsi qu'un client MySQL (MySQL Workbench, phpMyAdmin),
lien de téléchargement : https://dev.mysql.com/downloads/installer/

Une fois MySQL installé :

- Assurez-vous que MySQL est bien lancé sur le port 3306.

- Créez le le schéma grace au script se trouvant dans le dossier à la racine du projet

> `ressources/sql/script.sql`

- Créez un utilisateur username : user, password 123456 et lui attribuer les priviléges suivants: DELETE,EXECUTE,INSERT,SELECT,SHOW VIEW, UPDATE

Par défaut le compte admin est:

- login: yoga@studio.com
- password: test!1234

Vous pouvez désormais lancer le projet grâce à la commande :

> `mvn spring-boot:run`

## Front

Ce project a été générer avec [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

Se rendre à la racine du projet frontend (/front) et installer les dépendances grâce à npm :

> `npm install`

### Démarrer le projet front

> `npm run start`

### Test unitaire

> `npm run test:coverage`

Report disponible ici :

> `front/coverage/jest/icov-report/index.html`

### Test e2e

> `npx nyc instrument --compact=false src instrumented`

Ensuite pour lancer les tests et générer le rapport

> `ng e2e`
