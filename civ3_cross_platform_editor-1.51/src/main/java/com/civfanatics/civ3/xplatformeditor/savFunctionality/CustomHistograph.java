/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.savFunctionality;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;
import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.maxBy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Andrew
 */
public class CustomHistograph extends Application
{   
    
    private final static Comparator<Integer> VALUE = Comparator.comparing(Integer::intValue);
    
    private final static int Y_AXIS_GRANULARITY = 2000;
    
    private int numPlayers;
    private String label;
    private List<List<Integer>> scores;
    private List<String> civNames;
    private List<Color> colors;
    private List<Boolean> visible;
    
    public void provideScores(List<List<Integer>> scores) {
        this.scores = scores;
    }
    
    public void provideCivNames(List<String> names) {
        this.civNames = names;
    }
    
    public void sendData(String label, List<List<Integer>> scores, List<String> names, List<Color> colors) {
        numPlayers = scores.size();
        this.label = label;
        this.scores = scores;
        this.civNames = names;
        this.colors = colors;
        this.visible = new ArrayList<>(scores.size());
        for (String name : names) {
            visible.add(true);
        }
    }
  
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        //Defining Axis   
        final NumberAxis xaxis = new NumberAxis(0, scores.get(0).size() + 10, 10);  
        final NumberAxis yaxis = new NumberAxis(0, getScoresUpperBound(scores), Y_AXIS_GRANULARITY);  
          
        //Defining Label for Axis   
        xaxis.setLabel("Turn");  
        yaxis.setLabel(label);  
          
        //Creating the instance of linechart with the specified axis  
        LineChart linechart = new LineChart(xaxis,yaxis);
        linechart.getStylesheets().add("styles/styles.css");
        linechart.setCreateSymbols(true);
        
        for (int i = 0; i < civNames.size(); i++) {
            System.out.println(civNames.get(i));
        }
        
        final String[] symbolStyles = new String[scores.size()];
        for (int h = 0; h < scores.size(); h++) {
            Series<Number, Number> series = new XYChart.Series();  

            series.setName(civNames.get(h));
            for (int i = 0; i < scores.get(h).size(); i++) {
                System.out.println("Adding " + i + ", " + scores.get(h).get(i) + " to player " + (h + 1));
                series.getData().add(new Data(i, scores.get(h).get(i)));
            }
            
            linechart.getData().addAll(series);
            
            series.getNode().setOnMouseClicked(event -> {
//                series.getNode().setVisible(false);
//                for (XYChart.Data<Number, Number> data: series.getData()) {
//                    data.getNode().setVisible(false);
//                }
            });
            
            setSeriesColor(h, series, symbolStyles);
        }
        
        /*
        * Set remembered symbol colors (in Chart insertion order) on legend nodes.
        * We must use runLater here because the legend is only created on display.
        */
        Platform.runLater(() -> {
            for (Node node: linechart.lookupAll(".chart-legend-item-symbol"))
               for (String styleClass: node.getStyleClass())
                   if (styleClass.startsWith("series")) {
                       final int i = Integer.parseInt(styleClass.substring(6));
                       node.setStyle(symbolStyles[i]);
                       break;
                   }
           
            for (int i = 0; i < numPlayers; i++) {
                Legend l = (Legend)linechart.lookupAll(".chart-legend").iterator().next();
                LegendItem li = l.getItems().get(i);
                final int player = i;

                try {
                    Field f = LegendItem.class.getDeclaredField("label");
                    f.setAccessible(true);
                    Label legendLabel = (Label)f.get(li);
                    legendLabel.setOnMouseClicked(event -> {
                        if (!legendLabel.getStyleClass().contains("strikethrough")) {
                            legendLabel.getStyleClass().add("strikethrough");
                            toggleSeriesVisibility(linechart, player, false);
                        }
                        else {
                            legendLabel.getStyleClass().remove("strikethrough");
                            toggleSeriesVisibility(linechart, player, true);
                        }
                    });
                }
                catch(Exception ex) {
                    System.out.println(":(");
                }
            }
        });
          
        //setting Group and Scene   
        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(linechart, 0.0);
        AnchorPane.setBottomAnchor(linechart, 0.0);
        AnchorPane.setLeftAnchor(linechart, 0.0);
        AnchorPane.setRightAnchor(linechart, 0.0);
        anchor.getChildren().add(linechart);
        
        Scene scene = new Scene(anchor, 1280, 1024);
        primaryStage.setScene(scene);
        primaryStage.setTitle(label + " Histograph");
        primaryStage.show();
    }
    
    private void toggleSeriesVisibility(LineChart linechart, int player, boolean visible) {
        this.visible.set(player, visible);
        Series s = (Series)linechart.getData().get(player);
        s.getNode().setVisible(visible);
        for (Object o: s.getData()) {
            XYChart.Data<Number, Number> data = (Data)o;
            data.getNode().setVisible(visible);
        }
        ((NumberAxis)linechart.getYAxis()).setUpperBound(getScoresUpperBound(scores));
    }

    private void setSeriesColor(int h, Series<Number, Number> series, final String[] symbolStyles) {
        //Source: https://news.kynosarges.org/2017/05/14/javafx-chart-coloring/
        
        // convert Faction color to CSS format
        Color civColor = colors.get(h);
        final String color = String.format("rgba(%d, %d, %d, 1.0)",
                civColor.getRed(), civColor.getGreen(), civColor.getBlue());
        
        // set line color on Series node
        final String lineStyle = String.format("-fx-stroke: %s;", color);
        series.getNode().lookup(".chart-series-line").setStyle(lineStyle);
        
        // set symbol color on Data nodes, remember for legend nodes
        final String symbolStyle = String.format("-fx-background-color: %s, whitesmoke;", color);
        symbolStyles[h] = symbolStyle;
        for (XYChart.Data<Number, Number> data: series.getData())
            data.getNode().lookup(".chart-line-symbol").setStyle(symbolStyle);
    }
    
    int getScoresUpperBound(List<List<Integer>> scores) {
        int max = -1;
        int i = 0;
        for (List<Integer> scoreList : scores)  {
            if (visible.get(i)) {
                int maxPlayerScore = scoreList.stream().collect(maxBy(VALUE)).orElse(-1);
                if (maxPlayerScore > max) {
                    max = maxPlayerScore;
                }
            }
            i++;
        }
        return Y_AXIS_GRANULARITY * (max / Y_AXIS_GRANULARITY) + Y_AXIS_GRANULARITY;
    }
}
