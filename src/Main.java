import csc2b.gui.EditorPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Present scene to stage
        primaryStage.setTitle("Image Edit Pro");
        EditorPane layout = new EditorPane();
        Scene scene = new Scene(layout, 1250, 680);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
