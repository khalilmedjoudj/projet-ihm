import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private LocalDateTime dateHeure;
    private String statut;
    private List<LigneCommande> lignes;

    public Commande(int id, LocalDateTime dateHeure, String statut) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.statut = statut;
        this.lignes = new ArrayList<>();
    }

    public Commande() {
        this.dateHeure = LocalDateTime.now();
        this.statut = "En cours";
        this.lignes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public void ajouterLigne(LigneCommande ligne) {
        lignes.add(ligne);
    }

    public double getTotal() {
        return lignes.stream()
                .mapToDouble(LigneCommande::getSousTotal)
                .sum();
    }

    public String getDateFormatee() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateHeure.format(formatter);
    }
}
