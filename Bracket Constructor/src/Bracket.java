/*
 * Author: Saul Manzano
 * File Name: Bracket.java
 * Purpose: Purpose of this project is to read in a file of names with wins and loss and order the names for a bracket. Once the order is determined
 * and the "Create" button is pressed a bracket will appear with the names. 
 * 
 * Sample File:
 * Saul:5,7 3,4 5,5
   Ryan:7,7 4,4 4,5
   Gabe:6,7 3,4 3,5
   John:0,7 2,4 2,5

 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Bracket extends Application {
    int WINDOW_SIZE = 500;
	
	public static void main(String [] args) {
		
		launch(args);
	}
	
	public void start(Stage stage) {
		ArrayList<String> lines = makeLines("players.txt");
		HashMap<String, Player> map = new HashMap<String, Player>();
		
        makePlayers(lines,map);
      
//-------------------------------------------------------------------------------------
        
		// Initialize JavaFX window requirements, including canvas and graphics context
		Group root = new Group();
		Canvas canvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
        BorderPane p = new BorderPane();
        TextField cmd_in = new TextField();
        cmd_in.setPrefSize(500, 0);
        Button button = new Button("Enter");
        button.setPrefSize(500, 0);
        Button create = new Button("Create");
        create.setPrefSize(1000, 0);
        final int num_items = 2;
        HBox input_box = new HBox(num_items);
        input_box.getChildren().add(cmd_in);
        input_box.getChildren().add(button);
        p.setTop(canvas);    
        p.setCenter(input_box);
        p.setBottom(create);
        root.getChildren().add(p);
        button.setOnAction(new HandleTextInput(cmd_in, map));
        create.setOnAction((event)->{buildBracket(gc,map);});

		
        stage.setTitle("Bracket");
		stage.setScene(new Scene(root));
		stage.show();
		
	}
	/*
	 * Displays name along with a line by their name on the canvas 
	 */
	public static void buildBracket(GraphicsContext gc, HashMap<String, Player> map) {
		ArrayList<Player> roster = sortList(new ArrayList<Player>(map.values()));
        ArrayList<Player> finalList = bracketOrder(roster, new ArrayList<Player>()); 
        for(int i=0;i<finalList.size();i++) {
        	gc.fillText(finalList.get(i).getName(), 40, i*40+40, 100);
        	Line l = new Line(141,i*40+40,191,i*40+90);
        	gc.fillRect(141,i*40+30,50,5);
        }
        
	}
	/*
	 * used to handle events for the enter button
	 */
	class HandleTextInput implements EventHandler<ActionEvent> {
        private GraphicsContext gc;
        private TextField cmd_in;
        private HashMap<String, Player> map;
     

        public HandleTextInput(TextField cmd_in,HashMap<String, Player> map) {
            this.cmd_in = cmd_in;
            this.gc = gc;
            this.map = map;

            
        }

        /*
         * This handle when the button labeled enter is clicked is clicked
         */
        @Override
        public void handle(ActionEvent event) {
            // System.out.println(cmd_in.getText());
            String[] data = cmd_in.getText().split(":");
            cmd_in.clear();
    		Player p = new Player(data[0],data[1].split(" "));
    		map.put(p.getName(), p);
    		System.out.println(map);
        }

        
	}
	/*
	 * uses sorted list to get the final order to match best player to worst
	 */
	public static ArrayList<Player> bracketOrder(ArrayList<Player> roster, ArrayList<Player>finalList){
    	if(roster.size()==1 || roster.size()==0) {
    		finalList.addAll(roster);
    		return finalList;
    	}else {
    		finalList.add(roster.get(0));
    		finalList.add(roster.get(roster.size()-1));
    		roster.remove(0);
    		roster.remove(roster.size()-1);
    		return bracketOrder(roster,finalList);
    	}
    }
    /*
     * takes the data from file and converts to player objects to later be sorted
     */
    private static void makePlayers(ArrayList<String> lines, HashMap<String, Player> map) {
    
    	for(int i=0;i<lines.size();i++) {
    		String[] data = lines.get(i).split(":");
    		Player p = new Player(data[0],data[1].split(" "));
    		map.put(p.getName(), p);
    		
    	}
		
	}
	/*
     * takes a two sorted list and sews them together so then list goes from greatest to least
     */
    public static ArrayList<Player> mergeLists(ArrayList<Player> l1, ArrayList<Player> l2, ArrayList<Player> merged){
    	
    	if(l1.size() == 0 || l2.size()==0) {
    		merged.addAll(l1);
    		merged.addAll(l2);
    		return merged;
    	}
    	else {
    		if(l1.get(0).getPercent()>l2.get(0).getPercent()) {
        		merged.add(l1.get(0));
        		l1.remove(0);
        	}else {
        		merged.add(l2.get(0));
        		l2.remove(0);
        	}
        	return mergeLists(l1,l2,merged);
    	}
    	
    }
    /*
     * makes a new list given the start and end of an old list
     */
    public static ArrayList<Player> newCopy(int start,int end, ArrayList<Player> lis){
    	ArrayList<Player> l = new ArrayList<Player>();
    	for(int i = start;i<end;i++) {
    		l.add(lis.get(i));
    	}
    	return l;
    }
    /*
     * takes a list and sorts it by first splitting up the list till each index is an individual then once 
     * there is only one element per list mergeLists puts the list together so they are sorted
     */
    public static ArrayList<Player> sortList(ArrayList<Player> lis){
    	if (lis.size()<=1) {
    		return lis;
    	}else {
    		int mid = lis.size()/2;
    		ArrayList<Player> l1 = newCopy(0,mid,lis);
    		ArrayList<Player> l2 = newCopy(mid,lis.size(),lis);
    		ArrayList<Player> sl1 = sortList(l1);
    		ArrayList<Player> sl2 = sortList(l2);
    		return mergeLists(sl1,sl2, new ArrayList<Player>());
    		
    	}
    } 
    /*
     * reads in contents of a file and puts them in a string arraylist
     */
    public static ArrayList<String> makeLines(String fileName) {
        Scanner file = null;
        try {
            file = new Scanner(new File(fileName));
        } catch (FileNotFoundException error) {
            error.printStackTrace();
        }
        ArrayList<String> data = new ArrayList<String>();
        try {
            while (file.hasNext()) {
                String line = file.nextLine();
                data.add(line);
            }
        } finally {
            file.close();
        }
        return data;
    }
	
}

	

