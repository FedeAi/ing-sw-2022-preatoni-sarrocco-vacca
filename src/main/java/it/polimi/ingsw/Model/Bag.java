package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.Color;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
   private ArrayList<Student> students;

   public Bag(int numStudentEachColor){
      students = new ArrayList<>();
      for(int i = 0; i < numStudentEachColor * Color.values().length; i++){
         students.add(new Student(Color.values()[i % Color.values().length]));
      }
   }

   public ArrayList<Student> getStudents() {
      return students;
   }

   public ArrayList<Student> extract( int numStudent){
      ArrayList<Student> out = new ArrayList<Student>();

      Random rand = new Random();
      for (int i = 0; i < numStudent; i++) {
         int randomIndex = rand.nextInt(students.size());
         Student randomElement = students.get(randomIndex);
         students.remove(randomIndex);
         out.add(randomElement);
      }
      return out;
   }
}

