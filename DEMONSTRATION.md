# 🍔 Démonstration du Projet IHM - Fast Food Manager

## Vue d'ensemble
Ce document présente les fonctionnalités principales de l'application de gestion de fast-food.

---

## 🚀 Lancement de l'Application

### Étape 1: Compilation et Exécution
```bash
# Double-cliquez sur le fichier ou exécutez:
compile_and_run.bat
```

L'application se lance avec une interface graphique moderne.

---

## 📱 Interface Principale

### Fenêtre Principale
L'application s'ouvre avec **trois onglets principaux** :

1. **📋 Menu** - Gestion des plats
2. **🛒 Commandes** - Gestion des commandes
3. **💰 Caisse** - Traitement des paiements

---

## 1️⃣ Onglet MENU - Gestion des Plats

### Fonctionnalités:
- **Affichage des plats** organisés par catégorie
- **Ajouter un nouveau plat**
- **Modifier un plat existant**
- **Supprimer un plat**

### Catégories disponibles:
- 🍔 Burgers
- 🍕 Pizzas
- 🥤 Boissons
- 🍰 Desserts

### Données initiales (exemples):
| Nom | Catégorie | Prix (DA) |
|-----|-----------|-----------|
| Big Burger | Burger | 450.0 |
| Cheese Burger | Burger | 400.0 |
| Pizza Margherita | Pizza | 600.0 |
| Pizza 4 Fromages | Pizza | 700.0 |
| Coca Cola | Boisson | 100.0 |
| Fanta | Boisson | 100.0 |
| Tiramisu | Dessert | 250.0 |
| Brownie | Dessert | 200.0 |

### Actions possibles:
1. **Ajouter un plat:**
   - Cliquer sur "Ajouter"
   - Remplir le formulaire (Nom, Catégorie, Prix)
   - Valider

2. **Modifier un plat:**
   - Sélectionner un plat dans la liste
   - Cliquer sur "Modifier"
   - Modifier les informations
   - Valider

3. **Supprimer un plat:**
   - Sélectionner un plat
   - Cliquer sur "Supprimer"
   - Confirmer la suppression

---

## 2️⃣ Onglet COMMANDES - Gestion des Commandes

### Fonctionnalités:
- **Créer une nouvelle commande**
- **Ajouter des plats à la commande**
- **Modifier les quantités**
- **Voir le total de la commande**
- **Visualiser l'historique des commandes**

### Processus de création d'une commande:

1. **Sélectionner des plats:**
   - Parcourir la liste des plats disponibles
   - Cliquer sur un plat pour l'ajouter

2. **Ajuster les quantités:**
   - Utiliser les boutons +/- pour modifier les quantités
   - Voir le sous-total se mettre à jour automatiquement

3. **Finaliser la commande:**
   - Vérifier le récapitulatif
   - Cliquer sur "Valider la commande"
   - La commande est enregistrée avec un statut "En attente"

### Statuts des commandes:
- 🟡 **En attente** - Commande créée, en attente de préparation
- 🟢 **Prête** - Commande préparée, prête à être servie
- ✅ **Payée** - Commande payée et terminée

---

## 3️⃣ Onglet CAISSE - Traitement des Paiements

### Fonctionnalités:
- **Afficher les commandes en attente**
- **Traiter les paiements**
- **Calculer la monnaie à rendre**
- **Marquer les commandes comme payées**

### Processus de paiement:

1. **Sélectionner une commande:**
   - Voir la liste des commandes "En attente" ou "Prêtes"
   - Cliquer sur une commande pour la sélectionner

2. **Afficher le détail:**
   - Voir tous les articles de la commande
   - Voir le montant total à payer

3. **Traiter le paiement:**
   - Entrer le montant reçu du client
   - Le système calcule automatiquement la monnaie à rendre
   - Valider le paiement
   - La commande passe au statut "Payée"

---

## 💾 Base de Données

### Technologie:
- **SQLite** - Base de données locale légère
- **Fichier:** `fastfood.db`

### Tables:
1. **plats** - Stocke les informations des plats
   - id, nom, catégorie, prix

2. **commandes** - Stocke les commandes
   - id, date_heure, statut

3. **lignes_commande** - Détails des commandes
   - id, commande_id, plat_id, quantite

### Persistance des données:
- ✅ Toutes les données sont sauvegardées automatiquement
- ✅ Les données persistent entre les sessions
- ✅ Pas besoin de configuration supplémentaire

---

## 🎯 Scénario de Démonstration Complet

### Scénario: Commande d'un client

1. **Le client arrive et commande:**
   - 1x Big Burger (450 DA)
   - 1x Coca Cola (100 DA)
   - 1x Brownie (200 DA)

2. **Créer la commande (Onglet Commandes):**
   - Ajouter Big Burger → Quantité: 1
   - Ajouter Coca Cola → Quantité: 1
   - Ajouter Brownie → Quantité: 1
   - **Total: 750 DA**
   - Valider la commande

3. **Traiter le paiement (Onglet Caisse):**
   - Sélectionner la commande #1
   - Montant total: 750 DA
   - Client donne: 1000 DA
   - **Monnaie à rendre: 250 DA**
   - Valider le paiement
   - Commande marquée comme "Payée"

---

## 🛠️ Technologies Utilisées

- **Java Swing** - Interface graphique
- **SQLite JDBC** - Connexion base de données
- **SLF4J** - Logging
- **Java 8+** - Langage de programmation

---

## 📊 Points Forts du Projet

✅ **Interface intuitive** avec navigation par onglets  
✅ **Gestion complète** du cycle de vie d'une commande  
✅ **Persistance des données** avec SQLite  
✅ **Calcul automatique** des totaux et de la monnaie  
✅ **Organisation par catégories** pour faciliter la navigation  
✅ **Code bien structuré** avec séparation des responsabilités  

---

## 📝 Notes pour la Présentation

### Points à mentionner:
1. **Architecture MVC** (Model-View-Controller)
   - Modèles: Plat, Commande, LigneCommande
   - Vues: MenuPanel, CommandesPanel, CaissePanel
   - Contrôleur: DatabaseManager

2. **Gestion des événements** avec ActionListener

3. **Utilisation de JDBC** pour la persistance

4. **Interface responsive** qui s'adapte

---

## 🎥 Comment Enregistrer une Vidéo de Démonstration

Si vous souhaitez créer une vidéo de démonstration:

1. **Utiliser OBS Studio** (gratuit):
   - Télécharger: https://obsproject.com/
   - Configurer la capture d'écran
   - Enregistrer votre démonstration

2. **Ou utiliser l'Enregistreur Windows:**
   - Appuyer sur `Win + G`
   - Cliquer sur "Enregistrer"
   - Faire la démonstration
   - Arrêter l'enregistrement

3. **Script de démonstration suggéré (2-3 minutes):**
   - 0:00-0:30 - Introduction et lancement
   - 0:30-1:00 - Onglet Menu (ajouter/modifier un plat)
   - 1:00-2:00 - Onglet Commandes (créer une commande complète)
   - 2:00-2:30 - Onglet Caisse (traiter le paiement)
   - 2:30-3:00 - Conclusion et fermeture

---

## 📧 Contact

**Projet réalisé pour le cours IHM (Interface Homme-Machine)**

Repository GitHub: https://github.com/khalilmedjoudj/projet-ihm

---

*Dernière mise à jour: 31 Janvier 2026*
