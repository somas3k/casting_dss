package pl.edu.agh.casting_dss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.casting_dss.criterions.ProductionRange;
import pl.edu.agh.casting_dss.data.Norms;
import pl.edu.agh.casting_dss.utils.NormsUtil;
import pl.edu.agh.casting_dss.gui.controller.MainController;
import pl.edu.agh.casting_dss.gui.model.DSSModel;
import pl.edu.agh.casting_dss.utils.ProductionRangesUtils;
import pl.edu.agh.casting_dss.utils.SystemConfiguration;
import pl.edu.agh.casting_dss.utils.SystemConfigurationUtils;

import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SystemConfiguration configuration = SystemConfigurationUtils.loadFromFile(Main.class.getResource("system_configuration.json"));
        List<ProductionRange> productionRanges = ProductionRangesUtils.loadFromFile(Main.class.getResource("production_ranges.json"));
        Norms norms = NormsUtil.loadFromFile(Main.class.getResource("norms.json"));
        DSSModel model = new DSSModel(configuration, productionRanges, norms);
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent parent = loader.load();
        MainController controller = loader.getController();
        controller.initModel(model);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
