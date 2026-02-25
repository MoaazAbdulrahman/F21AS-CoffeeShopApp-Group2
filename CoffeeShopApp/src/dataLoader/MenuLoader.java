package dataLoader;

import core.MenuItem;
import exceptions.InvalidMenuItemException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MenuLoader {

    public Map<String, MenuItem> readMenu(String filePath) throws IOException{
        Map<String, MenuItem> menu = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null){
                lineNumber++;
                if (lineNumber == 1) continue; // skip header row
                if (line.trim().isEmpty()) continue; //skip empty row
                try{
                    MenuItem item = parseMenuItem(line, lineNumber);
                    menu.put(item.getId(), item);

                }
                catch (InvalidMenuItemException e){
                    System.out.println("Invalid menu item : " + e.getMessage());
                }
            }
        }
        System.out.println("============" + " Menu items Loaded = " + menu.size() + "============");
        return menu;
    }

    private MenuItem parseMenuItem(String line, int lineNumber) throws InvalidMenuItemException {
        String[] elements = line.split(",", 4);
        if(elements.length < 4){
            throw new InvalidMenuItemException("Row :"+ lineNumber + "missing values : " + line);

        }

        String id = elements[0].trim();
        String description = elements[1].trim();
        String cost = elements[2].trim();
        String category = elements[3].trim();

        // validate cost is double
        double costDouble;
        try{
            costDouble = Double.parseDouble(cost);
        } catch (NumberFormatException e){
            throw new InvalidMenuItemException( "Line :" + lineNumber + "Contains invalid cost number : " + line);
        }
        return new MenuItem(id, description, costDouble, category);

    }
}

