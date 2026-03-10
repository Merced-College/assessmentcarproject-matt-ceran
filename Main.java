import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Main.java
// Matt Ceran
// 3/7/26
// Project A - Brand + Efficency Finder
// loads car data, sorts by brand, searches by brand, shows stats

public class Main {

    // loads cars from csv file into arraylist
    public static ArrayList<Car> loadCars(String filename){
        ArrayList<Car> cars = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            br.readLine(); // skip header

            while((line = br.readLine()) != null){
                try{
                    String[] parts = line.split(",");
                    if(parts.length != 7){
                        continue;
                    }

                    String carId = parts[0].trim();
                    String brand = parts[1].trim();
                    String model = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    String fuelType = parts[4].trim();
                    String color = parts[5].trim();
                    double mileage = Double.parseDouble(parts[6].trim());

                    cars.add(new Car(carId, brand, model, year, fuelType, color, mileage));

                } catch(NumberFormatException e){
                    // skip bad rows
                }
            }
        } catch(IOException e){
            System.out.println("Error reading file: " + e.getMessage());
        }

        System.out.println("Total cars loaded: " + cars.size());
        return cars;
    }


    // selection sort by brand alphabeticaly
    public static void selectionSortByBrand(ArrayList<Car> list){
        int n = list.size();

        for(int i = 0; i < n - 1; i++){
            int minIndex = i;

            for(int j = i + 1; j < n; j++){
                if(list.get(j).getBrand().compareToIgnoreCase(list.get(minIndex).getBrand()) < 0){
                    minIndex = j;
                }
            }

            // swap
            if(minIndex != i){
                Car temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }

        // print first 10 after sorting
        System.out.println("\n--- First 10 cars after sorting by Brand ---");
        for(int i = 0; i < 10 && i < list.size(); i++){
            System.out.println(list.get(i));
        }
        System.out.println();
    }


    // binary search for brand, returns index or -1
    public static int binarySearchByBrand(ArrayList<Car> list, String target){
        int low = 0;
        int high = list.size() - 1;

        while(low <= high){
            int mid = (low + high) / 2;
            int cmp = list.get(mid).getBrand().compareToIgnoreCase(target);

            if(cmp == 0){
                return mid;
            } else if(cmp < 0){
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }


    // finds all cars with the brand using binary search then expanding left and right
    public static void searchAndDisplayBrand(ArrayList<Car> working, String brand){
        int index = binarySearchByBrand(working, brand);

        if(index == -1){
            System.out.println("No cars found with brand: " + brand);
            return;
        }

        // go left to find first occurance
        int left = index;
        while(left > 0 && working.get(left - 1).getBrand().equalsIgnoreCase(brand)){
            left--;
        }

        // go right to find last occurance
        int right = index;
        while(right < working.size() - 1 && working.get(right + 1).getBrand().equalsIgnoreCase(brand)){
            right++;
        }

        ArrayList<Car> results = new ArrayList<>();
        for(int i = left; i <= right; i++){
            results.add(working.get(i));
        }

        System.out.println("\n--- Found " + results.size() + " car(s) with brand: " + brand + " ---");
        for(Car c : results){
            System.out.println(c);
        }
        System.out.println();
    }


    // prints avg mileage and fuel type counts
    public static void printStats(ArrayList<Car> working){
        if(working.isEmpty()){
            System.out.println("No cars in working list.");
            return;
        }

        double total = 0;
        for(Car c : working){
            total += c.getMileageKmpl();
        }
        double avg = total / working.size();

        System.out.println("\n--- Statistics ---");
        System.out.printf("Average Mileage: %.2f kmpl%n", avg);

        // count fuel types using parallel arraylists
        ArrayList<String> fuelTypes = new ArrayList<>();
        ArrayList<Integer> fuelCounts = new ArrayList<>();

        for(Car c : working){
            String fuel = c.getFuelType();
            int pos = -1;

            for(int i = 0; i < fuelTypes.size(); i++){
                if(fuelTypes.get(i).equalsIgnoreCase(fuel)){
                    pos = i;
                    break;
                }
            }

            if(pos == -1){
                fuelTypes.add(fuel);
                fuelCounts.add(1);
            } else{
                fuelCounts.set(pos, fuelCounts.get(pos) + 1);
            }
        }

        System.out.println("\nCounts by Fuel Type:");
        for(int i = 0; i < fuelTypes.size(); i++){
            System.out.println("  " + fuelTypes.get(i) + ": " + fuelCounts.get(i));
        }
        System.out.println();
    }


    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        // load all cars from file
        ArrayList<Car> cars = loadCars("Car_Data.csv");

        // working list of first 2000
        int limit = Math.min(2000, cars.size());
        ArrayList<Car> working = new ArrayList<>(cars.subList(0, limit));
        System.out.println("Working list size: " + working.size());

        boolean sorted = false;

        // menu loop
        boolean running = true;
        while(running){
            System.out.println("========================================");
            System.out.println("  Project A - Brand + Efficency Finder");
            System.out.println("========================================");
            System.out.println("1. Sort by Brand");
            System.out.println("2. Search by Brand");
            System.out.println("3. Show Stats");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch(choice){
                case "1":
                    System.out.println("Sorting by Brand...");
                    selectionSortByBrand(working);
                    sorted = true;
                    break;

                case "2":
                    if(!sorted){
                        System.out.println("List must be sorted first. Sorting now...");
                        selectionSortByBrand(working);
                        sorted = true;
                    }
                    System.out.print("Enter brand to search: ");
                    String brand = scanner.nextLine().trim();
                    searchAndDisplayBrand(working, brand);
                    break;

                case "3":
                    printStats(working);
                    break;

                case "4":
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
        scanner.close();
    }
}