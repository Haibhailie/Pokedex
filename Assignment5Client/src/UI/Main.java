package UI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.input.MouseEvent;
import Model.Tokimon;

import javax.security.auth.callback.LanguageCallback;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
 * Client side main class
 * This handles all the client side output, including creating Vboxes, Hboxes, and the Stage.
 * Only 2 main windows, one for start and view all TOkimon
 * Another window for View individual Tokimon and Edit
 * Also handles adjusting the hue of the Tokimon based on the colour, handles 6 Colours - Red, Orange, Yellow, Blue, Violet, Green
 * Changes the height and width of Tokimon view based on actual height and width (try to keep minimum at 80 or it becomes too small)
 */
public class Main extends Application {

    private ArrayList<Tokimon> localTokimonList = new ArrayList<>();
    private Image defaultImage = new Image("file:defaultImage.png");
    private Image fireType = new Image("file:fire.png", 50, 50, true, true);
    private Image iceType = new Image("file:ice.png", 50, 50, true, true);
    private Image flyingType = new Image("file:fly.png", 50, 50, true, true);
    private Image electricType = new Image("file:electric.png", 50, 50, true, true);

    private ArrayList<ImageView> defaultImageView = new ArrayList<>();
    private ArrayList<ImageView> abilityImage = new ArrayList<>();
    private ArrayList<Label> tokimonName = new ArrayList<>();

    private GridPane tokiGrid;
    private Button startButton;
    private GridPane welcomeGrid;
    private Label welcomeLabel;
    private VBox welcomeBox;

    private VBox tokidexBox;
    private HBox buttonBox;
    private ArrayList<VBox> tokimonBox = new ArrayList<>();
    private GridPane tokimonGrid;
    private Button addButton;

    private Label individualTokimonLabel;
    private VBox individualTokimonBox;
    private Button editButton;
    private Button deleteButton;
    private Button popupBackButton;

    private Label addTokiLabel;
    private TextField newTokiName;
    private TextField newTokiAbility;
    private TextField newTokiColour;
    private TextField newTokiHeight;
    private TextField newTokiWeight;
    private TextField newTokiStrength;
    private VBox newTokiVbox;
    private Button submitNewToki;
    private Button backButton;

    private Label editTokiLabel;
    private TextField editTokiName;
    private TextField editTokiAbility;
    private TextField editTokiColour;
    private TextField editTokiHeight;
    private TextField editTokiWeight;
    private TextField editTokiStrength;
    private VBox editTokiVbox;
    private Button submitEditToki;
    private Button editbackButton;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //didn't make it into fucntions, but definitly could have!
        String status = "off";
        Label viewLabel = new Label("Currently: ");
        Button toggleButton = new Button("Toggle");

        String finalStatus = status;
        toggleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    URL getURL = new URL("http://bobbystesla.me/api/toggle");
                    HttpURLConnection postConnection = (HttpURLConnection) getURL.openConnection();
                    postConnection.setRequestMethod("POST");
                    postConnection.connect();

                    if(postConnection.getResponseCode()==200) {
                        if (finalStatus.compareTo("off") == 0) {
                            viewLabel.setText("Currently: on");
                        } else if (finalStatus.compareTo("on") == 0) {
                            viewLabel.setText("Currently: off");
                        }
                    }
                    else{
                        System.out.println("ERROR. PLEASE CHECK SERVER!");
                    }

