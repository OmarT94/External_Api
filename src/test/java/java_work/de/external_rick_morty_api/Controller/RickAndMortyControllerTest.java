package java_work.de.external_rick_morty_api.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class RickAndMortyControllerTest {
@Autowired
private MockMvc mockMvc;

@Autowired
private MockRestServiceServer mockServer;



    @Test
    void getAllCharacters() throws Exception {
        // Simuliere die Antwort der Rick&Morty API
        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                {
                  "info": {
                    "count": 1,
                    "pages": 1,
                    "next": null,
                    "prev": null
                  },
                  "results": [
                    {
                      "id": 1,
                      "name": "Rick Sanchez",
                      "status": "Alive",
                      "species": "Human",
                      "type": "",
                      "gender": "Male",
                      "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    }
                  ]
                }
            """, MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/api/character"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                  {
                    "id": 1,
                    "name": "Rick Sanchez",
                    "status": "Alive",
                    "species": "Human"
                  }
                ]
            """));
    }


    @Test
    void getCharacterById() throws Exception {
        // Simulierte Antwort der Rick&Morty API für die ID 1
        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
                        "id": 1,
                        "name": "Rick Sanchez",
                        "status": "Alive",
                        "species": "Human",
                        "type": "",
                        "gender": "Male",
                        "image": "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    }
                """, MediaType.APPLICATION_JSON)); // Simulierte JSON-Antwort

        // Teste den Controller-Endpunkt
        mockMvc.perform(get("/api/character/1")) // Anfrage an den Endpunkt mit ID 1
                .andExpect(status().isOk()) // Prüfe, ob der Status 200 OK ist
                .andExpect(content().json("""
                    {
                        "id": 1,
                        "name": "Rick Sanchez",
                        "status": "Alive",
                        "species": "Human"
                         
                    }
                """));
    }

    @Test
    void getCharactersByStatus() throws Exception {
        // Simulierte API-Antwort der Rick&Morty API
        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
                      "info": {
                        "count": 3,
                        "pages": 1,
                        "next": null,
                        "prev": null
                      },
                      "results": [
                        {
                          "id": 1,
                          "name": "Rick Sanchez",
                          "status": "Alive",
                          "species": "Human"
                        },
                        {
                          "id": 2,
                          "name": "Morty Smith",
                          "status": "Alive",
                          "species": "Human"
                        },
                        {
                          "id": 3,
                          "name": "Birdperson",
                          "status": "Dead",
                          "species": "Birdperson"
                        }
                      ]
                    }
                """, MediaType.APPLICATION_JSON)); // Simulierte JSON-Antwort

        // Teste den Endpunkt mit dem Filter "alive"
        mockMvc.perform(get("/api/character?status=alive"))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(content().json("""
                    [
                        {
                            "id": 1,
                            "name": "Rick Sanchez",
                            "status": "Alive",
                            "species": "Human"
                        },
                        {
                            "id": 2,
                            "name": "Morty Smith",
                            "status": "Alive",
                            "species": "Human"
                        }
                    ]
                """)); // Erwartete JSON-Antwort (nur "alive" Charaktere)
       }

    @Test
    void getSpeciesStatistics() throws Exception {
        // Simulate API Response
        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
                      "info": {
                        "count": 3,
                        "pages": 1,
                        "next": null,
                        "prev": null
                      },
                      "results": [
                        {
                          "id": 1,
                          "name": "Rick Sanchez",
                          "status": "Alive",
                          "species": "Human"
                        },
                        {
                          "id": 2,
                          "name": "Morty Smith",
                          "status": "Alive",
                          "species": "Human"
                        },
                        {
                          "id": 3,
                          "name": "Birdperson",
                          "status": "Dead",
                          "species": "Birdperson"
                        }
                      ]
                    }
                """, MediaType.APPLICATION_JSON)); // Mocked JSON response

        // Perform GET request to /api/species-statistic?species=Human
        mockMvc.perform(get("/api/character/species-statistic?species=Human"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().string("2"));
    }


    @Test
    void getSpeciesStatisticsForNonExistentSpecies() throws Exception {
        // Simulate API Response
        mockServer.expect(requestTo("https://rickandmortyapi.com/api/character"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                    {
                      "info": {
                        "count": 3,
                        "pages": 1,
                        "next": null,
                        "prev": null
                      },
                      "results": [
                        {
                          "id": 1,
                          "name": "Rick Sanchez",
                          "status": "Alive",
                          "species": "Alien"
                        },
                        {
                          "id": 2,
                          "name": "Morty Smith",
                          "status": "Alive",
                          "species": "Alien"
                        },
                        {
                          "id": 3,
                          "name": "Birdperson",
                          "status": "dead",
                          "species": "Alien"
                        }
                      ]
                    }
                """, MediaType.APPLICATION_JSON)); // Mocked JSON response
        // Perform GET request to /api/species-statistic?species=Alien
        mockMvc.perform(get("/api/character/species-statistic?species=Alien"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().string("2"));
    }
}




