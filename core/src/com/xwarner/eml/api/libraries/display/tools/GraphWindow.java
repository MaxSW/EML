package com.xwarner.eml.api.libraries.display.tools;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GraphWindow extends Application {

	private NumberAxis xAxis, yAxis;
	private LineChart<Number, Number> lineChart;

	public static ArrayList<NumericVariable> defs;
	public static NumericVariable var;
	public static double a, b, n;
	public static Bundle bundle;

	public void start(Stage stage) throws Exception {
		stage.setTitle("EML Graph");

		StackPane root = new StackPane();

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		root.getChildren().add(lineChart);

		stage.setScene(new Scene(root, 800, 480));
		stage.show();

		setData();
	}

	public void setData() {

		xAxis.setLabel(var.getReference().getName());

		for (NumericVariable def : defs) {
			Series<Number, Number> series = new Series<Number, Number>();
			series.setName(def.getReference().getName());

			for (int i = 0; i < n + 1; i++) {
				double x = a + i * ((b - a) / n);
				var.setValue(BigDecimal.valueOf(x));
				double y = ((BigDecimal) def.getValue(bundle)).doubleValue();

				Data<Number, Number> dd = new Data<Number, Number>(x, y);
				Rectangle rect = new Rectangle(0, 0);
				rect.setVisible(false);
				dd.setNode(rect);
				series.getData().add(dd);
			}

			lineChart.getData().add(series);
		}

	}
}
