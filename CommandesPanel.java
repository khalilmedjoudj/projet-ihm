import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CommandesPanel extends JPanel {
    private JTable tableCommandes;
    private DefaultTableModel commandesModel;
    private JTable tableDetails;
    private DefaultTableModel detailsModel;
    private JLabel lblStatut;
    private JLabel lblTotal;
    private JButton btnFinaliser;
    private JButton btnAnnuler;
    private JButton btnActualiser;
    private DatabaseManager db;
    private Commande commandeSelectionnee;

    public CommandesPanel() {
        db = DatabaseManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(creerPanneauHaut(), BorderLayout.NORTH);
        add(creerPanneauCentre(), BorderLayout.CENTER);
        add(creerPanneauBas(), BorderLayout.SOUTH);

        chargerCommandes();
    }

    private JPanel creerPanneauHaut() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Liste des Commandes"));
        panel.setPreferredSize(new Dimension(0, 250));

        String[] colonnes = { "N° Commande", "Date et Heure", "Total (DZD)", "Statut" };
        commandesModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCommandes = new JTable(commandesModel);
        tableCommandes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCommandes.setRowHeight(25);
        tableCommandes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherDetailsCommande();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableCommandes);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnActualiser = new JButton("Actualiser");
        btnActualiser.setBackground(new Color(173, 216, 230));
        btnActualiser.setForeground(new Color(44, 62, 80));
        btnActualiser.setFocusPainted(false);
        btnActualiser.addActionListener(e -> chargerCommandes());
        panelBouton.add(btnActualiser);
        panel.add(panelBouton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creerPanneauCentre() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Détails de la Commande"));

        String[] colonnes = { "Plat", "Prix Unitaire", "Quantité", "Sous-total" };
        detailsModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableDetails = new JTable(detailsModel);
        tableDetails.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tableDetails);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel panelStatut = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStatut.add(new JLabel("Statut:"));
        lblStatut = new JLabel("-");
        lblStatut.setFont(new Font("Arial", Font.BOLD, 14));
        panelStatut.add(lblStatut);
        panelInfo.add(panelStatut);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(new JLabel("TOTAL:"));
        lblTotal = new JLabel("0.00 DZD");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(44, 62, 80));
        panelTotal.add(lblTotal);
        panelInfo.add(panelTotal);

        panel.add(panelInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creerPanneauBas() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnAnnuler = new JButton("Annuler la Commande");
        btnAnnuler.setBackground(new Color(255, 182, 193));
        btnAnnuler.setForeground(new Color(44, 62, 80));
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setFont(new Font("Arial", Font.BOLD, 14));
        btnAnnuler.addActionListener(e -> annulerCommande());
        panel.add(btnAnnuler);

        btnFinaliser = new JButton("Finaliser la Commande");
        btnFinaliser.setBackground(new Color(144, 238, 144));
        btnFinaliser.setForeground(new Color(44, 62, 80));
        btnFinaliser.setFocusPainted(false);
        btnFinaliser.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinaliser.addActionListener(e -> finaliserCommande());
        panel.add(btnFinaliser);

        return panel;
    }

    private void afficherDetailsCommande() {
        int selectedRow = tableCommandes.getSelectedRow();
        if (selectedRow == -1) {
            detailsModel.setRowCount(0);
            lblStatut.setText("-");
            lblTotal.setText("0.00 DZD");
            commandeSelectionnee = null;
            return;
        }

        try {
            int commandeId = (int) commandesModel.getValueAt(selectedRow, 0);

            for (Commande cmd : db.getToutesLesCommandes()) {
                if (cmd.getId() == commandeId) {
                    commandeSelectionnee = cmd;
                    break;
                }
            }

            if (commandeSelectionnee != null) {
                detailsModel.setRowCount(0);
                for (LigneCommande ligne : commandeSelectionnee.getLignes()) {
                    Object[] row = {
                            ligne.getPlat().getNom(),
                            String.format("%.2f", ligne.getPlat().getPrix()),
                            ligne.getQuantite(),
                            String.format("%.2f", ligne.getSousTotal())
                    };
                    detailsModel.addRow(row);
                }

                lblStatut.setText(commandeSelectionnee.getStatut());
                lblTotal.setText(String.format("%.2f DZD", commandeSelectionnee.getTotal()));

                switch (commandeSelectionnee.getStatut()) {
                    case "En cours":
                        lblStatut.setForeground(new Color(243, 156, 18));
                        break;
                    case "Finalisée":
                        lblStatut.setForeground(new Color(46, 204, 113));
                        break;
                    case "Annulée":
                        lblStatut.setForeground(new Color(231, 76, 60));
                        break;
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'affichage: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finaliserCommande() {
        if (commandeSelectionnee == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une commande!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!commandeSelectionnee.getStatut().equals("En cours")) {
            JOptionPane.showMessageDialog(this,
                    "Cette commande ne peut pas être finalisée!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous finaliser la commande N°" + commandeSelectionnee.getId() + "?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                db.modifierStatutCommande(commandeSelectionnee.getId(), "Finalisée");

                JOptionPane.showMessageDialog(this,
                        "Commande finalisée avec succès!",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                chargerCommandes();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la finalisation: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void annulerCommande() {
        if (commandeSelectionnee == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une commande!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!commandeSelectionnee.getStatut().equals("En cours")) {
            JOptionPane.showMessageDialog(this,
                    "Cette commande ne peut pas être annulée!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous vraiment annuler la commande N°" + commandeSelectionnee.getId() + "?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                db.modifierStatutCommande(commandeSelectionnee.getId(), "Annulée");

                JOptionPane.showMessageDialog(this,
                        "Commande annulée!",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);

                chargerCommandes();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'annulation: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerCommandes() {
        try {
            commandesModel.setRowCount(0);
            for (Commande commande : db.getToutesLesCommandes()) {
                Object[] row = {
                        commande.getId(),
                        commande.getDateFormatee(),
                        String.format("%.2f", commande.getTotal()),
                        commande.getStatut()
                };
                commandesModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualiser() {
        chargerCommandes();
    }
}
