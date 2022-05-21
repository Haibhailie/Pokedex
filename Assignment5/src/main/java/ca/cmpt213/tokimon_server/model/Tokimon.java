package ca.cmpt213.tokimon_server.model;

/*
 * The Tokimon class that the JSON file that is read is turned into
 * This Is the class that will be turned into an arraylist to store necessary information
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

    public void reduceTokiID(){
        tokiID--;
    }
    public void setTokiID(int tokiID) {
        this.tokiID = tokiID;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}
