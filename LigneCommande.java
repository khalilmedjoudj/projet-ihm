public class LigneCommande {
    private int id;
    private int commandeId;
    private Plat plat;
    private int quantite;

    public LigneCommande(int id, int commandeId, Plat plat, int quantite) {
        this.id = id;
        this.commandeId = commandeId;
        this.plat = plat;
        this.quantite = quantite;
    }

    public LigneCommande(Plat plat, int quantite) {
        this.plat = plat;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public Plat getPlat() {
        return plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getSousTotal() {
        return plat.getPrix() * quantite;
    }
}
