package java_work.de.external_rick_morty_api.Service;
import java_work.de.external_rick_morty_api.Model.Character;

import java_work.de.external_rick_morty_api.Model.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class Rick_Morty_Service {

    private final RestClient client;

    public Rick_Morty_Service(RestClient.Builder clientBuilder) {
       this.client = clientBuilder.baseUrl("https://rickandmortyapi.com/api").build();
    }

    public List<Character> getCharacters() {
        return client
                .get()
                .uri("/character")
                .retrieve().body(ApiResponse.class)
                .results();

    }

    public Character getCharacterById(int id) {
        return client
                .get()
                .uri("/character/"+id)
                .retrieve()
                .body(Character.class);
    }

    public List<Character> getCharactersByStatus(String status) {
        return getCharacters()
                .stream()
                .filter(character -> character.status().equalsIgnoreCase(status))
                .toList();
    }

    public int getLivingCharactersCountBySpecies(String species) {
        return (int) getCharacters().stream()
                .filter(character -> character.species().equalsIgnoreCase(species))
                .filter(character -> character.status().equalsIgnoreCase("alive"))
                .count(); // Count living characters of the specified species
    }
}
