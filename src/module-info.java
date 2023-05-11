module Jbomber {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.media;

	opens application to javafx.graphics, javafx.fxml, com.google.common;
	opens controllers to javafx.graphics, javafx.fxml, com.google.common;
}
