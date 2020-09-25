package pl.edu.agh.casting_dss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.casting_dss.data.PossibleValues;
import pl.edu.agh.casting_dss.factories.ModelLoadingException;
import pl.edu.agh.casting_dss.gui.controller.MainController;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.model.MechanicalPropertiesModel;
import pl.edu.agh.casting_dss.model.Model;
import pl.edu.agh.casting_dss.solution.SolutionFinder;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;
import pl.edu.agh.casting_dss.utils.SystemConfigurationUtils;

import java.io.File;
import java.io.IOException;

import static pl.edu.agh.casting_dss.factories.PossibleValuesFactory.POSSIBLE_VALUES_FACTORY;
import static pl.edu.agh.casting_dss.factories.XGBModelFactory.XGB_MODEL_FACTORY;
import static pl.edu.agh.casting_dss.utils.MechanicalProperty.HB;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SystemConfiguration configuration = SystemConfigurationUtils.loadFromFile(Main.class.getResource("system_configuration.json"));
        DSSModel model = new DSSModel(configuration);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent parent = loader.load();
        MainController controller = loader.getController();
        controller.initModel(model);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