                    postConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            URL getURL = new URL("http://bobbystesla.me/api/status");
            HttpURLConnection getConnection = (HttpURLConnection) getURL.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
            status = br.readLine();
            getConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewLabel.setText("Currently: "+status);
        VBox viewBox = new VBox(20, viewLabel, toggleButton);
        viewBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(viewBox, 200, 200);
    }

    public void checkDataAndSubmit() {

        if (!newTokiName.getText().isEmpty() && !newTokiAbility.getText().isEmpty() && !newTokiColour.getText().isEmpty()
                && isNumber(newTokiHeight.getText()) && isNumber(newTokiWeight.getText()) && isNumber(newTokiStrength.getText())) {

            try {
                URL postURL = new URL("http://localhost:8080/api/tokimon/add");
                HttpURLConnection postConnection = (HttpURLConnection) postURL.openConnection();
                postConnection.setDoOutput(true);
                postConnection.setRequestMethod("POST");
                postConnection.setRequestProperty("Content-Type", "application/json");

                OutputStreamWriter writer = new OutputStreamWriter(postConnection.getOutputStream());
                writer.write("{\"name\":\"" + newTokiName.getText() + "\"," +
                        "\"ability\":\"" + newTokiAbility.getText() + "\"," +
                        "\"colour\":\"" + newTokiColour.getText() + "\"," +
                        "\"weight\":" + Integer.parseInt(newTokiWeight.getText()) + "," +
                        "\"height\":" + Integer.parseInt(newTokiHeight.getText()) + "," +
                        "\"strength\":" + Integer.parseInt(newTokiStrength.getText()) + "}");

                System.out.println("{\"name\":\"" + newTokiName.getText() + "\"," +
                        "\"ability\":\"" + newTokiAbility.getText() + "\"," +
                        "\"colour\":\"" + newTokiColour.getText() + "\"," +
                        "\"weight\":" + Integer.parseInt(newTokiWeight.getText()) + "," +
                        "\"height\":" + Integer.parseInt(newTokiHeight.getText()) + "," +
                        "\"strength\":" + Integer.parseInt(newTokiStrength.getText()) + "}");

                writer.flush();
                writer.close();
                postConnection.connect();
                System.out.println(postConnection.getResponseCode());
                postConnection.disconnect();
                startButton.fire();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showAlert();
        }
    }

    public void showAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION, "Please check your inputs.", ButtonType.OK);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.show();
    }

    public void setupAddTokiScreen() {

        Label tokiNameLabel = new Label("Name:");
        Label tokiAbilityLabel = new Label("Ability:");
        Label tokiColourLabel = new Label("Colour:");
        Label tokiStrengthLabel = new Label("Strength:");
        Label tokiWeightLabel = new Label("Weight");
        Label tokiHeightLabel = new Label("Height");


        addTokiLabel = new Label("Add a new Tokimon!");
        addTokiLabel.setFont(new Font("Calibri", 16));

        newTokiName = new TextField();
        newTokiName.setPromptText("Name");

        newTokiAbility = new TextField();
        newTokiAbility.setPromptText("Ability");

        newTokiColour = new TextField();
        newTokiColour.setPromptText("Colour");

        newTokiStrength = new TextField();
        newTokiStrength.setPromptText("Strength");

        newTokiWeight = new TextField();
        newTokiWeight.setPromptText("Weight");

        newTokiHeight = new TextField();
        newTokiHeight.setPromptText("Height");

        submitNewToki = new Button("Submit");
        backButton = new Button("Exit");

        newTokiVbox = new VBox(20, addTokiLabel, tokiNameLabel, newTokiName, tokiAbilityLabel, newTokiAbility, tokiColourLabel, newTokiColour, tokiStrengthLabel, newTokiStrength, tokiHeightLabel, newTokiHeight, tokiWeightLabel, newTokiWeight, submitNewToki, backButton);
        newTokiVbox.setAlignment(Pos.CENTER);
    }

    public void resetAllArrayLists() {
        defaultImageView = new ArrayList<>();
        abilityImage = new ArrayList<>();
        tokimonName = new ArrayList<>();
        tokimonBox = new ArrayList<>();
        localTokimonList = new ArrayList<>();
    }

    public void makeTokiDexGrid() {
        try {
            URL getURL = new URL("http://localhost:8080/api/tokimon/all");
            HttpURLConnection getConnection = (HttpURLConnection) getURL.openConnection();
            getConnection.setRequestMethod("GET");
            getConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
            String tokimonJsonString = br.readLine();
            parseGetRequestJson(tokimonJsonString);
            getConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeTokidexView();
    }

    public void parseGetRequestJson(String jsonString) {
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject presentTokiObj = jsonArray.getJSONObject(i);
            localTokimonList.add(new Tokimon(presentTokiObj.getString("name"), presentTokiObj.getString("ability"), presentTokiObj.getString("colour"), presentTokiObj.getFloat("strength"), presentTokiObj.getFloat("weight"), presentTokiObj.getFloat("height")));
        }
    }

    public void initializeStartupScreen() {
        addButton = new Button();
        welcomeLabel = new Label();
        welcomeLabel.setFont(new Font("Calibri", 20));
        welcomeLabel.setText("This is the TokiDex. It stores information about all the Tokimon you have caught.");
        welcomeLabel.setAlignment(Pos.CENTER);
        startButton = new Button("Start!");
        startButton.setMaxSize(180, 80);
        startButton.setFont(new Font("Calibri", 20));
        welcomeBox = new VBox(20, welcomeLabel, startButton);
        welcomeBox.setAlignment(Pos.CENTER);
    }

    public void initializeTokidexView() {
        tokiGrid = new GridPane();
        tokiGrid.setPadding(new Insets(10));
        tokiGrid.setHgap(10);
        tokiGrid.setVgap(10);
        addButton = new Button("Add Tokimon");

        for (int i = 0; i < localTokimonList.size(); i++) {
            tokimonName.add(new Label(localTokimonList.get(i).getName()));
            tokimonName.get(i).setFont(new Font("Calibri", 16));
            defaultImageView.add(new ImageView(defaultImage));
            if (localTokimonList.get(i).getColour().compareTo("Yellow") == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(0.0);
                defaultImageView.get(i).setEffect(colorAdjust);
            } else if (localTokimonList.get(i).getColour().compareTo("Red") == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(-0.35);
                defaultImageView.get(i).setEffect(colorAdjust);
            } else if (localTokimonList.get(i).getColour().compareTo("Blue") == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(+0.75);
                defaultImageView.get(i).setEffect(colorAdjust);
            } else if (localTokimonList.get(i).getColour().compareTo("Violet") == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(-0.75);
                defaultImageView.get(i).setEffect(colorAdjust);
            } else if (localTokimonList.get(i).getColour().compareTo("Green") == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(+0.35);
                defaultImageView.get(i).setEffect(colorAdjust);
            } else if (localTokimonList.get(i).getColour().compareTo("Orange") == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(-0.1);
                defaultImageView.get(i).setEffect(colorAdjust);
            }

            defaultImageView.get(i).setFitHeight(localTokimonList.get(i).getHeight());
            defaultImageView.get(i).setFitWidth(localTokimonList.get(i).getWeight());

            if (localTokimonList.get(i).getAbility().compareTo("Electrify") == 0)
                abilityImage.add(new ImageView(electricType));
            else if (localTokimonList.get(i).getAbility().compareTo("Freeze") == 0)
                abilityImage.add(new ImageView(iceType));
            else if (localTokimonList.get(i).getAbility().compareTo("Fly") == 0)
                abilityImage.add(new ImageView(flyingType));
            else
                abilityImage.add(new ImageView(fireType));

            int finalI = i;
            defaultImageView.get(i).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
                @Override
                public void handle(javafx.scene.input.MouseEvent onClickEvent) {

                    try {
                        Stage popupStage = new Stage();
                        popupStage.setTitle(localTokimonList.get(finalI).getName());
                        individualTokimonLabel = new Label(localTokimonList.get(finalI).getName());
                        individualTokimonLabel.setFont(new Font("Calibri", 16));

                        editButton = new Button("Edit Tokimon");
                        editButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                setupEditTokiScreen(finalI);
                                //change stage here
                                editTokiVbox.setStyle("-fx-border-style: solid inside;" +
                                        "-fx-border-width: 5;" +
                                        "-fx-border-insets: 0;" +
                                        "-fx-border-radius: 5;" +
                                        "-fx-border-color: #4040b3;");
                                Scene addTokiScene = new Scene(editTokiVbox, 350, 700);
                                popupStage.setScene(addTokiScene);
                                popupStage.show();

                                submitEditToki.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        if (!editTokiName.getText().isEmpty() && !editTokiAbility.getText().isEmpty() && !editTokiColour.getText().isEmpty()
                                                && isNumber(editTokiHeight.getText()) && isNumber(editTokiWeight.getText()) && isNumber(editTokiStrength.getText())) {
                                            sendEditPostRequest(finalI + 1);
                                            popupStage.close();
                                            startButton.fire();
                                        } else {
                                            showAlert();
                                        }
                                    }
                                });

                                editbackButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        popupStage.close();
                                        startButton.fire();
                                    }
                                });
                            }
                        });

                        deleteButton = new Button("Delete Tokimon");
                        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                sendDeleteRequest(finalI + 1);
                                startButton.fire();
                                popupStage.close();
                            }
                        });

                        deleteButton.setStyle("-fx-background-color: #f83c3c; ");
                        popupBackButton = new Button("Exit");
                        popupBackButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                startButton.fire();
                                popupStage.close();
                            }
                        });

                        VBox individualTokimonOptions = new VBox(10, individualTokimonLabel, abilityImage.get(finalI), editButton, deleteButton, popupBackButton);
                        individualTokimonOptions.setAlignment(Pos.CENTER);
                        individualTokimonBox = new VBox(10, defaultImageView.get(finalI));
                        individualTokimonBox.setAlignment(Pos.CENTER);

                        Label tokiAbility = new Label("Ability: " + localTokimonList.get(finalI).getAbility());
                        tokiAbility.setFont(new Font("Calibri", 16));
                        Label tokiColour = new Label("Colour: " + localTokimonList.get(finalI).getColour());
                        tokiColour.setFont(new Font("Calibri", 16));
                        Label tokiWeight = new Label("Weight: " + localTokimonList.get(finalI).getWeight());
                        tokiWeight.setFont(new Font("Calibri", 16));
                        Label tokiHeight = new Label("Height: " + localTokimonList.get(finalI).getHeight());
                        tokiHeight.setFont(new Font("Calibri", 16));
                        Label tokiStrength = new Label("Strength: " + localTokimonList.get(finalI).getStrength());
                        tokiStrength.setFont(new Font("Calibri", 16));

                        VBox individualTokimonData = new VBox(10, tokiAbility, tokiStrength, tokiColour, tokiHeight, tokiWeight);
                        individualTokimonData.setAlignment(Pos.CENTER);
                        Label individualName = new Label("Data of " + localTokimonList.get(finalI).getName());
                        individualName.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
                        individualName.setAlignment(Pos.TOP_CENTER);
                        HBox tokimonHbox = new HBox(10, individualTokimonData, individualTokimonBox, individualTokimonOptions);
                        tokimonHbox.setAlignment(Pos.TOP_CENTER);

                        VBox viewVerticalBox = new VBox(individualName, tokimonHbox);
                        viewVerticalBox.setAlignment(Pos.TOP_CENTER);

                        viewVerticalBox.setStyle("-fx-border-style: solid inside;" +
                                "-fx-border-width: 5;" +
                                "-fx-border-insets: 5;" +
                                "-fx-border-radius: 5;" +
                                "-fx-border-color: #4040b3;");

                        Scene tokimonScene = new Scene(viewVerticalBox, 750, localTokimonList.get(finalI).getHeight() + 150);
                        popupStage.setScene(tokimonScene);
                        popupStage.initStyle(StageStyle.UNDECORATED);
                        popupStage.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tokimonBox.add(new VBox(tokimonName.get(i), abilityImage.get(i), defaultImageView.get(i)));
            tokimonBox.get(i).setPrefSize(160, 100);
            tokimonBox.get(i).setAlignment(Pos.TOP_CENTER);
        }

        int presentToki = 0;
        for (int i = 0; i <= localTokimonList.size() / 4; i++) {
            for (int j = 0; j < 4; j++) {
                int tokiNumber = 4 * i + j;
                if (tokiNumber == localTokimonList.size())
                    break;
                tokiGrid.add(tokimonBox.get(presentToki), j, i);
                presentToki++;
            }
        }

        Label infoLabel = new Label("Click on any present Tokimon to View, Edit or Delete it!");
        infoLabel.setFont(new Font("Calibri", 12));

        HBox topHbox = new HBox(10, addButton, infoLabel);
        topHbox.setAlignment(Pos.CENTER);
        tokidexBox = new VBox(25, topHbox, tokiGrid);
        tokidexBox.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 5;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #1fc8db;");
        tokidexBox.setAlignment(Pos.CENTER);
    }

    public void sendEditPostRequest(int tokiID) {
        try {

            URL postURL = new URL("http://localhost:8080/api/tokimon/change/" + tokiID + "?");

            HttpURLConnection postConnection = (HttpURLConnection) postURL.openConnection();
            postConnection.setDoOutput(true);
            postConnection.setDoInput(true);
            postConnection.setRequestMethod("POST");

            List<NameValuePair> queryParameters = new ArrayList<>();

            queryParameters.add(new BasicNameValuePair("name", editTokiName.getText()));
            queryParameters.add(new BasicNameValuePair("colour", editTokiColour.getText()));
            queryParameters.add(new BasicNameValuePair("ability", editTokiAbility.getText()));
            queryParameters.add(new BasicNameValuePair("weight", editTokiWeight.getText()));
            queryParameters.add(new BasicNameValuePair("height", editTokiHeight.getText()));
            queryParameters.add(new BasicNameValuePair("strength", editTokiStrength.getText()));

            OutputStream os = postConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
            writer.write(appendQueryParameters(queryParameters));
            writer.flush();
            writer.close();
            postConnection.connect();
            System.out.println(postConnection.getResponseCode());
            postConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String appendQueryParameters(List<NameValuePair> queryParameters) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : queryParameters) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(pair.getName(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), StandardCharsets.UTF_8));
        }
        return result.toString();
    }

    public void setupEditTokiScreen(int tokiID) {

        editTokiLabel = new Label("Edit Tokimon!");
        editTokiLabel.setFont(new Font("Calibri", 16));

        Label tokiNameLabel = new Label("Name:");
        Label tokiAbilityLabel = new Label("Ability:");
        Label tokiColourLabel = new Label("Colour:");
        Label tokiStrengthLabel = new Label("Strength:");
        Label tokiWeightLabel = new Label("Weight");
        Label tokiHeightLabel = new Label("Height");

        editTokiName = new TextField();
        editTokiName.setText(localTokimonList.get(tokiID).getName());


        editTokiAbility = new TextField();
        editTokiAbility.setText(localTokimonList.get(tokiID).getAbility());


        editTokiColour = new TextField();
        editTokiColour.setText(localTokimonList.get(tokiID).getColour());


        editTokiStrength = new TextField();
        editTokiStrength.setText(String.valueOf(localTokimonList.get(tokiID).getStrength()));


        editTokiWeight = new TextField();
        editTokiWeight.setText(String.valueOf(localTokimonList.get(tokiID).getWeight()));


        editTokiHeight = new TextField();
        editTokiHeight.setText(String.valueOf(localTokimonList.get(tokiID).getHeight()));

        submitEditToki = new Button("Submit");
        editbackButton = new Button("Exit");

        editTokiVbox = new VBox(20, editTokiLabel, tokiNameLabel, editTokiName, tokiAbilityLabel, editTokiAbility, tokiColourLabel, editTokiColour, tokiStrengthLabel, editTokiStrength, tokiHeightLabel, editTokiHeight, tokiWeightLabel, editTokiWeight, submitEditToki, editbackButton);
        editTokiVbox.setAlignment(Pos.CENTER);

    }

    public void sendDeleteRequest(int tokiID) {
        try {
            URL deleteURL = new URL("http://localhost:8080/api/tokimon/" + tokiID);
            HttpURLConnection deleteConnection = (HttpURLConnection) deleteURL.openConnection();

            deleteConnection.setDoOutput(true);
            deleteConnection.setRequestMethod("DELETE");
            deleteConnection.setRequestProperty("Content-Type", "application/json");
            deleteConnection.connect();

            System.out.println(deleteConnection.getResponseCode());
            deleteConnection.disconnect();
            startButton.fire();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNumber(String strCheck) {
        if (strCheck == null) {
            return false;
        }
        try {
            float d = Float.parseFloat(strCheck);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
