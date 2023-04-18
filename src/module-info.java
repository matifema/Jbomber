module Jbomber{
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens entities.menu to javafx.graphics, javafx.fxml;
	opens entities.gameField to javafx.graphics, javafx.fxml;
}
