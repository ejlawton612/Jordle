import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

/**.
 * public class Jordle is subclass of Application
 * @author Emma Lawton
 * @version 1.0
 */

public class Jordle extends Application {
    private static int col = 0;
    private static int row = 0;
    private static Text phrase = new Text();
    private static HBox boxes = new HBox();
    private static VBox mn = new VBox();
    private static Scene scene = new Scene(mn, 800, 900);
    private static String word = "";
    private static String w = "";
    private static Text t = new Text();
    private static HBox txt = new HBox();
    private static HBox buttons = new HBox();
    private static boolean dark = false;
    private static String num = "";
    private static int red = -1;
    private static int green = -1;
    private static int blue = -1;
    private static int counter = 0;
    private static ArrayList<String> played = new ArrayList<String>();
    private static boolean alertBool = false;
    private static String alpha = "ABCDEFGHIJKLMNOPQESTUVWXYZ";
    private static HBox root1 = new HBox();

    /**.
     * start method initializes the set up
     * @param window the window that propagates at start
     */

    public void start(Stage window) {
        alertBool = false;
        window.setTitle("Jordle");
        played.clear();
        Jordle.word();
        played.add(word);
        Button btn = new Button("Restart");
        phrase.setText("Try guessing a word");
        phrase.setFont(Font.font("Curlz MT", FontWeight.EXTRA_BOLD, 30));
        Jordle.changeBackground(Color.WHITE);
        buttons.getChildren().add(phrase);
        Button d = new Button("Colors");
        buttons.getChildren().add(d);
        d.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Jordle.darkMode();
            }
        });
        buttons.getChildren().add(btn);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Jordle.restart();
            }
        });
        Button instBtn = new Button("Instructions");
        buttons.getChildren().add(instBtn);
        instBtn.setOnMouseClicked(e -> {
            boxes.requestFocus();
            Stage s = new Stage();
            HBox root = new HBox();
            s.setTitle("Instructions");
            Text instructions = new Text();
            root.setBackground(new Background(new BackgroundFill(Color.DARKVIOLET, null, null)));
            instructions.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
            instructions.setFill(Color.MISTYROSE);
            instructions.setText("To play guess a 5 letter word. \n"
                    + "If the letter is in the word and in the correct place it will turn green, "
                    + "if it is in the word \n"
                    + " but in the wrong place it will turn yellow, otherwise it will turn grey. You have 6 tries."
                    + " \n Good Luck!"
                    + " :)");
            root.getChildren().add(instructions);
            Scene sc = new Scene(root, 800, 200);
            s.setScene(sc);
            s.show();
        });
        for (int i = 0; i < 5; i++) {
            boxes.getChildren().add(Jordle.boxes());
        }
        t.setText("Jordle");
        t.setFont(Font.font("Curlz MT", FontWeight.EXTRA_BOLD, 50));
        t.setFill(Color.BLACK);
        txt.getChildren().add(t);
        boxes.setPadding(new Insets(40, 150, 40, 150));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        txt.setPadding(new Insets(20, 0, 0, 0));
        txt.setAlignment(Pos.CENTER);
        mn.getChildren().addAll(txt, boxes, buttons);
        window.setScene(scene);
        window.setResizable(false);
        boxes.requestFocus();
        window.show();
        mn.setOnKeyPressed(e -> {
            KeyCode c = e.getCode();
            String a = String.valueOf(e.getCode());
            Jordle.codeCheck(c, a);
        });
    }

    /**.
     * rect creates a standard rectangle to be added to the grid setup
     * @return Rectangle
     */
    public static Rectangle rect() {
        Rectangle rect = new Rectangle();
        rect.setFill(Color.TRANSPARENT);
        rect.setWidth(100);
        rect.setHeight(100);
        rect.setStroke(Color.BLACK);
        return rect;
    }
    /**.
     * main method launches the application
     * @param args the arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**.
     * boxes creates VBoxes of 6 rectangles to be the grid lines
     * @return VBox column of grid
     */
    public static VBox boxes() {
        VBox column = new VBox();
        for (int i = 0; i < 6; i++) {
            column.getChildren().add(Jordle.rect());
        }
        return column;
    }

    /**.
     * codeCheck takes in key input and directs it to the according actions based on its type
     * @param c KeyCode of the input
     * @param a KeyCode of the input as a string
     */
    public static void codeCheck(KeyCode c, String a) {
        if (c == KeyCode.BACK_SPACE) {
            if (col != 0) {
                col--;
                w = w.substring(0, col);
                VBox column = (VBox) boxes.getChildren().get(col);
                StackPane pane = (StackPane) column.getChildren().get(row);
                pane.getChildren().remove(1);
            }
        } else if (c == KeyCode.ENTER) {
            w = w.toLowerCase();
            if (col != 5) {
                Jordle.shortAlert();
            } else {
                if (w.equals(word)) {
                    Jordle.check();
                    Jordle.win();
                } else {
                    if (row == 5) {
                        Jordle.check();
                        Jordle.lose();
                    } else {
                        Jordle.check();
                    }
                }
                row++;
                col = 0;
                w = "";
            }

        } else if (c.isLetterKey()) {
            if (col < 5) {
                Text t1 = new Text();
                t1.setText(a);
                t1.setFont(Font.font("Curlz MT", FontWeight.EXTRA_BOLD, 60));
                VBox column = (VBox) boxes.getChildren().get(col);
                if (column.getChildren().get(row) instanceof StackPane) {
                    StackPane pane = (StackPane) column.getChildren().get(row);
                    pane.getChildren().add(t1);
                } else {
                    Rectangle r = (Rectangle) column.getChildren().get(row);
                    StackPane stack = new StackPane();
                    stack.getChildren().addAll(r, t1);
                    column.getChildren().add(row, stack);
                }
                w += a;
                col++;
            }
        }
    }

    /**.
     * phraseChange changes phrase at bottom according to win or loss
     * @param win boolean says whether or not user won game
     */
    public static void phraseChange(String win) {
        if (win.equals("yes")) {
            phrase.setText("You won");
            Jordle.changeFont(Color.WHITE);
            Jordle.changeBox(Color.MEDIUMPURPLE);
            Jordle.changeBackground(Color.GOLD);
        } else {
            Jordle.changeFont(Color.LIGHTGRAY);
            Jordle.changeBox(Color.RED);
            Jordle.changeBackground(Color.BLACK);
            phrase.setText("You lost, the word is " + word);
        }
    }

    /**.
     * word selects a random index based on length of the list
     */
    public static void word() {
        ArrayList<String> words = Words.list;
        if (played.size() >= words.size()) {
            alertBool = true;
        }
        int len = words.size();
        int index = (int) (Math.random() * len);
        Jordle.word = words.get(index);
    }

    /**.
     * color method fills the given rectangle based on the input color
     * @param color color to be filled
     * @param i the index of the rectangle to be filled
     */
    public static void color(Color color, int i) {
        VBox column = (VBox) boxes.getChildren().get(i);
        StackPane pane = (StackPane) column.getChildren().get(row);
        Rectangle r = (Rectangle) pane.getChildren().get(0);
        r.setFill(color);
    }

    /**.
     * win changes calls phraseChange with win being yes
     */
    public static void win() {
        Jordle.phraseChange("yes");
    }

    /**.
     * lose calls phraseChange with win being no
     */
    public static void lose() {
        Jordle.phraseChange("no");
    }

    /**.
     * count method counts how many instances of the given substring there are in the given word
     * @param wd the word to have the substring counted in
     * @param letter the substring to be counted
     * @return int the number of times the letter was in the word
     */
    public static int count(String wd, String letter) {
        int len = wd.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (wd.substring(i, i + 1).equals(letter)) {
                count += 1;
            }
        }
        return count;
    }

    /**.
     * check checks if the guessed letter is correct
     */
    public static void check() {
        w = w.toLowerCase();
        word = word.toLowerCase();
        for (int i = 0; i < 5; i++) {
            if (w.substring(i, i + 1).equals(word.substring(i, i + 1))) {
                Jordle.color(Color.GREEN, i);
            } else {
                if (word.contains(w.substring(i, i + 1))) {
                    Jordle.color(Color.YELLOW, i);
                } else {
                    Jordle.color(Color.GREY, i);
                }
            }
        }
        Jordle.doubleLetters();
    }

    /**.
     * restart resets the rectangles and the phrase
     */
    public static void restart() {
        boxes.requestFocus();
        phrase.setText("Try guessing a word");
        col = 0;
        row = 0;
        word = "";
        w = "";
        Jordle.changeFont(Color.BLACK);
        Jordle.changeBackground(Color.WHITE);
        t.setFont(Font.font("Curlz MT", 50));
        phrase.setFont(Font.font("Curlz MT", 30));
        boxes.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            boxes.getChildren().add(Jordle.boxes());
        }
        boxes.requestFocus();
        Jordle.word();
        while (played.contains(word)) {
            if (!alertBool) {
                Jordle.word();
            } else {
                Jordle.wordAlert();
                break;
            }
        }
        played.add(word);
        mn.setOnKeyPressed(e -> {
            KeyCode c = e.getCode();
            String a = String.valueOf(e.getCode());
            Jordle.codeCheck(c, a);
        });
        boxes.requestFocus();
    }

    /**.
     * darkMode contains all of the buttons with their color options
     */
    public static void darkMode() {
        Stage s = new Stage();
        HBox colors = new HBox();
        boxes.requestFocus();
        s.setTitle("Colors");
        Button original = new Button("Original");
        original.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boxes.requestFocus();

                phrase.setFont(Font.font("Curlz MT", FontWeight.EXTRA_BOLD, 30));
                t.setFont(Font.font("Curlz MT", FontWeight.EXTRA_BOLD, 60));
                Jordle.changeBackground(Color.WHITE);
                Jordle.changeFont(Color.BLACK);
                Jordle.changeBox(Color.WHITE);
            }
        });
        Button easter = new Button("Easter");
        easter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boxes.requestFocus();
                phrase.setFont(Font.font("Lucida Sans Typewriter", FontWeight.EXTRA_BOLD, 30));
                t.setFont(Font.font("Lucida Sans Typewriter", FontWeight.EXTRA_BOLD, 60));
                Jordle.changeBackground(Color.LIGHTGREEN);
                Jordle.changeFont(Color.PINK);
                Jordle.changeBox(Color.LIGHTSKYBLUE);
            }
        });
        Button pink = new Button("Pink");
        pink.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boxes.requestFocus();
                phrase.setFont(Font.font("Ink Free", FontWeight.EXTRA_BOLD, 30));
                t.setFont(Font.font("Ink Free", FontWeight.EXTRA_BOLD, 60));
                Jordle.changeBackground(Color.LIGHTPINK);
                Jordle.changeFont(Color.DEEPPINK);
                Jordle.changeBox(Color.LIGHTSALMON);
            }
        });
        Button bl = new Button("Blue");
        bl.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boxes.requestFocus();
                phrase.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 30));
                t.setFont(Font.font("Georgia", FontWeight.EXTRA_BOLD, 60));
                Jordle.changeBackground(Color.LIGHTBLUE);
                Jordle.changeFont(Color.DARKBLUE);
                Jordle.changeBox(Color.LIGHTSKYBLUE);
            }
        });
        Button dMode = new Button("Dark Mode");
        dMode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boxes.requestFocus();
                phrase.setFont(Font.font("Script", FontWeight.EXTRA_BOLD, 30));
                t.setFont(Font.font("Script", FontWeight.EXTRA_BOLD, 60));
                Jordle.changeBackground(Color.BLACK);
                Jordle.changeBox(Color.DARKBLUE);
                Jordle.changeFont(Color.WHITE);
            }
        });
        Button hallow = new Button("Halloween");
        hallow.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boxes.requestFocus();
                phrase.setFont(Font.font("Chiller", 50));
                t.setFont(Font.font("Chiller", 80));
                Jordle.changeBackground(Color.rgb(238, 129, 20));
                Jordle.changeBox(Color.PURPLE);
                Jordle.changeFont(Color.BLACK);
            }
        });
        colors.getChildren().addAll(easter, pink, bl, dMode, hallow, original);
        colors.setBackground(new Background(new BackgroundFill(Color.rgb(225, 138, 255), null, null)));
        colors.setSpacing(20);
        colors.setPadding(new Insets(10, 10, 10, 10));
        Scene sc = new Scene(colors, 550, 400);
        s.setScene(sc);
        s.show();
    }

    /**.
     * doubleLetters is method to account for double letters in the word
     */
    public static void doubleLetters() {
        String check = "";
        for (int i = 0; i < 5; i++) {
            VBox column = (VBox) boxes.getChildren().get(i);
            StackPane pane = (StackPane) column.getChildren().get(row);
            Rectangle r = (Rectangle) pane.getChildren().get(0);
            if (r.getFill() == Color.GREEN) {
                check += w.substring(i, i + 1);
            }
        }
        for (int i = 0; i < 5; i++) {
            VBox column = (VBox) boxes.getChildren().get(i);
            StackPane pane = (StackPane) column.getChildren().get(row);
            Rectangle r = (Rectangle) pane.getChildren().get(0);
            if (r.getFill() == Color.YELLOW) {
                if (Jordle.count(check, w.substring(i, i + 1)) >= (Jordle.count(word, w.substring(i, i + 1)))) {
                    r.setFill(Color.GREY);
                }
            }
            check += w.substring(i, i + 1);
        }
    }

    /**.
     * changeBackground changes the backgrounds of all of the panes to the given color
     * @param c the color to change the panes to
     */
    public static void changeBackground(Color c) {
        boxes.setBackground(new Background(new BackgroundFill(c, null, null)));
        buttons.setBackground(new Background(new BackgroundFill(c, null, null)));
        txt.setBackground(new Background(new BackgroundFill(c, null, null)));
        mn.setBackground(new Background(new BackgroundFill(c, null, null)));
    }

    /**.
     * changeFont changes the color of all of the text
     * @param c the color to change the text
     */
    public static void changeFont(Color c) {
        phrase.setFill(c);
        t.setFill(c);
    }

    /**.
     * changeBox chnages the colors of the rectangles backgrounds
     * @param c color to change the boxes to
     */
    public static void changeBox(Color c) {
        for (int i = 0; i < 5; i++) {
            VBox column = (VBox) boxes.getChildren().get(i);
            for (int j = row; j < 6; j++) {
                if (column.getChildren().get(j) instanceof StackPane) {
                    StackPane pane = (StackPane) column.getChildren().get(j);
                    Rectangle r = (Rectangle) pane.getChildren().get(0);
                    r.setFill(c);
                } else {
                    Rectangle r = (Rectangle) column.getChildren().get(j);
                    r.setFill(c);
                }
            }
        }
    }

    /**.
     * shortAlert causes an alert to propogate when guess is not 5 letters
     */
    public static void shortAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Too short");
        alert.setContentText("Guess must be 5 letters, please try again.");
        alert.show();
    }

    /**.
     * wordAlert creates an alert when all words have been used
     */
    public static void wordAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No more words");
        alert.setContentText("You have played all of the words,\n please close the game and open it again");
        alert.show();
    }
}