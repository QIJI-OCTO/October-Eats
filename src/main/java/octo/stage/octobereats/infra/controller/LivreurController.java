package octo.stage.octobereats.infra.controller;

import octo.stage.octobereats.domain.Commande;
import octo.stage.octobereats.domain.CommandeStatus;
import octo.stage.octobereats.domain.Livreur;
import octo.stage.octobereats.infra.flux.CommandeFlux;
import octo.stage.octobereats.infra.repository.LivreurRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivreurController {

    @Autowired
    LivreurRepository livreurRepository;

    @Autowired
    CommandeFlux commandeFlux;

    public LivreurController(LivreurRepository livreurRepository) {
         this.livreurRepository = livreurRepository;
    }

    // get la liste des livreurs
    @GetMapping("/livreurs")
    public List<Livreur> livreurs(){
        return livreurRepository.getLivreurs();
    }// todo: usecase : RecupererLesLivreurs

    // post un nouveau livreur dans la liste
    @PostMapping("/livreurs")
    public Livreur newLivreur(@RequestBody Livreur livreur){
        return livreurRepository.addLivreur(livreur);
    } // todo: usecase : CreerLivreur

    // get la liste des commandes qui ne sont pas encore choisies par les livreurs en réactive
    @GetMapping(path="/livreurs/commandesPasEncoreChoisies", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<Commande> getCommandesPasEncoreChoisies(){
        return commandeFlux.getCommandesPublisher().filter(commande ->
                commande.getCommandeStatus() != CommandeStatus.EN_LIVRAISON &&
                        commande.getCommandeStatus() != CommandeStatus.LIVREE &&
                                commande.getIdLivreur() == 0);
    }// todo: usecase : SuivreCommandesDisponibles (et renommer pasEncoreChoisies en Disponibles)

    // get les commandes choisies pour le livreur identifiant=id
    @GetMapping(path="/livreurs/{id}/commandes",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Publisher<Commande> getCommandeLivreur(@PathVariable long id){
        return commandeFlux.getCommandesPublisher().filter((commande)-> commande.getIdLivreur() == id);
    }// todo: usecase : SuivreCommandesLivreur

}
