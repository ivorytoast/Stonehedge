package com.snow.stonehedge.ui;

import com.snow.stonehedge.enigma.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class HelloFX extends Application {

    Symbol currentSymbol = new Symbol("spx");
    ObservableList<Order> bidList = FXCollections.observableArrayList();
    ObservableList<Order> askList = FXCollections.observableArrayList();
    Stage window;
    TableView<Order> bidsTable;
    TableView<Order> asksTable;
    TableView<Order> completedOrdersTable;
    Label underlier, bestPrice;
    TextField priceInput, quantityInput;

    StringProperty underlierProperty = new SimpleStringProperty(currentSymbol.underlier);
    DoubleProperty bestPriceProperty = new SimpleDoubleProperty(currentSymbol.asks.bestPrice);

    ObservableList<Order> completedOrders = FXCollections.observableArrayList(currentSymbol.bids.completedOrders);

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Stonehedge");
        window.setMinWidth(650);

        TableColumn<Order, Double> ID_column = new TableColumn<>("ID");
        ID_column.setMinWidth(100);
        ID_column.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, Double> PRICE_column = new TableColumn<>("Price");
        PRICE_column.setMinWidth(100);
        PRICE_column.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Order, Long> QUANTITY_column = new TableColumn<>("Quantity");
        QUANTITY_column.setMinWidth(100);
        QUANTITY_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, Long> ORIGINAL_QAUNTITY_column = new TableColumn<>("Original Quantity");
        ORIGINAL_QAUNTITY_column.setMinWidth(150);
        ORIGINAL_QAUNTITY_column.setCellValueFactory(new PropertyValueFactory<>("originalQuantity"));

        TableColumn<Order, Double> ID_column2 = new TableColumn<>("ID");
        ID_column2.setMinWidth(100);
        ID_column2.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, Double> PRICE_column2 = new TableColumn<>("Price");
        PRICE_column2.setMinWidth(100);
        PRICE_column2.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Order, Long> QUANTITY_column2 = new TableColumn<>("Quantity");
        QUANTITY_column2.setMinWidth(100);
        QUANTITY_column2.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, Long> ORIGINAL_QAUNTITY_column2 = new TableColumn<>("Original Quantity");
        ORIGINAL_QAUNTITY_column2.setMinWidth(150);
        ORIGINAL_QAUNTITY_column2.setCellValueFactory(new PropertyValueFactory<>("originalQuantity"));

        TableColumn<Order, Double> ID_column3 = new TableColumn<>("ID");
        ID_column3.setMinWidth(100);
        ID_column3.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, Double> PRICE_column3 = new TableColumn<>("Price");
        PRICE_column3.setMinWidth(100);
        PRICE_column3.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Order, Long> QUANTITY_column3 = new TableColumn<>("Quantity");
        QUANTITY_column3.setMinWidth(100);
        QUANTITY_column3.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, Long> ORIGINAL_QAUNTITY_column3 = new TableColumn<>("Original Quantity");
        ORIGINAL_QAUNTITY_column3.setMinWidth(150);
        ORIGINAL_QAUNTITY_column3.setCellValueFactory(new PropertyValueFactory<>("originalQuantity"));

        priceInput = new TextField();
        priceInput.setPromptText("Price");
        priceInput.setMinWidth(100);

        quantityInput = new TextField();
        quantityInput.setPromptText("Quantity");
        quantityInput.setMinWidth(100);

        underlier = new Label(underlierProperty.getValue());
        underlier.setAlignment(Pos.CENTER_RIGHT);
        underlier.setMinWidth(40);
        underlier.textProperty().bind(underlierProperty);

        Label atLabel = new Label(" @ ");
        atLabel.setAlignment(Pos.CENTER);
        atLabel.setMinWidth(20);

        bestPrice = new Label();
        bestPrice.setAlignment(Pos.CENTER_LEFT);
        bestPrice.setMinWidth(40);
        bestPrice.textProperty().bind(bestPriceProperty.asString());

        HBox bestPriceBox = new HBox(underlier, atLabel, bestPrice);

        Button buyButton = new Button("Buy");
        buyButton.setOnAction(e -> buyButtonClicked());
        Button sellButton = new Button("Sell");
        sellButton.setOnAction(e -> sellButtonClicked());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> cancelButtonClicked());
        Button buyMarketButton = new Button("Buy Market");
        buyMarketButton.setOnAction(e -> buyMarketButtonClicked());
        Button sellMarketButton = new Button("Sell Market");
        sellMarketButton.setOnAction(e -> sellMarketButtonClicked());

        HBox topHeader = new HBox();
        topHeader.setPadding(new Insets(10, 10, 10, 10));
        topHeader.setSpacing(0);
        topHeader.getChildren().addAll(bestPriceBox);
        topHeader.setAlignment(Pos.CENTER);

        HBox bottomFooter = new HBox();
        bottomFooter.setPadding(new Insets(10, 10, 10, 10));
        bottomFooter.setSpacing(10);
        bottomFooter.getChildren().addAll(priceInput, quantityInput, buyButton, sellButton, cancelButton, buyMarketButton, sellMarketButton);

        bidsTable = new TableView<>();
        bidsTable.setItems(bidList);

        bidsTable.getColumns().add(ID_column);
        bidsTable.getColumns().add(PRICE_column);
        bidsTable.getColumns().add(QUANTITY_column);
        bidsTable.getColumns().add(ORIGINAL_QAUNTITY_column);

        bidsTable.getSortOrder().add(PRICE_column);

        completedOrdersTable = new TableView<>();
        completedOrdersTable.setItems(completedOrders);

        completedOrdersTable.getColumns().add(ID_column2);
        completedOrdersTable.getColumns().add(PRICE_column2);
        completedOrdersTable.getColumns().add(QUANTITY_column2);
        completedOrdersTable.getColumns().add(ORIGINAL_QAUNTITY_column2);

        asksTable = new TableView<>();
        asksTable.setItems(askList);

        asksTable.getColumns().add(ID_column3);
        asksTable.getColumns().add(PRICE_column3);
        asksTable.getColumns().add(QUANTITY_column3);
        asksTable.getColumns().add(ORIGINAL_QAUNTITY_column3);

        PRICE_column3.setSortType(TableColumn.SortType.DESCENDING);
        asksTable.getSortOrder().add(PRICE_column3);

        BorderPane layout = new BorderPane();
        layout.setTop(topHeader);
        layout.setLeft(bidsTable);
        layout.setCenter(completedOrdersTable);
        layout.setBottom(bottomFooter);
        layout.setRight(asksTable);

        // To make sure it doesn't show a large number for the price if the underlier has not traded yet
        updateHeader();

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    private void updateHeader() {
        underlierProperty.setValue(currentSymbol.underlier);

        if (currentSymbol.asks.bestPrice == Double.MAX_VALUE) {
            bestPriceProperty.setValue(0.0);
        } else {
            bestPriceProperty.setValue(currentSymbol.asks.bestPrice);
        }
    }

    private void updateTables() {
        List<Order> allLiveBids = currentSymbol.bids.ordersPerPrice.values()
            .stream()
            .flatMap(List::stream)
            .filter(x -> x.getQuantity() > 0)
            .collect(Collectors.toList());

        List<Order> allLiveAsks = currentSymbol.asks.ordersPerPrice.values()
            .stream()
            .flatMap(List::stream)
            .filter(x -> x.getQuantity() > 0)
            .collect(Collectors.toList());

        bidList.clear();
        askList.clear();
        completedOrders.clear();

        bidList.addAll(allLiveBids);
        askList.addAll(allLiveAsks);

        completedOrders.addAll(currentSymbol.bids.completedOrders);
        completedOrders.addAll(currentSymbol.asks.completedOrders);
    }

    private void updateFooter() {
        priceInput.clear();
        quantityInput.clear();
    }

    private void updateUI() {
        updateHeader();
        updateTables();
        updateFooter();
    }

    private void buyButtonClicked() {
        double price = Double.parseDouble(priceInput.getText());
        long quantity = Long.parseLong(quantityInput.getText());
        Order newOrder = new Order(price, quantity);
        currentSymbol.bids.processOrder(newOrder);
        updateUI();
    }

    private void sellButtonClicked() {
        double price = Double.parseDouble(priceInput.getText());
        long quantity = Long.parseLong(quantityInput.getText());
        Order newOrder = new Order(price, quantity);
        currentSymbol.asks.processOrder(newOrder);
        updateUI();
    }

    private void cancelButtonClicked() {
        ObservableList<Order> bidsSelected = bidsTable.getSelectionModel().getSelectedItems();
        ObservableList<Order> asksSelected = asksTable.getSelectionModel().getSelectedItems();
        bidsSelected.forEach(x -> currentSymbol.bids.removeOrder(new Order(x.getId(), x.getPrice(), x.getQuantity(), x.getOriginalQuantity()), x.getPrice()));
        asksSelected.forEach(x -> currentSymbol.asks.removeOrder(new Order(x.getId(), x.getPrice(), x.getQuantity(), x.getOriginalQuantity()), x.getPrice()));
        updateUI();
    }

    private void buyMarketButtonClicked() {
        double price = currentSymbol.asks.bestPrice;
        long quantity = Long.parseLong(quantityInput.getText());
        Order newOrder = new Order(price, quantity);
        boolean fullyMatched = currentSymbol.asks.matchOrder(newOrder);

        if (!fullyMatched) currentSymbol.bids.processOrder(newOrder);

        updateUI();
    }

    private void sellMarketButtonClicked() {
        double price = currentSymbol.bids.bestPrice;
        long quantity = Long.parseLong(quantityInput.getText());
        Order newOrder = new Order(price, quantity);
        boolean fullyMatched = currentSymbol.bids.matchOrder(newOrder);

        if (!fullyMatched) currentSymbol.asks.processOrder(newOrder);

        updateUI();
    }

    public static void main(String[] args) {
        launch();
    }

}
