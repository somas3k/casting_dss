package pl.edu.agh.casting_dss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.gui.controller.MainController;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.solution.SolutionFinder;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;
import pl.edu.agh.casting_dss.utils.SystemConfigurationUtils;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DSSModel model = new DSSModel();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent parent = loader.load();
        MainController controller = loader.getController();
        SystemConfiguration configuration = SystemConfigurationUtils.loadFromFile(Main.class.getResource("system_configuration.json"));
        controller.initModel(model);
        controller.setConfiguration(configuration);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
