package player;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

/**
 * @Author Eric Bell
 * @version 1.00
 * */
public class Player extends Application{
    private FileChooser filechooser= new FileChooser();
    private Stage primaryStage;
    private MediaView videoView= new MediaView();
    private BorderPane pane= new BorderPane();
    private Slider timeSlider= new Slider();
    private Slider volumeSlider= new Slider();
    private Button playBtn = new Button("");
    private Button stopBtn= new Button("");
    private Text timer = new Text("00:00/00:00");
    private String totalDuration= "";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load in images
        ImageView stopImg= new ImageView("media/stop.png");
        stopImg.setFitHeight(15);
        stopImg.setPreserveRatio(true);
        stopBtn.setGraphic(stopImg);
        stopBtn.setPrefWidth(50);
        ImageView playImg = new ImageView("media/play.png");
        playImg.setFitHeight(15);
        playImg.setPreserveRatio(true);
        playBtn.setGraphic(playImg);
        playBtn.setPrefWidth(50);
        ImageView openImg= new ImageView("media/open.png");
        openImg.setFitHeight(15);
        openImg.setPreserveRatio(true);
        ImageView closeImg= new ImageView("media/close.png");
        closeImg.setFitHeight(15);
        closeImg.setPreserveRatio(true);
        ImageView docImg= new ImageView("media/documentation.png");
        docImg.setFitHeight(15);
        docImg.setPreserveRatio(true);
        ImageView exitImg= new ImageView("media/exit.png");
        exitImg.setFitHeight(15);
        exitImg.setPreserveRatio(true);
        ImageView infoImg= new ImageView("media/info.png");
        infoImg.setFitHeight(15);
        infoImg.setPreserveRatio(true);

        // Popups
        Stage docPop = new Stage();
        docPop.setTitle("Documentation");
        Text info = new Text("Press Space or P to Play/Pause the video!");
        info.setLayoutX(50);
        info.setLayoutY(50);
        Pane infoPane = new Pane();
        infoPane.setPrefSize(300, 100);
        infoPane.getChildren().add(info);
        docPop.setScene(new Scene(infoPane, 300, 100));

        Stage aboutPop= new Stage();
        aboutPop.setTitle("About");
        Text about = new Text("Created by Eric Bell\nVersion 1.00");
        about.setLayoutX(100);
        about.setLayoutY(50);
        Pane aboutPane = new Pane();
        infoPane.setPrefSize(300, 100);
        aboutPane.getChildren().add(about);
        aboutPop.setScene(new Scene(aboutPane, 300, 100));


        this.primaryStage= primaryStage;
        pane.setStyle("-fx-background-color: darkgrey");
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Micro Player");
        primaryStage.setScene(scene);
        pane.setPrefSize(1077,648);
        videoView.setFitHeight(598);
        // Setup the menubar
        MenuBar menubar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setGraphic(openImg);
        openMenuItem.setOnAction(this::chooseFile);
        MenuItem closeMenuItem = new MenuItem("Close");
        closeMenuItem.setGraphic(closeImg);
        closeMenuItem.setOnAction(this::closeFile);
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setGraphic(exitImg);
        exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        fileMenu.getItems().addAll(openMenuItem, closeMenuItem, exitMenuItem);
        Menu helpMenu = new Menu("Help"); // TODO or not TODO
        MenuItem docMenuItem = new MenuItem("Documentation");
        docMenuItem.setGraphic(docImg);
        docMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                docPop.showAndWait();
            }
        });
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setGraphic(infoImg);
        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutPop.showAndWait();
            }
        });
        helpMenu.getItems().addAll(docMenuItem, aboutMenuItem);
        menubar.getMenus().addAll(fileMenu, helpMenu);


        // Setup Controls
        HBox controls = new HBox();
        controls.setSpacing(5);
        playBtn.setOnAction(this::playPause);
        playBtn.setDisable(true);
        stopBtn.setOnAction(this::stop);
        stopBtn.setDisable(true);
        // Init the time Slider
        timeSlider.setDisable(true);
        timeSlider.setPrefWidth(900);
        timeSlider.setMin(0.0);
        timeSlider.setValue(0.0);
        timeSlider.setMax(1+0.0);
        // Seek if clicked/Dragged
        timeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoView.getMediaPlayer().seek(Duration.seconds(timeSlider.getValue()));
            }
        });
        timeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoView.getMediaPlayer().seek(Duration.seconds(timeSlider.getValue()));
            }
        });
        controls.getChildren().addAll(playBtn, stopBtn, timeSlider, timer);

        // Setup Volume Slider
        volumeSlider.setMin(0.0);
        volumeSlider.setValue(0.8);
        volumeSlider.setMax(1.0);
        volumeSlider.setDisable(true); // To be enabled later
        volumeSlider.setOrientation(Orientation.VERTICAL);
        volumeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoView.getMediaPlayer().setVolume(volumeSlider.getValue());
            }
        });
        volumeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoView.getMediaPlayer().setVolume(volumeSlider.getValue());
            }
        });


        pane.setTop(menubar);
        // Center gets set when opening a file
        pane.setBottom(controls);
        pane.setRight(volumeSlider);
        primaryStage.show();

        // Check if pausing by space or P keys
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.P){
                    playPause(new ActionEvent());
                }
            }
        });

    }

    // File chooser window
    private void chooseFile(ActionEvent event){
        filechooser.setTitle("Pick a File");
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.m4v", "*.m4a")
                //,new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.aac", "*.flac", "*.ogg")
        );
        File selectedFile = filechooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {

            closeFile(event); // close other videos
            Media movie= new Media(selectedFile.toURI().toString());
            MediaPlayer moviePlayer = new MediaPlayer(movie);
            moviePlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    totalDuration= getTimeString(moviePlayer.getTotalDuration().toMillis());
                    timeSlider.setMax(moviePlayer.getTotalDuration().toSeconds());
                    pane.setPrefSize(moviePlayer.getMedia().getWidth()+30, moviePlayer.getMedia().getHeight()+75); // This doesn't do anything unfortunately
                }
            });
            videoView.setMediaPlayer(moviePlayer);

            // Enable Controls
            playBtn.setDisable(false);
            stopBtn.setDisable(false);
            volumeSlider.setDisable(false);
            timeSlider.setDisable(false);

            // Setup time Slider0
            timeSlider.setValue(0);
            moviePlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() { // Update with every second passed
                @Override
                public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                    timeSlider.setValue(newValue.toSeconds());
                    timer.setText(getTimeString(newValue.toMillis()) + "/" + totalDuration);
                }
            });

            // Set pane center to mediaView
            pane.setCenter(videoView);


        }

        else System.out.println("Error Choosing Selected File");
    }

    // Thank you stackoverflow
    private String getTimeString(double millis) {
        StringBuffer buf = new StringBuffer();

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    private void closeFile(ActionEvent event) {
        playBtn.setDisable(true);
        stopBtn.setDisable(true);
        volumeSlider.setDisable(true);
        timeSlider.setDisable(true);

        if (videoView.getMediaPlayer()!= null) videoView.getMediaPlayer().stop();
        pane.setCenter(null);
    }

    // Set current MediaPlayer to play or pause based on current state
    private void playPause(ActionEvent event) {
        MediaPlayer currentPlayer= videoView.getMediaPlayer();
        if (currentPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            currentPlayer.pause();
        }
        else currentPlayer.play();
    }

    // Set Stop functions
    private void stop(ActionEvent event){
        MediaPlayer currentPlayer= videoView.getMediaPlayer();
        currentPlayer.pause();
        timeSlider.setValue(0);
        currentPlayer.seek(Duration.ZERO);
    }

}
