package sample;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
/*
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.geometry.*;
import javafx.event.*;
*/

/** Constructs a scene with a pannable Map background. */
public class PannableView extends Application {
    private Image backgroundImage;

    private int draw;
    private Boolean mouseMoved;

    @Override public void init() {
        File file = new File("/Users/dcard/Desktop/NarniaMap.jpg");
        backgroundImage = new Image(file.toURI().toString());
        draw = 1;
    }

    @Override public void start(Stage stage) throws Exception {
        mouseMoved = false;

        stage.setTitle("Drag the mouse to pan the map");

        // create a canvas, and draw something in it
        Canvas canvas = new Canvas(2200, 2200);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);

        // create a borderPane with subpanes
        BorderPane border = new BorderPane();

        // add something to the top pane
        border.setTop(addHBox());

        // construct the scene contents over a stacked background.
        StackPane layout = new StackPane();
        layout.getChildren().setAll(
                new ImageView(backgroundImage),
                canvas,
                createKillButton()
        );


        // wrap the scene contents in a pannable scroll pane.
        final ScrollPane scroll = createScrollPane(layout);

        border.setCenter(scroll);

        // show the scene.
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.show();

        // bind the preferred size of the scroll area to the size of the scene.
        scroll.prefWidthProperty().bind(scene.widthProperty());
        scroll.prefHeightProperty().bind(scene.widthProperty());

        // center the scroll contents.
        scroll.setHvalue(scroll.getHmin() + (scroll.getHmax() - scroll.getHmin()) / 2);
        scroll.setVvalue(scroll.getVmin() + (scroll.getVmax() - scroll.getVmin()) / 2);

        scroll.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseMoved = false;
            }
        });

        scroll.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseMoved = true;
            }
        });

        scroll.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!mouseMoved) {
                    System.out.print(event.getSceneX() + ", " + event.getSceneY());
                    System.out.println("; " + scroll.getHvalue() + ", " + scroll.getVvalue());
                }
            }
        });


        int radius = 10;
        canvas.setOnMouseClicked(event -> {
            if (!mouseMoved) {
                if (draw > 0) {
                    gc.setFill(Color.GREEN);
                    //gc.setStroke(Color.BLUE);
                    //gc.setLineWidth(5);
                    // draw an oval
                    gc.fillOval(event.getX() - radius / 2, event.getY() - radius / 2, radius, radius);
                }
                else {
                    // erase a rectangle
                    gc.clearRect(event.getX() - radius / 2, event.getY() - radius / 2, radius, radius);
                }
                draw *= -1;
            }
        });

    }

    /** @return a control to place on the scene. */
    private Button createKillButton() {
        final Button killButton = new Button("Kill the evil witch");
        killButton.setStyle("-fx-base: firebrick;");
        killButton.setTranslateX(65);
        killButton.setTranslateY(-130);
        killButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                killButton.setStyle("-fx-base: forestgreen;");
                killButton.setText("Ding-Dong! The Witch is Dead");
            }
        });
        return killButton;
    }

    /** @return a ScrollPane which scrolls the layout. */
    private ScrollPane createScrollPane(Pane layout) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        scroll.setPrefSize(800, 600);
        scroll.setContent(layout);
        return scroll;
    }

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonCurrent = new Button("Current");
        buttonCurrent.setPrefSize(100, 20);

        Button buttonProjected = new Button("Projected");
        buttonProjected.setPrefSize(100, 20);
        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }


    public static void main(String[] args) { launch(args); }
}