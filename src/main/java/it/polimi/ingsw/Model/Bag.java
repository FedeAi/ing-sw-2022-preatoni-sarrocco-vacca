package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
   private ArrayList<Color> students;

   public Bag(int numStudentEachColor){
      students = new ArrayList<>();
      for(int i = 0; i < numStudentEachColor * Color.values().length; i++){
         students.add(Color.values()[i % Color.values().length]);
      }
   }

   public ArrayList<Color> getStudents() {
      return students;
   }

   public ArrayList<Color> extract( int numStudent){
      ArrayList<Color> out = new ArrayList<Color>();

      Random rand = new Random();
      for (int i = 0; i < numStudent; i++) {
         int randomIndex = rand.nextInt(students.size());
         Color randomElement = students.get(randomIndex);
         students.remove(randomIndex);
         out.add(randomElement);
      }
      return out;
   }
}

