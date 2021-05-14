package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.*;


import javafx.event.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Board gameBoard = new Board();
        Scanner levelDataReader = new Scanner(new File("src//sample//data//levels.csv"));

        InputStream waterStream = new FileInputStream("src//sample//data//water.png");
        Image waterSprite = new Image(waterStream);
        InputStream missStream = new FileInputStream("src//sample//data//watermiss.png");
        Image missSprite = new Image(missStream);
        InputStream hitStream = new FileInputStream("src//sample//data//waterhit.png");
        Image hitSprite = new Image(hitStream);
        InputStream sunkStream = new FileInputStream("src//sample//data//watersunk.png");
        Image sunkSprite = new Image(sunkStream);
        Text missileText = new Text(10, 680, "Missiles remaining: " + gameBoard.getMissiles());
        missileText.setFont(new Font(20));
        Text shipText = new Text(10, 700, "Ships sunk: " + gameBoard.getShipSunkCount()+"/"+gameBoard.getShipCount());
        shipText.setFont(new Font(20));
        Text gameOverText = new Text(125, 350, "GAME OVER!");
        gameOverText.setFont(new Font(72));
        Alert restartAlert = new Alert(AlertType.NONE, "Restart level?", ButtonType.OK);


        Group imgGroup = new Group();

        boardSetup(gameBoard, levelDataReader, true, primaryStage);
        drawBoard(gameBoard, waterSprite, missSprite, hitSprite, sunkSprite, missileText, shipText, imgGroup);

        imgGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent clickEvent) {
                int clickedTileX = (int) Math.floor(clickEvent.getSceneX() / 64);
                int clickedTileY = (int) Math.floor(clickEvent.getSceneY() / 64);
                if (gameBoard.getTileId(clickedTileX, clickedTileY) <= 1 && gameBoard.getIsActive() == true && clickedTileY <= 9) {
                    tileSetter(gameBoard, gameBoard.getTileId(clickedTileX, clickedTileY) + 2, clickedTileX, clickedTileY);
                    gameBoard.addMissiles(-1);
                    int checkShipsSunk = 0;
                    for (Ship ships : gameBoard.getShipList()) {
                        if (ships.isShipSunk(gameBoard)) {
                            checkShipsSunk++;
                        }
                    }
                    gameBoard.setShipSunkCount(checkShipsSunk);
                    if (checkShipsSunk == gameBoard.getShipCount()) {
                        gameBoard.setIsActive(false);
                        restartAlert.setTitle("You win!");
                        restartAlert.setContentText("Proceed to next level?");
                        restartAlert.showAndWait().ifPresent((btnType) -> {
                            if (btnType == ButtonType.OK) {
                                boardSetup(gameBoard, levelDataReader, true, primaryStage);
                                drawBoard(gameBoard, waterSprite, missSprite, hitSprite, sunkSprite, missileText, shipText, imgGroup);
                            }
                        });
                    }
                }
                drawBoard(gameBoard, waterSprite, missSprite, hitSprite, sunkSprite, missileText, shipText, imgGroup);
                if (gameBoard.getMissiles() <= 0) {
                    gameBoard.setIsActive(false);
                    restartAlert.setTitle("You lose!");
                    restartAlert.setContentText("Restart level?");
                    restartAlert.showAndWait().ifPresent((btnType) -> {
                        if (btnType == ButtonType.OK) {
                            boardSetup(gameBoard, levelDataReader, false, primaryStage);
                            drawBoard(gameBoard, waterSprite, missSprite, hitSprite, sunkSprite, missileText, shipText, imgGroup);
                        }
                    });
                }
            }

            public void tileSetter(Board gameBoard, int tileId, int tileX, int tileY) {
                gameBoard.setTileId(tileId, tileX, tileY);
            }
        });

        imgGroup.getChildren().add(missileText);
        imgGroup.getChildren().add(shipText);

        primaryStage.setTitle("Level 1 - Battleship FX");
        primaryStage.setScene(new Scene(imgGroup, 640, 720));
        primaryStage.show();

    }

    public static void drawBoard(Board gameBoard, Image wSpr, Image mSpr, Image hSpr, Image sSpr, Text mText, Text sText, Group imgGrp) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ImageView tileView = new ImageView();
                if (gameBoard.getTileId(i, j) == 0 || gameBoard.getTileId(i, j) == 1) {
                    tileView.setImage(wSpr);
                }
                if (gameBoard.getTileId(i, j) == 2) {
                    tileView.setImage(mSpr);
                }
                if (gameBoard.getTileId(i, j) == 3) {
                    tileView.setImage(hSpr);
                }
                if (gameBoard.getTileId(i, j) == 4) {
                    tileView.setImage(hSpr);
                }
                if (gameBoard.getTileId(i, j) == 5) {
                    tileView.setImage(sSpr);
                }
                tileView.setX(i * 64);
                tileView.setY(j * 64);
                mText.setText("Missiles remaining: " + gameBoard.getMissiles());
                sText.setText("Ships sunk: " + gameBoard.getShipSunkCount()+"/"+gameBoard.getShipCount());
                imgGrp.getChildren().add(tileView);
            }
        }
    }

    public static void boardSetup(Board gboard, Scanner levelGetter, boolean isNextLevel, Stage stage) {
        gboard.setIsActive(true);
        gboard.clearBoard();

        if (isNextLevel) {
            gboard.setLevelString(levelGetter.nextLine());
            gboard.setLevel(gboard.getLevel()+1);
        }
        stage.setTitle("Level "+gboard.getLevel()+" - Battleship FX");

        Scanner levelReader = new Scanner(gboard.getLevelString());
        levelReader.useDelimiter(",");
        int levelIterator = 1;
        while (levelReader.hasNext()) {
            String levelReaderString = levelReader.next();
            if (levelIterator == 1) {
                gboard.setMissiles(Integer.parseInt(levelReaderString));
            } else {
                gboard.placeShip(Integer.parseInt(levelReaderString));
            }
            levelIterator++;
        }
        levelReader.close();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
