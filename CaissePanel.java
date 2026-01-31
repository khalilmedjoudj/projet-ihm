import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CaissePanel extends JPanel {
    private JList<Plat> listePlats;
    private DefaultListModel<Plat> listeModel;
    private JTable tablePanier;
    private DefaultTableModel panierModel;
    private JLabel lblTotal;
    private JSpinner spinnerQuantite;
    private JButton btnAjouterPanier;
    private JButton btnRetirerPanier;
    private JButton btnValider;
    private JButton btnAnnuler;
    private DatabaseManager db;
    private List<LigneCommande> panier;

    public CaissePanel() {
        db = DatabaseManager.getInstance();
        panier = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(creerPanneauGauche(), BorderLayout.WEST);
        add(creerPanneauCentre(), BorderLayout.CENTER);
        add(creerPanneauBas(), BorderLayout.SOUTH);

        chargerPlats();
    }

    private JPanel creerPanneauGauche() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Plats Disponibles"));
        panel.setPreferredSize(new Dimension(300, 0));

        listeModel = new DefaultListModel<>();
        listePlats = new JList<>(listeModel);
        listePlats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listePlats);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelControls = new JPanel(new FlowLayout());
        panelControls.add(new JLabel("Quantité:"));
        spinnerQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        panelControls.add(spinnerQuantite);

        btnAjouterPanier = new JButton("Ajouter au Panier");
        btnAjouterPanier.setBackground(new Color(144, 238, 144));
        btnAjouterPanier.setForeground(new Color(44, 62, 80));
        btnAjouterPanier.setFocusPainted(false);
        btnAjouterPanier.addActionListener(e -> ajouterAuPanier());
        panelControls.add(btnAjouterPanier);

        panel.add(panelControls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creerPanneauCentre() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Panier"));

        String[] colonnes = { "Plat", "Prix Unit.", "Quantité", "Sous-total" };
        panierModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePanier = new JTable(panierModel);
        tablePanier.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePanier.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tablePanier);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRetirerPanier = new JButton("Retirer");
        btnRetirerPanier.setBackground(new Color(255, 182, 193));
        btnRetirerPanier.setForeground(new Color(44, 62, 80));
        btnRetirerPanier.setFocusPainted(false);
        btnRetirerPanier.addActionListener(e -> retirerDuPanier());
        panelControls.add(btnRetirerPanier);
        panel.add(panelControls, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creerPanneauBas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(new JLabel("TOTAL:"));
        lblTotal = new JLabel("0.00 DZD");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblTotal.setForeground(new Color(44, 62, 80));
        panelTotal.add(lblTotal);
        panel.add(panelTotal, BorderLayout.NORTH);

        JPanel panelBoutons = new JPanel(new GridLayout(1, 2, 10, 0));

        btnAnnuler = new JButton("Annuler la Commande");
        btnAnnuler.setBackground(new Color(255, 182, 193));
        btnAnnuler.setForeground(new Color(44, 62, 80));
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 14));
        btnAnnuler.addActionListener(e -> annulerCommande());
        panelBoutons.add(btnAnnuler);

        btnValider = new JButton("Valider la Commande");
        btnValider.setBackground(new Color(144, 238, 144));
        btnValider.setForeground(new Color(44, 62, 80));
        btnValider.setFocusPainted(false);
        btnValider.setFont(new Font("Arial", Font.BOLD, 14));
        btnValider.addActionListener(e -> validerCommande());
        panelBoutons.add(btnValider);

        panel.add(panelBoutons, BorderLayout.CENTER);

        return panel;
    }

    private void ajouterAuPanier() {
        Plat platSelectionne = listePlats.getSelectedValue();
        if (platSelectionne == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un plat!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantite = (int) spinnerQuantite.getValue();

            boolean trouve = false;
            for (LigneCommande ligne : panier) {
                if (ligne.getPlat().getId() == platSelectionne.getId()) {
                    ligne.setQuantite(ligne.getQuantite() + quantite);
                    trouve = true;
                    break;
                }
            }

            if (!trouve) {
                panier.add(new LigneCommande(platSelectionne, quantite));
            }

            mettreAJourPanier();
            spinnerQuantite.setValue(1);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retirerDuPanier() {
        int selectedRow = tablePanier.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un article à retirer!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            panier.remove(selectedRow);
            mettreAJourPanier();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du retrait: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validerCommande() {
        if (panier.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le panier est vide!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous valider cette commande?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Commande commande = new Commande();
                for (LigneCommande ligne : panier) {
                    commande.ajouterLigne(ligne);
                }

                int commandeId = db.ajouterCommande(commande);

                JOptionPane.showMessageDialog(this,
                        "Commande N°" + commandeId + " validée avec succès!\nTotal: " +
                                String.format("%.2f", commande.getTotal()) + " DZD",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                viderPanier();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la validation: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annulerCommande() {
        if (panier.isEmpty()) {
            return;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous annuler cette commande?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                viderPanier();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourPanier() {
        panierModel.setRowCount(0);
        double total = 0;

        for (LigneCommande ligne : panier) {
            Object[] row = {
                    ligne.getPlat().getNom(),
                    String.format("%.2f", ligne.getPlat().getPrix()),
                    ligne.getQuantite(),
                    String.format("%.2f", ligne.getSousTotal())
            };
            panierModel.addRow(row);
            total += ligne.getSousTotal();
        }

        lblTotal.setText(String.format("%.2f DZD", total));
    }

    private void viderPanier() {
        panier.clear();
        mettreAJourPanier();
    }

    private void chargerPlats() {
        try {
            listeModel.clear();
            for (Plat plat : db.getTousLesPlats()) {
                listeModel.addElement(plat);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualiser() {
        chargerPlats();
    }
}
