import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MenuPanel extends JPanel {
    private JTable tablePlats;
    private DefaultTableModel tableModel;
    private JTextField txtNom;
    private JTextField txtPrix;
    private JComboBox<String> cmbCategorie;
    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private DatabaseManager db;

    public MenuPanel() {
        db = DatabaseManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(creerPanneauFormulaire(), BorderLayout.NORTH);
        add(creerPanneauTable(), BorderLayout.CENTER);

        chargerPlats();
    }

    private JPanel creerPanneauFormulaire() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Gestion des Plats"));

        panel.add(new JLabel("Nom:"));
        txtNom = new JTextField(15);
        panel.add(txtNom);

        panel.add(new JLabel("Catégorie:"));
        String[] categories = { "Burger", "Pizza", "Boisson", "Dessert" };
        cmbCategorie = new JComboBox<>(categories);
        panel.add(cmbCategorie);

        panel.add(new JLabel("Prix (DZD):"));
        txtPrix = new JTextField(8);
        panel.add(txtPrix);

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(144, 238, 144));
        btnAjouter.setForeground(new Color(44, 62, 80));
        btnAjouter.setFocusPainted(false);
        btnAjouter.addActionListener(e -> ajouterPlat());
        panel.add(btnAjouter);

        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(new Color(173, 216, 230));
        btnModifier.setForeground(new Color(44, 62, 80));
        btnModifier.setFocusPainted(false);
        btnModifier.addActionListener(e -> modifierPlat());
        panel.add(btnModifier);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(255, 182, 193));
        btnSupprimer.setForeground(new Color(44, 62, 80));
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.addActionListener(e -> supprimerPlat());
        panel.add(btnSupprimer);

        return panel;
    }

    private JPanel creerPanneauTable() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] colonnes = { "ID", "Nom", "Catégorie", "Prix (DZD)" };
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablePlats = new JTable(tableModel);
        tablePlats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePlats.getTableHeader().setReorderingAllowed(false);
        tablePlats.setRowHeight(25);
        tablePlats.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirFormulaire();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablePlats);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void ajouterPlat() {
        try {
            String nom = txtNom.getText().trim();
            String categorie = (String) cmbCategorie.getSelectedItem();
            String prixText = txtPrix.getText().trim();

            if (nom.isEmpty() || prixText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs!",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double prix = Double.parseDouble(prixText);
            if (prix <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Le prix doit être positif!",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Plat plat = new Plat(nom, categorie, prix);
            db.ajouterPlat(plat);

            JOptionPane.showMessageDialog(this,
                    "Plat ajouté avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            chargerPlats();
            viderFormulaire();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Prix invalide! Veuillez entrer un nombre.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierPlat() {
        int selectedRow = tablePlats.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un plat à modifier!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nom = txtNom.getText().trim();
            String categorie = (String) cmbCategorie.getSelectedItem();
            String prixText = txtPrix.getText().trim();

            if (nom.isEmpty() || prixText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir tous les champs!",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double prix = Double.parseDouble(prixText);
            if (prix <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Le prix doit être positif!",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous vraiment modifier ce plat?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Plat plat = new Plat(id, nom, categorie, prix);
                db.modifierPlat(plat);

                JOptionPane.showMessageDialog(this,
                        "Plat modifié avec succès!",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                chargerPlats();
                viderFormulaire();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Prix invalide! Veuillez entrer un nombre.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerPlat() {
        int selectedRow = tablePlats.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un plat à supprimer!",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nom = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Voulez-vous vraiment supprimer \"" + nom + "\"?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                db.supprimerPlat(id);

                JOptionPane.showMessageDialog(this,
                        "Plat supprimé avec succès!",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                chargerPlats();
                viderFormulaire();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerPlats() {
        try {
            tableModel.setRowCount(0);
            for (Plat plat : db.getTousLesPlats()) {
                Object[] row = {
                        plat.getId(),
                        plat.getNom(),
                        plat.getCategorie(),
                        String.format("%.2f", plat.getPrix())
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void remplirFormulaire() {
        int selectedRow = tablePlats.getSelectedRow();
        if (selectedRow != -1) {
            txtNom.setText((String) tableModel.getValueAt(selectedRow, 1));
            cmbCategorie.setSelectedItem((String) tableModel.getValueAt(selectedRow, 2));
            txtPrix.setText((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

    private void viderFormulaire() {
        txtNom.setText("");
        txtPrix.setText("");
        cmbCategorie.setSelectedIndex(0);
        tablePlats.clearSelection();
    }

    public void actualiser() {
        chargerPlats();
    }
}
