package com.snow.stonehedge.orders.service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class InterviewTest {

    public static void main(String[] args) throws Exception {
        String dataset = transformFileIntoString("dataset");
        System.out.println(dataset.length());
        Set<String> map = findClumps(dataset, 9, 500, 3);
        map.forEach((key) -> System.out.print(key + " "));
        System.out.println(map.size());
    }

    public static Set<String> findClumps(String data, int wordLength, int totalLength,  int howMany) {
        // CGGACTCGACAGATGTGAAGAACGACAATGTGAAGACTCGACACGACAGAGTGAAGAGAAGAGGAAACATTGTAA
        // 5 50 4
        Set<String> yep = new HashSet<>();
        for (int i = 0; i < data.length() - totalLength; i++) {
            Map<String, Integer> map2 = computeOccurrences(wordLength, data.substring(i, i + totalLength));
            Map<String, Integer> output = map2.entrySet()
                .stream()
                .filter(input -> input.getValue() == howMany)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            output.forEach((x, y) -> yep.add(x));
        }
        return yep;
    }

    public static List<Integer> findPositionsOfString(String toFind, String data) {
        int length = toFind.length();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < data.length() - length; i++) {
            if (data.substring(i, i + length).equalsIgnoreCase(toFind)) list.add(i);
        }
        return list;
    }

    public static void computeMostOccurrences(int from, int to) throws Exception {
        for (int i = from; i < to; i++) {
            System.out.println("--- " + i + " ---");
            System.out.println(getKeysWithMaxValue(computeOccurrences(i, transformFileIntoString("dataset"))));
        }
    }

    public static String computeComplement(String data) {
        // A -> T
        // C -> G
        StringBuilder output= new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char nucleotide = data.charAt(i);
            if (nucleotide == 'A') output.insert(0,'T');
            if (nucleotide == 'T') output.insert(0,'A');
            if (nucleotide == 'G') output.insert(0,'C');
            if (nucleotide == 'C') output.insert(0,'G');
        }
        return output.toString();
    }

    public static int computeOccurrences(String data, String toFind) {
        int length = toFind.length();
        int counter = 0;
        for (int i = 0; i < data.length() - length; i++) {
            if (data.substring(i, i + length).equalsIgnoreCase(toFind)) counter++;
        }
        return counter;
    }

    public static String transformFileIntoString(String fileName) throws Exception {
        File myObj = new File(fileName + ".txt");
        Scanner myReader = new Scanner(myObj);
        String data = "";
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
        }
        myReader.close();
        return data;
    }

    public static Map<String, Integer> computeOccurrences(int wordLength, String data) {
        Map<String, Integer> map = new HashMap<>();
        int sequenceLength = data.length();
        for (int i = 0; i < sequenceLength - wordLength; i++) {
            String word = data.substring(i, i + wordLength);
            if (!map.containsKey(word)) {
                map.put(word, 1);
            } else {
                map.merge(word, 1, Integer::sum);
            }
        }
        return map;
    }

    public static List<String> getKeysWithMaxValue(Map<String, Integer> map){
        final List<String> resultList = new ArrayList<>();
        int currentMaxValue = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : map.entrySet()){
            if (entry.getValue() > currentMaxValue){
                resultList.clear();
                resultList.add(entry.getKey() + " -> " + entry.getValue());
                currentMaxValue = entry.getValue();
            } else if (entry.getValue() == currentMaxValue){
                resultList.add(entry.getKey() + " -> " + entry.getValue());
            }
        }
        return resultList;
    }

}
