package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
		
	}
	
	//Função para abrir outra tela
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene(); //pega a MainScene do Main, que é a MainScene da janela principal da application
			VBox mainVBox = (VBox)(((ScrollPane)mainScene.getRoot()).getContent()); //.getRoot pega o primeiro elemento da View //.getCOntent pega o content do ScrollPane(que é o primeiro elemento da view)
			
			Node mainMenu = mainVBox.getChildren().get(0); //Guarda na variável o primeiro filho do MainVBox
			mainVBox.getChildren().clear(); //limpa todos os filhos do MainVBox
			
			mainVBox.getChildren().add(mainMenu); //Add o mainMenu novamente ao MainVBox
			mainVBox.getChildren().addAll(newVBox.getChildren()); //addAll adiciona uma coleção = filhos do newVBox
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch(IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
