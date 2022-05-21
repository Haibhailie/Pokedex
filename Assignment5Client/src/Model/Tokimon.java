package Model;

/*
 *Handles the local storage of the tokimon.
 * Only taken and used during initialization
 */
public class Tokimon {

    private static int totalToki = 0;
    private int tokiID;
    private String name;
    private String ability;
    private String colour;
    private float strength;
    private float weight;
    private float height;

    public Tokimon(String name, String ability, String colour, float strength, float weight, float height) {

        totalToki++;
        this.tokiID=totalToki;
        this.name = name;
        this.ability = ability;
        this.colour = colour;
        this.strength = strength;
        this.weight = weight;
        this.height = height;
    }


    public int getTokiID() {
        return tokiID;
    }

    public String getName() {
        return name;
    }

    public String getAbility() {
        return ability;
    }

    public String getColour() {
        return colour;
    }

    public float getStrength() {
        return strength;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }
}
