package octo.stage.octobereats.infra;

import octo.stage.octobereats.domain.Commande;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandeController {

    @PostMapping("/commandes")
    public Commande newCommande(@RequestBody Commande newCommande) {

        return newCommande;
    }

}
