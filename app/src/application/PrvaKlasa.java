package application;




import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class PrvaKlasa extends Application {
	
	public static List<String> accounts = new LinkedList();
	
	public static DatabaseManager databaseManager;
	
	public static String databasePath;
	
	public static void main(String[] args) {
		
		//prvo ucitavamo excel fajl, pre pokretanja samog GUI-ja (da ne bismo pretrpavali fju start koja omoguvuje funkcionalnost GUIja)
		List<HashMap<String, String>> list;
		
		
		try{
			
			
			databasePath = args[4];
			//path to excel document
			list = XLSXLoader.XLSXToList(args[3]); 
			databaseManager = new DatabaseManager();
			databaseManager.initializeDatabase(list, databasePath); //second argument, database path
		
		}catch(Exception e){	
			e.printStackTrace();
		}	
		
		Application.launch(args);
		
	}

	
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Create transaction");
	
		primaryStage.setResizable(true);
		
		
		VBox main=new VBox();
		main.setSpacing(20);
		main.setAlignment(Pos.CENTER);
		
		
		ObservableList<String> observable = FXCollections.observableArrayList(accounts);
		
		HBox top=new HBox();
		top.setAlignment(Pos.CENTER);
		top.setSpacing(50);
		Label label1=new Label("Sender:");
		label1.setLineSpacing(50);
		ComboBox combo1=new ComboBox(observable);
		combo1.setMinSize(150, 25);
		top.getChildren().addAll(label1, combo1); 

		HBox medium=new HBox();
		medium.setAlignment(Pos.CENTER);
		medium.setSpacing(50);
		Label label2=new Label("Receiver:");
		label1.setLineSpacing(50);
		ComboBox combo2=new ComboBox(observable);
		combo2.setMinSize(150, 25);
		medium.getChildren().addAll(label2, combo2);


		HBox down=new HBox();
		down.setAlignment(Pos.CENTER);
		down.setSpacing(50);
		Label label3=new Label("Amount:");
		label1.setLineSpacing(50);
		TextField field3=new TextField();
		field3.setEditable(true);
		
		down.getChildren().addAll(label3, field3);
		
		Label outputText = new Label();
		outputText.setLineSpacing(50);
		outputText.setAlignment(Pos.CENTER);
		
		
		HBox least=new HBox();
		least.setAlignment(Pos.CENTER);
		Button button=new Button();
		button.setOnAction(new EventHandler<ActionEvent>()  {
			
			@Override
			public void handle(ActionEvent arg0) {
				try {
					
					databaseManager.updateDatabaseAfterTransaction(Integer.parseInt(field3.getText()),
							(String )combo1.getValue(), (String) combo2.getValue(), databasePath);
					outputText.setText("Your transaction is succesfuly processed!");
				
				} catch(RuntimeException re){
				
					outputText.setText("Unable to perform your transaction. Reason: Not enough funds on your account!");
				
				}catch(SQLException e1){
				
					e1.printStackTrace();
				
				}catch(Exception e2){
					e2.printStackTrace();
				}
				
			}
		});
		
		button.prefWidthProperty();
		button.prefHeightProperty();
		
		button.setText("Submit");
		least.getChildren().add(button);
	
		
		main.getChildren().add(top);
		main.getChildren().add(medium);
		main.getChildren().add(down);
		main.getChildren().add(outputText);
		main.getChildren().add(least);
		
		Scene scene = new Scene(main, 450, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}


