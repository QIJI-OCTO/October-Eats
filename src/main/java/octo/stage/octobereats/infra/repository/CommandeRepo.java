package octo.stage.octobereats.infra.repository;

import octo.stage.octobereats.domain.Commande;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommandeRepo implements CommandeRepository{

    private List<Commande> list = new ArrayList<Commande>();

    public List<Commande> getCommandes() {
        return list;
    }

    public String addCommande(Commande commande){
        list.add(commande);
        return commande.toString();
    }

}
