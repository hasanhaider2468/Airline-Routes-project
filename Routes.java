import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

class City {
                    // We can use this class to define each city as a node in the Graph
                    // Each city will carry its information regarding destination cost and time
    String destination;
    int cost;
    int time;

    public City(String destination, int cost, int time) {
        this.destination = destination;
        this.cost = cost;
        this.time = time;
    }
}

public class Graph {
    private LinkedList<City>[] adjList;          //adjacency list of cities containing the route information
    private List<String> cityNames;

    public Graph(int cities) {
        adjList = new LinkedList[cities];
        for (int i = 0; i < cities; i++) {
            adjList[i] = new LinkedList<>();     // linked lists of linked lists to create our adjancency list
        }
        cityNames = new LinkedList<>();          // we can store our cities to ensure we don't make two of the same cities
    }

    public void addRoute(String origin, String destination, int cost, int time) { //adds an edge to graph//
                                                //adds city information for each city in the adj. list
                                                // utilizes getCityIndex to get the index (method created below)
                                                
        int sourceIndex = getCityIndex(origin); 
        adjList[sourceIndex].add(new City(destination, cost, time));
    }
    
    public int getCityIndex(String cityName) { 
                                                // we can get the index of each city from the citynames linked list to match our adjacency list
                                                // also ensures no repeats of cities with "contains" method
        if (!cityNames.contains(cityName)) {
            cityNames.add(cityName);
        }
        return cityNames.indexOf(cityName);
    }
    
    
    public List<List<City>> findAllRoutesByCost(String origin, String destination) {
        int originIndex = getCityIndex(origin);
        List<List<City>> allRoutes = new LinkedList<>();           // arrayList could potentially work as well, I just preferred this way
        Stack<LinkedList<City>> stack = new Stack<>();
    
        LinkedList<City> initialPath = new LinkedList<>();
        initialPath.add(new City(origin, 0, 0));                   //adding origin city of the route
        stack.push(initialPath);
                                                                 // Will utilize DFS structure to find all paths:
                                                                 
        while (!stack.isEmpty()) {                               //while stack is not empty, it should keep iterating
            LinkedList<City> currentPath = stack.pop();          // will also allow us to iteratively back track as we go down all the nodes
            City currentCity = currentPath.getLast();            // and as we explore all the chidren of the nodes. .getLast() will let us backtrack
                                                                 // as we explore the children
    
            if (currentCity.destination.equals(destination)) {  // this should stop cycles from occuring and will have the proper path
                allRoutes.add(currentPath);                     // we know cuurentCity is the last city in the current path. 
            } else {
                for (City neighbor : adjList[getCityIndex(currentCity.destination)]) {
                    
                                                                // Check if the neighbor city is already in the current path (T or F)
                    boolean alreadyVisited = false;
                    
                    for (City city : currentPath) {
                        if (city.destination.equals(neighbor.destination)) { //checks the children of each node(city) of the adjacency list and sees if they match
                            alreadyVisited = true;                          //breaks the for loop if there is a match
                            break;
                        }
                    }
                    
                    if (!alreadyVisited) {
                        LinkedList<City> newPath = new LinkedList<>(currentPath);
                        newPath.add(neighbor);                  // adds just the neighbor and that current path so that we can continue to look at all the neighbors of the node(city)
                        stack.push(newPath);                    //new path in the stack
                                                                //the .getLast() will allow us to constantly know where we are in the stack 
                                                                //the boolean already visited will allow the 
                        //while we iteratively backtrack
                        // T
                    }
                }
            }
        }
    
        for (int i = 0; i < allRoutes.size() - 1; i++) {
            for (int j = 0; j < allRoutes.size() - i - 1; j++) {
                int CityOne_Cost = 0;
                for (City city : allRoutes.get(j)) {             //for each city at the index get the total cost
                    CityOne_Cost += city.cost;
                }

                int CityTwo_Cost = 0;
                for (City city : allRoutes.get(j + 1)) {
                    CityTwo_Cost += city.cost;
                }
                                                                //we are getting the total cost of two seperate routes above
                                                                //below we can use Collections.swap in order to compare the two values 
                                                                //and swap the routes based on position if one is greater than the other
                if (CityOne_Cost > CityTwo_Cost) {
                    Collections.swap(allRoutes, j, j + 1);
                }
            }
        }
        List<List<City>> TopThreeRoutes = new LinkedList<>();
        for (List<City> route : allRoutes) {
            TopThreeRoutes.add(route);
            if(TopThreeRoutes.size() == 3){                     //Ensures we don't output over 3 routes
                break;
            }
        }
        return TopThreeRoutes;
    }
    
