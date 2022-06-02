package octo.stage.octobereats.usecases;

import octo.stage.octobereats.domain.CommandeStatus;
import octo.stage.octobereats.domain.Livreur;
import octo.stage.octobereats.infra.flux.CommandeFlux;
import octo.stage.octobereats.infra.repository.CommandeRepository;
import octo.stage.octobereats.infra.repository.LivreurRepository;
import org.springframework.stereotype.Component;

// un usecase doit prendre du repository pour ensuite
// les donner au domain (où sont stockées les règles métier)
// puis retourne le résultat
// on peut conclure que le usecase est un "passe-plat"
// le usecase ne doit pas faire de if ni de for. Il ne doit contenir aucune règle
// un usecase par controller et un controller par usecase.
// un usecase ne comporte qu'une seule méthode publique, qui s'appelle exécuter
@Component
public class livreurChoisitCommande {
    CommandeRepository commandeRepository;
    LivreurRepository livreurRepository;
    CommandeFlux commandeFlux;

    public livreurChoisitCommande(CommandeRepository commandeRepository, LivreurRepository livreurRepository, CommandeFlux commandeFlux) {
        this.commandeRepository = commandeRepository;
        this.livreurRepository = livreurRepository;
        this.commandeFlux = commandeFlux;
    }

    public Livreur exécuter(long idLivreur, long idCommande) {
        var commande = commandeRepository.findById(idCommande);
        var livreur = livreurRepository.findById(idLivreur);
        var commandesDuLivreur = livreur.getCommandeList();

        var livreurEncoreEnLivraison = commandesDuLivreur
                .stream()
                .anyMatch(commandeDuLivreur -> commandeDuLivreur.getCommandeStatus() != CommandeStatus.LIVREE);

        var commandePasPrête = commande.getCommandeStatus() != CommandeStatus.PRETE;

        if (livreurEncoreEnLivraison || commandePasPrête) {
            return null;
        }

        commande.setIdLivreur(idLivreur);
        livreurRepository.addCommandeDansList(commande, livreur);
        commandeFlux.getCommandesStream().next(commande);

        return livreur;
    }
}
