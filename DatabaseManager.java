import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            String basePath = new File(".").getAbsolutePath();
            String dbPath = "fastfood.db";

            String url = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(url);
            creerTables();
            insererDonneesInitiales();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void creerTables() throws SQLException {
        String sqlPlats = "CREATE TABLE IF NOT EXISTS plats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "categorie TEXT NOT NULL," +
                "prix REAL NOT NULL)";

        String sqlCommandes = "CREATE TABLE IF NOT EXISTS commandes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_heure TEXT NOT NULL," +
                "statut TEXT NOT NULL)";

        String sqlLignes = "CREATE TABLE IF NOT EXISTS lignes_commande (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "commande_id INTEGER NOT NULL," +
                "plat_id INTEGER NOT NULL," +
                "quantite INTEGER NOT NULL," +
                "FOREIGN KEY (commande_id) REFERENCES commandes(id)," +
                "FOREIGN KEY (plat_id) REFERENCES plats(id))";

        Statement stmt = connection.createStatement();
        stmt.execute(sqlPlats);
        stmt.execute(sqlCommandes);
        stmt.execute(sqlLignes);
        stmt.close();
    }

    private void insererDonneesInitiales() throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM plats";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(checkSql);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();

        if (count == 0) {
            ajouterPlat(new Plat("Big Burger", "Burger", 450.0));
            ajouterPlat(new Plat("Cheese Burger", "Burger", 400.0));
            ajouterPlat(new Plat("Pizza Margherita", "Pizza", 600.0));
            ajouterPlat(new Plat("Pizza 4 Fromages", "Pizza", 700.0));
            ajouterPlat(new Plat("Coca Cola", "Boisson", 100.0));
            ajouterPlat(new Plat("Fanta", "Boisson", 100.0));
            ajouterPlat(new Plat("Tiramisu", "Dessert", 250.0));
            ajouterPlat(new Plat("Brownie", "Dessert", 200.0));
        }
    }

    public void ajouterPlat(Plat plat) throws SQLException {
        String sql = "INSERT INTO plats (nom, categorie, prix) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, plat.getNom());
        pstmt.setString(2, plat.getCategorie());
        pstmt.setDouble(3, plat.getPrix());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void modifierPlat(Plat plat) throws SQLException {
        String sql = "UPDATE plats SET nom = ?, categorie = ?, prix = ? WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, plat.getNom());
        pstmt.setString(2, plat.getCategorie());
        pstmt.setDouble(3, plat.getPrix());
        pstmt.setInt(4, plat.getId());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void supprimerPlat(int id) throws SQLException {
        String sql = "DELETE FROM plats WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public List<Plat> getTousLesPlats() throws SQLException {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM plats ORDER BY categorie, nom";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Plat plat = new Plat(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("categorie"),
                    rs.getDouble("prix"));
            plats.add(plat);
        }

        rs.close();
        stmt.close();
        return plats;
    }

    public int ajouterCommande(Commande commande) throws SQLException {
        String sql = "INSERT INTO commandes (date_heure, statut) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, commande.getDateHeure().toString());
        pstmt.setString(2, commande.getStatut());
        pstmt.executeUpdate();
        pstmt.close();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
        int commandeId = 0;
        if (rs.next()) {
            commandeId = rs.getInt(1);
        }
        rs.close();
        stmt.close();

        for (LigneCommande ligne : commande.getLignes()) {
            ajouterLigneCommande(commandeId, ligne);
        }

        return commandeId;
    }

    private void ajouterLigneCommande(int commandeId, LigneCommande ligne) throws SQLException {
        String sql = "INSERT INTO lignes_commande (commande_id, plat_id, quantite) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, commandeId);
        pstmt.setInt(2, ligne.getPlat().getId());
        pstmt.setInt(3, ligne.getQuantite());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public void modifierStatutCommande(int commandeId, String statut) throws SQLException {
        String sql = "UPDATE commandes SET statut = ? WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, statut);
        pstmt.setInt(2, commandeId);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public List<Commande> getToutesLesCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commandes ORDER BY id DESC";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Commande commande = new Commande(
                    rs.getInt("id"),
                    LocalDateTime.parse(rs.getString("date_heure")),
                    rs.getString("statut"));
            chargerLignesCommande(commande);
            commandes.add(commande);
        }

        rs.close();
        stmt.close();
        return commandes;
    }

    private void chargerLignesCommande(Commande commande) throws SQLException {
        String sql = "SELECT lc.*, p.nom, p.categorie, p.prix " +
                "FROM lignes_commande lc " +
                "JOIN plats p ON lc.plat_id = p.id " +
                "WHERE lc.commande_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, commande.getId());
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Plat plat = new Plat(
                    rs.getInt("plat_id"),
                    rs.getString("nom"),
                    rs.getString("categorie"),
                    rs.getDouble("prix"));
            LigneCommande ligne = new LigneCommande(
                    rs.getInt("id"),
                    rs.getInt("commande_id"),
                    plat,
                    rs.getInt("quantite"));
            commande.ajouterLigne(ligne);
        }

        rs.close();
        pstmt.close();
    }

    public void fermerConnexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