    public List<List<City>> findAllRoutesByTime(String origin, String destination) { //no comments on this method, same as previous method but sorted by time
        int originIndex = getCityIndex(origin);
        List<List<City>> allRoutes = new LinkedList<>();
        Stack<LinkedList<City>> stack = new Stack<>();
    
        LinkedList<City> initialPath = new LinkedList<>();
        initialPath.add(new City(origin, 0, 0)); 
        stack.push(initialPath);
    
        while (!stack.isEmpty()) { 
            LinkedList<City> currentPath = stack.pop();
            City currentCity = currentPath.getLast();
    
            if (currentCity.destination.equals(destination)) {
                allRoutes.add(currentPath);
            } else {
                for (City neighbor : adjList[getCityIndex(currentCity.destination)]) {
                    boolean alreadyVisited = false;
                    for (City city : currentPath) {
                        if (city.destination.equals(neighbor.destination)) {
                            alreadyVisited = true;
                            break;
                        }
                    }
    
                    if (!alreadyVisited) {
                        LinkedList<City> newPath = new LinkedList<>(currentPath);
                        newPath.add(neighbor);
                        stack.push(newPath);
                    }
                }
            }
        }
    
        for (int i = 0; i < allRoutes.size() - 1; i++) {
            for (int j = 0; j < allRoutes.size() - i - 1; j++) {
                int CityOne_Time = 0;
                for (City city : allRoutes.get(j)) { 
                    CityOne_Time += city.time;
                }

                int CityTwo_Time = 0;
                for (City city : allRoutes.get(j + 1)) {
                    CityTwo_Time += city.time;
                }

                if (CityOne_Time > CityTwo_Time) {
                    Collections.swap(allRoutes, j, j + 1);
                }
            }
        }
        List<List<City>> TopThreeRoutes = new LinkedList<>();
        for (List<City> route : allRoutes) {
            TopThreeRoutes.add(route);
            if(TopThreeRoutes.size() == 3){
                break;
            }
        }
        return TopThreeRoutes;
    }
    
    
    public static void main(String[] args) {
        String fileName = args[0];
        String input = args[1];
        String outputFileName = args[2];
        try (PrintStream writer = new PrintStream(new FileOutputStream(new File(outputFileName)))) {
            System.setOut(writer);
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                // Read the number of cities from the first line
                int numberOfCities = Integer.parseInt(br.readLine());
    
                // Create a map with the number of given cities
                Graph map = new Graph(numberOfCities);
                
                for (int i = 0; i < numberOfCities; i++) {
                    String[] data = br.readLine().split("\\|"); //regex
                    String origin = data[0];
                    String destination = data[1];
                    int cost = Integer.parseInt(data[2]);
                    int time = Integer.parseInt(data[3]);
    
                    map.addRoute(origin, destination, cost, time);
                    map.addRoute(destination, origin, cost, time); //undirected
                }
                try (BufferedReader inputReader = new BufferedReader(new FileReader(input))) {
                                                                                    // Read the number of cities from the first line
                    int iterations = Integer.parseInt(inputReader.readLine());      //how many inputs the user will have
                    for (int j = 0; j < iterations; j++) {                          //depends on user input (first line)
                        String[] data = inputReader.readLine().split("\\|");        //regex
                        String origin = data[0];
                        String destination = data[1];
                        String request = data[2];
                        
                        String user_req;                                             //this is just for print statements later on
                        if(request.equals("T")){
                            user_req = "Time";
                        } else{
                            user_req = "Cost";
                        }
                        
                        System.out.println("Flight " + (j + 1) + ": " + origin + ", " + destination + " (" + user_req + ")");
                        if(request.equals("T")){
                            List<List<City>> allRoutes = map.findAllRoutesByTime(origin, destination);
                            for (List<City> route : allRoutes) {
                                int totalCost = 0;
                                int totalTime = 0;
                                
                                                                                    // Calculate total cost and total time using loops
                                for (int i = 0; i < route.size(); i++) {
                                    totalCost += route.get(i).cost;                 //add up all the costs in the route
                                    totalTime += route.get(i).time;                 // add up all the time in the route
                            
                                    // Print city destination
                                    if (i > 0) {
                                        System.out.print(" -> ");                   //print an array every time besides the origin city
                                    }
                                    System.out.print(route.get(i).destination);
                                }
                                System.out.println(", Time: " + totalTime + " Cost: " + totalCost);
                            }
                            if(allRoutes.size() == 0) //if we have no routes availible
                            {
                                System.out.println("Sorry no routes exist between these cities"); 
                            }
                        } else if(request.equals("C")){
                            List<List<City>> allRoutes = map.findAllRoutesByCost(origin, destination);
                            for (List<City> route : allRoutes) {
                                int totalCost = 0;
                                int totalTime = 0;
                                
                                for (int i = 0; i < route.size(); i++) {
                                    totalCost += route.get(i).cost;
                                    totalTime += route.get(i).time;
                            
                                    // Print city destination
                                    if (i > 0) {
                                        System.out.print(" -> ");
                                    }
                                    System.out.print(route.get(i).destination);
    
                                }
                            System.out.println(", Time: " + totalTime + " Cost: " + totalCost);
    
                            }
                            if(allRoutes.size() == 0)
                            {
                                System.out.println("Sorry no routes exist between these cities"); //if we have no routes availible
                            }
                        } else {
                            System.out.println("Improper request of Time or Cost sorting");
                        }
                        System.out.println("");
                    }
                
                } catch (IOException e) {
                System.err.println("Error reading the input file: " + e.getMessage()); //try catch statement 
                }
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage()); //try catch statement
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}