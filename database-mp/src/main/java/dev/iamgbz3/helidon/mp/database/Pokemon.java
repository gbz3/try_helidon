
package dev.iamgbz3.helidon.mp.database;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * A Pokemon entity class. A Pokemon is represented as a triple of an
 * ID, a name and a type.
 *
 * Pokemon, and Pokemon character names are trademarks of Nintendo.
 */
@Entity(name = "Pokemon")
@Table(name = "POKEMON")
@Access(AccessType.PROPERTY)
@NamedQueries({
        @NamedQuery(name = "getPokemons",
                    query = "SELECT p FROM Pokemon p"),
        @NamedQuery(name = "getPokemonByName",
                    query = "SELECT p FROM Pokemon p WHERE p.name = :name")
})
public class Pokemon {

    private int id;

    private String name;

    private PokemonType pokemonType;

    private int type;

    public Pokemon() {
    }

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToOne
    public PokemonType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(PokemonType pokemonType) {
        this.pokemonType = pokemonType;
        this.type = pokemonType.getId();
    }

    @Transient
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
