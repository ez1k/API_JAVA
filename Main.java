/**
 *
 *  @author Wolańczyk Adam S22484
 *
 */

package API;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();


    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    TextField textField_country = new TextField();
    TextField textField_city = new TextField();
    TextField textField_currency = new TextField();

    Label label_country = new Label("Country :");
    Label label_city = new Label("City : ");
    Label label_currency = new Label("Currency : ");

    VBox root = new VBox(10,
            new HBox(10, label_country, textField_country),
            new HBox(10, label_city, textField_city),
            new HBox(10, label_currency, textField_currency)
    );



    Label weatherLabel = new Label("");
    Label rateLabel1 = new Label("");
    Label rateLabel2 = new Label("");

    VBox vboxLabels = new VBox(10, weatherLabel, rateLabel1, rateLabel2);
    vboxLabels.setPrefWidth(400);

    VBox vboxValues = new VBox(10);
    vboxValues.setPrefWidth(400);

    Button button = new Button("Get Weather and Rates");
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Service service = new Service(textField_country.getText());
        String weather = service.getWeather(textField_city.getText());
        weatherLabel.setText("Weather in "+service.getCity()+" : \n"+weather);
        rateLabel1.setText(service.getCurrency() +" to " + textField_currency.getText() +": "+ service.getRateFor(textField_currency.getText()).toString());
        String value = String.valueOf(service.getCurrency() +" to PLN "+service.getNBPRate());
        rateLabel2.setText(value);
        webEngine.load("http://pl.wikipedia.org/wiki/"+service.getCity());
        webView.setLayoutX(0);
        webView.setLayoutY(100);



      }
    });

    HBox hbox = new HBox(10, vboxLabels, vboxValues, webView);
    VBox mainVBox = new VBox(10, root, hbox, button);
    mainVBox.setPadding(new Insets(10));

    Scene scene = new Scene(mainVBox);

    primaryStage.setTitle("Weather and Exchange Rates");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}




