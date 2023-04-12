module Jbomber {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens entities.menu to javafx.graphics, javafx.fxml;
	opens entities.game to javafx.graphics, javafx.fxml;
	opens entities.player to javafx.graphics, javafx.fxml;
}
