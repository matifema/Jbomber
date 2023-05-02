module Jbomber{
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.media;
	requires com.google.common;
	
	opens application to javafx.graphics, javafx.fxml, com.google.common;
	opens controllers to javafx.graphics, javafx.fxml, com.google.common;
}
