import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private MenuPanel menuPanel;
    private CaissePanel caissePanel;
    private CommandesPanel commandesPanel;

    public MainFrame() {
        setTitle("Gestion Fast-Food");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initialiserComposants();
        configurerInterface();
    }

    private void initialiserComposants() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        menuPanel = new MenuPanel();
        caissePanel = new CaissePanel();
        commandesPanel = new CommandesPanel();

        tabbedPane.addTab("Gestion Menu", menuPanel);
        tabbedPane.addTab("Caisse", caissePanel);
        tabbedPane.addTab("Commandes", commandesPanel);

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            switch (index) {
                case 0:
                    menuPanel.actualiser();
                    break;
                case 1:
                    caissePanel.actualiser();
                    break;
                case 2:
                    commandesPanel.actualiser();
                    break;
            }
        });
    }

    private void configurerInterface() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel lblTitre = new JLabel("SYSTEME DE GESTION FAST-FOOD");
        lblTitre.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE);
        headerPanel.add(lblTitre);

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void afficher() {
        setVisible(true);
    }
}
