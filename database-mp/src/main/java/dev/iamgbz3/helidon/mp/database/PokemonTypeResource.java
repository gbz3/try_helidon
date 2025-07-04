
package dev.iamgbz3.helidon.mp.database;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This class implements a REST endpoint to retrieve Pokemon types.
 *
 * <ul>
 * <li>GET /type: Retrieve list of all pokemon types</li>
 * </ul>
 *
 * Pokemon, and Pokemon character names are trademarks of Nintendo.
 */
@Path("type")
public class PokemonTypeResource {

    @PersistenceContext(unitName = "pu1")
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PokemonType> getPokemonTypes() {
        return entityManager.createNamedQuery("getPokemonTypes", PokemonType.class).getResultList();
    }
}
