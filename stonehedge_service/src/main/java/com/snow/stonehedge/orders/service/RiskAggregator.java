package com.snow.stonehedge.orders.service;

import com.snow.stonehedge.orders.model.BuyOrSell;
import com.snow.stonehedge.orders.model.CallOrPut;
import com.snow.stonehedge.orders.model.Position;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RiskAggregator {

    public static void main(String[] args) {
//        List<Position> positions = new ArrayList<>();
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 433.4, 46147,1.0));

//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 1449.24, 8280,1.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 1449.24, 8280,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 1449.24, 8280,1.0));

//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 1651.9, 30268,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 1932.32, 16560,1.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 1932.32, 16560,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 1932.32, 16560,1.0));
//
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 2049.0, 7978,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 2090.75, 2391,1.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 2090.75, 2391,1.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2090.75, 2391,1.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2090.75, 2391,1.0));
//
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.CALL, 2147.47, 30268,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2290.0, 7978,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2415.40, 8280,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 2415.40, 8280,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 2415.40, 8280,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2531.95, 7978,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 2689.34, 5,100.0));
//
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.CALL, 2726.0, 3668,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 2726.0, 3668,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 2802.39, 943,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.CALL, 2802.39, 5,100.0));
//
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 2829.82, 7978,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 2830.0, 150,100.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2830.0, 150,100.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 2870.0, 23,100.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 2870.0, 23,100.0));
//
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.CALL, 2870.75, 2299,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 2870.75, 2299,1.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.CALL, 3000.0, 50,100.0));
//
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 3130.0, 15000,1.0));
//        positions.add(new Position(BuyOrSell.BUY, CallOrPut.PUT, 3130.0, 15000,1.0));
//        positions.add(new Position(BuyOrSell.SELL, CallOrPut.PUT, 3130.0, 15000,1.0));

        RiskAggregator riskAggregator = new RiskAggregator();
        List<Position> positions = riskAggregator.readInFile();
        for (double spxPrice = 2000.0; spxPrice < 4000.0; spxPrice = spxPrice + 1.0) {
            System.out.println(spxPrice + " -> " + riskAggregator.aggregatePnL(spxPrice, LocalDate.of(2021, 4, 6), positions));
        }
    }

    private List<Position> readInFile() {
        try {
            List<Position> positions = new ArrayList<>();
            File file = new File("testFile.txt");
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                String lineWithoutCommas = line.replaceAll(",", "");
                String[] words = lineWithoutCommas.split("\\s+");
                Position position = new Position(
                    getBuyOrSell(words[0]),
                    getCallOrPut(words[4]),
                    Double.parseDouble(words[2]),
                    getSize(words[0]),
                    Double.parseDouble(words[1]),
                    getExpiryDate(words[3])
                );
                positions.add(position);
            }
            input.close();
            return positions;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private LocalDate getExpiryDate(String input) {
        String[] parts = input.split("/");
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
    }

    private double getSize(String input) {
        if (input.contains("(") | input.contains(")")) {
            String withoutLeftBracket =  input.replace("(", "");
            String withoutRightBracket = withoutLeftBracket.replace(")", "");
            return Double.parseDouble(withoutRightBracket);
        }
        return Double.parseDouble(input);
    }

    private BuyOrSell getBuyOrSell(String input) {
        return (input.contains("(") | input.contains(")")) ? BuyOrSell.SELL : BuyOrSell.BUY;
    }

    private CallOrPut getCallOrPut(String input) throws Exception {
        if (!input.equalsIgnoreCase("call") & !input.equalsIgnoreCase("put")) throw new Exception("Not a valid put or call value");
        return (input.equalsIgnoreCase("call")) ? CallOrPut.CALL : CallOrPut.PUT;
    }

    private String aggregatePnL(double currentStockPrice, LocalDate afterThisExpiryDate,  List<Position> positions) {
        double pnl = positions.parallelStream()
            .filter(x -> x.getExpiryDate().isAfter(afterThisExpiryDate))
            .map(x -> x.getPnL(currentStockPrice))
            .reduce(0.0, Double::sum);
        return String.format("%.2fM", pnl / 1000000.0);
    }

}
