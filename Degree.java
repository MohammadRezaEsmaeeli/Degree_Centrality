import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Collections;
import java.util.Comparator;

public class Degree {

    static int NUMBER_OF_MOST_CENTRAL_NODES = 10;

    public static HashMap<Integer,Integer> degrees;

    public static void readLine_saveGraph() {
        degrees = new HashMap<Integer,Integer>();
        Path path = Paths.get("web-graph.txt");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(s -> {
                String[] temp = s.split(" ");
                int src = Integer.parseInt(temp[0]);
                int des = Integer.parseInt(temp[1]);
                int currentDegreeSrc = degrees.containsKey(src)? degrees.get(src): 0;
                degrees.put(src,currentDegreeSrc + 1);
                if (!degrees.containsKey(des)) {
                    degrees.put(des, 0);
                }
            });
        }
        catch (IOException ex) {
            System.out.println();
        }catch (OutOfMemoryError e){
            System.err.println("Max JVM memory: " + Runtime.getRuntime().maxMemory());
        }
    }

    public static HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm) {
        List<Map.Entry<Integer, Double> > list =
            new LinkedList<Map.Entry<Integer, Double> >(hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double> >() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static HashMap<Integer,Double> calculateMostCentralNodes(double avgDegreeOfNodes) {
        HashMap<Integer, Double> results = new HashMap<Integer, Double>();
        degrees.forEach((key, value) -> {
            results.put(key, value / avgDegreeOfNodes);
        });
        return results;
    }

    public static void printResult(HashMap<Integer, Double> nodes) {
        System.out.println("The most central Nodes: ");
        int index = 0;
        for(Map.Entry<Integer, Double> entry : nodes.entrySet()) {
            int key = entry.getKey();
            double value = entry.getValue();
            System.out.println("Node " + key + " => Avg degree: " + value);
            index++;
            if (index >= NUMBER_OF_MOST_CENTRAL_NODES) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        long step1 = System.currentTimeMillis();
        readLine_saveGraph();
        long step2 = System.currentTimeMillis();
        System.out.println("Read web-graph.txt and save network: " + ((step2 - step1) / 1000.0) + " s");
        long sumDegrees = 0;
        for (int d : degrees.values()) {
            sumDegrees += d;
        }
        double avgDegreeOfNodes = sumDegrees / (degrees.size() * 1.0);
        System.out.println("Average degree of nodes: " + avgDegreeOfNodes);
        HashMap<Integer, Double> nodes = calculateMostCentralNodes(avgDegreeOfNodes);
        nodes = sortByValue(nodes);
        printResult(nodes);
        long step3 = System.currentTimeMillis();
        System.out.println("Implement algorithm: " + ((step3 - step2) / 1000.0) + " s");
    }

}
