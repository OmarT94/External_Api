package java_work.de.external_rick_morty_api.Controller;

import java_work.de.external_rick_morty_api.Model.Character;
import java_work.de.external_rick_morty_api.Service.Rick_Morty_Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
public class RickAndMortyController {

    private final Rick_Morty_Service service;

    public RickAndMortyController(Rick_Morty_Service service) {
        this.service = service;
    }

    @GetMapping
    public List<Character> getAllCharacters() {
        return service.getCharacters();
    }

    @GetMapping("/{id}")
    public Character getCharacterById(@PathVariable int id) {
        return service.getCharacterById(id);
    }

    @GetMapping(params = "status")
    public List<Character> getCharactersByStatus(@RequestParam String status) {
        return service.getCharactersByStatus(status);
    }
    @GetMapping("/species-statistic")
    public int getSpeciesStatistics(@RequestParam String species) {
        return service.getLivingCharactersCountBySpecies(species);
    }

}
