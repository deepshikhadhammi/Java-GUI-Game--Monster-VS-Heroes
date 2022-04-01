
/* CPSC233W22A3
   NAME: DEEPSHIKHA DHAMMI
   DATE: APRIL 1, 2022
   TUTORIAL: T08
   TA: KIMIYA SADAT
 */
package mvh.util;

import mvh.enums.WeaponType;
import mvh.world.Hero;
import mvh.world.Monster;
import mvh.world.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class to assist reading in world file
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class Reader {

    /**
     * Load the world from the given file
     * (Do not expect students to create anything near as robust as this file reading method!)
     * (A better design would also use sub-functions.)
     *
     * @param fileWorld The world file to load
     * @return A World created from the world file
     */
    public static World loadWorld(File fileWorld) {
        int row = 0, column = 0;
        String line = "";
        try {
            FileReader file_reader = new FileReader(fileWorld); //read a file
            BufferedReader buffered_reader = new BufferedReader(file_reader);

            for (int i = 0; i < 2; i++) {  //read the row and column value from first two lines
                line = buffered_reader.readLine();
                if (i == 0) {
                    row = Integer.parseInt(line);   //store the value of row
                } else {
                    column = Integer.parseInt(line);//store the value of column
                }
            }
            World world1 = new World(row, column);   //create a new world

            for (int p = 0,q=3; p < (row * column); p++,q++) {   //loop for(row*column lines)
                line = buffered_reader.readLine();
                String[] array;
                array = line.split(",");  //split the elements at , and store in an array
                if (array.length > 2 && (array[2].equals("MONSTER"))) { //check if the information is about monster
                    int r = Integer.parseInt(array[0]);  //store row of monster
                    int c = Integer.parseInt(array[1]);  //store column of monster
                    int health = Integer.parseInt(array[4]); //store health of monster
                    char symbol = array[3].charAt(0);  //store symbol of monster
                    char weapon = array[5].charAt(0);  //store weapon of  monster
                    Monster monster = new Monster(health, symbol, WeaponType.getWeaponType(weapon));//create a monster object
                    world1.addEntity(r, c, monster);  //add monster to world1
                } else if (array.length > 2 && array[2].equals("HERO")) {//check if information is about hero
                    int r = Integer.parseInt(array[0]);  //store row
                    int c = Integer.parseInt(array[1]); //store column
                    int health = Integer.parseInt(array[4]);  //store health
                    char symbol = array[3].charAt(0); //store symbol
                    int weapon_strength = Integer.parseInt(array[5]); //stores weapon strength
                    int arm_strength = Integer.parseInt(array[6]); //stores armour strength
                    Hero hero = new Hero(health, symbol, weapon_strength, arm_strength); //creates a hero object
                    world1.addEntity(r, c, hero); //add hero to world
                }
            }
            return world1; //return world1

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
