package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private AnimationTimer timer;
    private static double rocketY = 0;
    private static double speed = 0;
    private static int fuel = 250;
    private static boolean throlle = false;
    private static boolean gameIsActive = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        theStage.setTitle("To Mars!");

        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        Canvas canvas = new Canvas(800, 512);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image space = new Image("resources/mars.png");
        Image fire = new Image("resources/rocket_fire.png");
        Image noFire = new Image("resources/rocket_nofire.png");
        Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 30 );
        gc.setFont( theFont );

        theScene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();
            buttonPressed(code);
        });

        theScene.setOnKeyReleased(e -> {
            String code = e.getCode().toString();
                buttonUnPressed(code);
        });

        timer = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                String speed1  = Double.valueOf(speed).toString().substring(0,4);

                gc.drawImage(space, 0, 0, 800, 700);
                if (throlle){
                    gc.drawImage(fire, 360, rocketY, 60, 60);
                }else {
                    gc.drawImage(noFire, 360, rocketY, 60, 60);
                }
                gc.fillText( "Топливо: " + fuel, 60, 50 );
                gc.fillText( "Скорость: " + speed1, 60, 100 );
            }
        };
        timer.start();
        game(gc);
        theStage.show();
    }

    private void game(GraphicsContext gc) {

        Thread thread = new Thread(() -> {
            while (gameIsActive) {
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rocketY = rocketY + speed;
                if (fuel<1) throlle = false;

                if (throlle){
                    fuel--;
                    speed = speed - 0.01;
                }
                speed = speed + 0.003;

                if (rocketY > 470 ){

                    if (speed < 0.2){
                        gc.fillText( "YOU WON!", 340, 300 );
                        timer.stop();
                        gameIsActive = false;
                    }else {
                        gc.fillText( "GAME OVER!", 340, 300 );
                        timer.stop();
                        gameIsActive = false;
                    }

                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void buttonPressed(String button){
        if (fuel>0) {
            throlle = true;
        }
    }
    private void buttonUnPressed(String button){
          throlle = false;
    }
}
