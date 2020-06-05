package com.snow.stonehedge.ui;

import com.snow.stonehedge.enigma.Asks;
import com.snow.stonehedge.enigma.Bids;
import com.snow.stonehedge.enigma.Book;
import com.snow.stonehedge.enigma.Order;
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

    Book bids = new Bids();
    Book asks = new Asks();
    ObservableList<Order> bidList = FXCollections.observableArrayList();
    ObservableList<Order> askList = FXCollections.observableArrayList();
    Stage window;
    TableView<Order> bidsTable;
    TableView<Order> asksTable;
    TableView<Order> completedOrdersTable;
    Label bestPrice, amountAvailable;
    TextField priceInput, quantityInput;

    DoubleProperty bestPriceProperty = new SimpleDoubleProperty(bids.bestPrice);
    DoubleProperty amountAvailableProperty = new SimpleDoubleProperty(bids.amountAvailable);

    ObservableList<Order> completedOrders = FXCollections.observableArrayList(bids.completedOrders);

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

        Label bestPriceLabel = new Label("Best Price: ");
        Label amountAvailableLabel = new Label("Amount Available: ");

        bestPrice = new Label("Best Price: " + bestPriceProperty.getValue());
        bestPrice.setMinWidth(100);
        bestPrice.textProperty().bind(bestPriceProperty.asString());

        amountAvailable = new Label();
        amountAvailable.setMinWidth(100);
        amountAvailable.textProperty().bind(amountAvailableProperty.asString());

        HBox bestPriceBox = new HBox(bestPriceLabel, bestPrice);
        HBox amountAvailableBox = new HBox(amountAvailableLabel, amountAvailable);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonClicked());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> cancelButtonClicked());
        Button matchOrderButton = new Button("Match Order");
        matchOrderButton.setOnAction(e -> matchOrderButtonClicked());

        HBox topHeader = new HBox();
        topHeader.setPadding(new Insets(10, 10, 10, 10));
        topHeader.setSpacing(0);
        topHeader.getChildren().addAll(bestPriceBox, amountAvailableBox);
        topHeader.setAlignment(Pos.CENTER);

        HBox bottomFooter = new HBox();
        bottomFooter.setPadding(new Insets(10, 10, 10, 10));
        bottomFooter.setSpacing(10);
        bottomFooter.getChildren().addAll(priceInput, quantityInput, addButton, cancelButton, matchOrderButton);

        bidsTable = new TableView<>();
        bidsTable.setItems(bidList);

        bidsTable.getColumns().add(ID_column);
        bidsTable.getColumns().add(PRICE_column);
        bidsTable.getColumns().add(QUANTITY_column);
        bidsTable.getColumns().add(ORIGINAL_QAUNTITY_column);

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

        BorderPane layout = new BorderPane();
        layout.setTop(topHeader);
        layout.setLeft(bidsTable);
        layout.setCenter(completedOrdersTable);
        layout.setBottom(bottomFooter);
        layout.setRight(asksTable);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    private void updateHeader() {
        bestPriceProperty.setValue(bids.bestPrice);
        amountAvailableProperty.setValue(bids.amountAvailable);
    }

    private void updateTables() {
        List<Order> allLiveOrdersOnBook = bids.ordersPerPrice.values()
            .stream()
            .flatMap(List::stream)
            .filter(x -> x.getQuantity() > 0)
            .collect(Collectors.toList());

        bidList.clear();
        completedOrders.clear();

        bidList.addAll(allLiveOrdersOnBook);
        completedOrders.addAll(bids.completedOrders);
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

    // Order order1 = new Order(11, 50);
    // Order order2 = new Order(13, 100);
    // Order order3 = new Order(12, 300);

    // bids.processOrder(order1);
    // bids.processOrder(order2);
    // bids.processOrder(order3);

    // bids.matchOrder(newOrder);
    private void addButtonClicked() {
        double price = Double.parseDouble(priceInput.getText());
        long quantity = Long.parseLong(quantityInput.getText());
        Order newOrder = new Order(price, quantity);

        bids.processOrder(newOrder);

        updateUI();
    }

    private void cancelButtonClicked() {
        ObservableList<Order> productsSelected;
        productsSelected = bidsTable.getSelectionModel().getSelectedItems();
        productsSelected.forEach(x -> bids.removeOrder(new Order(x.getId(), x.getPrice(), x.getQuantity(), x.getOriginalQuantity()), x.getPrice()));
        updateUI();
    }

    private void matchOrderButtonClicked() {
        double price = Double.parseDouble(priceInput.getText());
        long quantity = Long.parseLong(quantityInput.getText());
        Order newOrder = new Order(price, quantity);
        bids.matchOrder(newOrder);

        bids.processOrder(newOrder);

        updateUI();
    }

    public static void main(String[] args) {
        launch();
    }

}
