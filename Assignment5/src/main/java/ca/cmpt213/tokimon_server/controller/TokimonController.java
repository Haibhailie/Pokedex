package ca.cmpt213.tokimon_server.controller;

import ca.cmpt213.tokimon_server.model.Tokimon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * The server controller, handles all the GET, POST and DELETE requests.
 * Takes in data from the Json file present in data/tokimon.json on initialization
 * Loads that up as the server "database"
 * Returns request code based on what's been done after being called
 */

@RestController
class BikeController {

    private BikeManager manager = new BikeManager();
    private ArrayList<Bike> bikeArrayList = new ArrayList<Bike>();

    //Setup server database
    @PostConstruct
    public void initializeBikeJson() throws IOException {
        Gson gson = new Gson();
        try {
            String jsonFile = new String(Files.readAllBytes(Paths.get("data/bike.json")));
            JSONArray bikeJsonArray = new JSONArray(jsonFile);

            for (int i = 0; i < bikeJsonArray.length(); i++) {
                JSONObject presentBikeObj = bikeJsonArray.getJSONObject(i);
                bikeArrayList.add(new Bike(presentBikeObj.getInt("year"), presentBikeObj.getString("make")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void updateJsonDatabase() throws JsonProcessingException {
        Gson gson = new Gson();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonString = objectWriter.writeValueAsString(bikeArrayList);
        try (FileWriter file = new FileWriter("data/bike.json")) {
            file.write(jsonString);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // i) Add a Bike
    @PostMapping("/api/bike/add")
    public Bike addNewBike(@RequestBody Bike newBike, HttpServletResponse response) throws JsonProcessingException {
        boolean flag = true;
        System.out.println("Bike POSTED.");
        for(int i=0;i<bikeArrayList.size();i++){
            if(bikeArrayList.get(i).getMake().compareTo(newBike.getMake())==0)
                flag=false;
        }
        if(flag==true) {
            bikeArrayList.add(newBike);
            updateJsonDatabase();
            response.setStatus(201, "Bike has been added to database.");
            return newBike;
        }
        else{
            throw new IllegalArgumentException();
        }
    }


    // ii) Find a Bike
    @GetMapping("/api/findBike/{tokiID}")
    public Bike getBikeFromID(@PathVariable int bikeID, HttpServletResponse response) {

        final boolean[] flag = {false};
        manager.getFiltered(new Filter() {
            @Override
            public void include(Bike bike) {
                for(int i=0;i<bikeArrayList.size();i++){
                    if(bikeArrayList.get(i).getId()==bikeID)
                        flag[0] =true;
                }
            }
        });
        if(true){
            response.setStatus(302, "Bike found");
            return bikeArrayList.get(bikeID);
        }

        if(flag[0] == false)
            response.setStatus(404, "Tokimon has not been found.");
        return null;
    }
}

//@RestController
public class TokimonController {

    private ArrayList<Tokimon> tokimonArrayList = new ArrayList<>();

    @GetMapping("/api/tokimon/{tokiID}")
    public Tokimon getTokiFromID(@PathVariable int tokiID, HttpServletResponse response) {

        for (int i = 0; i < tokimonArrayList.size(); i++) {
            if (tokiID == (i + 1)) {
                response.setStatus(200);
                return tokimonArrayList.get(i);
            }
        }
        response.setStatus(404, "Tokimon has not been found.");
        return null;
    }

    @PostConstruct
    public void initializeTokimonJson() throws IOException {
        Gson gson = new Gson();
        try {
            String jsonFile = new String(Files.readAllBytes(Paths.get("data/tokimon.json")));
            JSONArray tokimonJsonArray = new JSONArray(jsonFile);

            for (int i = 0; i < tokimonJsonArray.length(); i++) {

                JSONObject presentTokiObj = tokimonJsonArray.getJSONObject(i);
                tokimonArrayList.add(new Tokimon(presentTokiObj.getString("name"), presentTokiObj.getString("ability"), presentTokiObj.getString("colour"), presentTokiObj.getFloat("strength"), presentTokiObj.getFloat("weight"), presentTokiObj.getFloat("height")));

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @GetMapping("/api/tokimon/all")
    public ArrayList<Tokimon> getAllTokimon(HttpServletResponse response) {
        if (tokimonArrayList.isEmpty())
            response.setStatus(404);
        System.out.println("Endpoint for getting all the Tokimon");
        response.setStatus(200);
        try{
            Scanner sc = new Scanner(System.in);
            int x = sc.nextInt();
        }catch (IllegalArgumentException e){
            System.out.println(e);
        }
        return tokimonArrayList;
    }


    //curl -i -X POST -d "{\"name\":\"Pikachu\",\"ability\":\"Lightning\",\"colour\":\"Yellow\",\"strength\":40,\"weight\":20,\"height\":10}" -H "Content-Type:application/json" localhost:8080/tokimon/add
    @PostMapping("/api/tokimon/add")
    public ResponseEntity<String> addNewTokimon(@RequestBody Tokimon newTokimon, HttpServletResponse response) throws JsonProcessingException {
        System.out.println("Tokimon Add POSTED.");
        tokimonArrayList.add(newTokimon);
        rewriteJsonFile();
        return new ResponseEntity<>("Tokimon has been added.", HttpStatus.CREATED);
    }

    public void rewriteJsonFile() throws JsonProcessingException {
        Gson gson = new Gson();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonString = objectWriter.writeValueAsString(tokimonArrayList);
        try (FileWriter file = new FileWriter("data/tokimon.json")) {
            file.write(jsonString);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //curl -i -X POST localhost:8080/tokimon/change/1?name="Squirlte"^&colour="Blue"
    @PostMapping("/api/tokimon/change/{tokiID}")
    public ResponseEntity<String> editTokimon(@PathVariable int tokiID, @RequestParam(value="name", defaultValue = "") String name,
                                              @RequestParam(value="ability", defaultValue = "") String ability,
                                              @RequestParam(value="colour", defaultValue = "") String colour,
                                              @RequestParam(value="strength", defaultValue = "") String strength,
                                              @RequestParam(value="height", defaultValue = "") String height,
                                              @RequestParam(value="weight", defaultValue = "") String weight) throws JsonProcessingException {

        if (tokiID - 1 >= tokimonArrayList.size())
            return new ResponseEntity<>("OUT OF BOUNDS", HttpStatus.NOT_FOUND);

        if(name.compareTo("")!=0)
            tokimonArrayList.get(tokiID-1).setName(name);
        if(!ability.isEmpty())
            tokimonArrayList.get(tokiID-1).setAbility(ability);
        if(!colour.isEmpty())
            tokimonArrayList.get(tokiID-1).setColour(colour);
        if(!strength.isEmpty())
            tokimonArrayList.get(tokiID-1).setStrength(Float.parseFloat(strength));
        if(!height.isEmpty())
            tokimonArrayList.get(tokiID-1).setHeight(Float.parseFloat(height));
        if(!weight.isEmpty())
            tokimonArrayList.get(tokiID-1).setWeight(Float.parseFloat(weight));

        rewriteJsonFile();
        return new ResponseEntity<>("Tokimon Changed", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/tokimon/{tokiID}")
    public ResponseEntity<String> deleteToki(@PathVariable int tokiID, HttpServletResponse response) throws JsonProcessingException {

        int maxToki = tokimonArrayList.size();
        if (tokiID - 1 >= maxToki)
            return new ResponseEntity<>("OUT OF BOUNDS", HttpStatus.NOT_FOUND);
        System.out.println(tokimonArrayList.get(tokiID - 1).getName() + " is going to be removed.");
        tokimonArrayList.remove(tokiID - 1);
        for (int i = tokiID - 1; i < tokimonArrayList.size(); i++)
            tokimonArrayList.get(i).reduceTokiID();
        rewriteJsonFile();
        return new ResponseEntity<>("Tokimon Deleted", HttpStatus.NO_CONTENT);
    }
}
