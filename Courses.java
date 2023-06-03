package registrar;

//Minsoo Ku, mku1, 118994239, 0104
//I pledge on my honor that I have not given or received any unauthorized 
//assistance on this assignment.

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// This class contains data for different courses and methods to help the 
// main registrar class find certain course or certain students. This class 
// also contains students set in which it will allow each course to store 
// set of students taking each course. 
public class Courses {

   // This map will contain course number and total number of seats.
   private Map<Integer, Integer> courseInfo = new HashMap<>();
   // This map will contain course number and number of students currently
   // enrolled in a course with course number.
   private Map<Integer, Integer> currentSeats = new HashMap<>();
   // This set contains students who are currently taking this course object. 
   private Set<Students> studentSet = new HashSet<>();
   // Will save the unique number for the course. 
   private int number;
   
   // A constructor with parameter number and numSeats that will save each 
   // into a proper field maps. 
   public Courses(int number, int numSeats) {
      this.number = number;
      courseInfo.put(number ,numSeats);
      // The value will be 0 since no student is enrolled in the course yet. 
      currentSeats.put(number ,0);
   }
 
   // Returns true if the course exist in the map, false otherwise. 
   public boolean containsCourse(int number) {
      // If number exists in both maps, it means the course exists, so return
      // true.
      if (courseInfo.containsKey(number) && 
            currentSeats.containsKey(number)) {
         return true;
      }
      
      return false;
   }
   
   // Removes course with number from the system.
   public void removeCourse(int number) {
      if (courseInfo.containsKey(number)
            && currentSeats.containsKey(number)) {
         courseInfo.remove(number);
         currentSeats.remove(number);
      }
   }
   
   // This method will add students in the student set. 
   public void addStudent(String firstName, String lastName) {
      if (courseInfo.containsKey(number) 
            && currentSeats.containsKey(number)) {
         // If the course exists, add student to the set and increase the 
         // current number of students taking this course by 1. 
         studentSet.add(new Students(firstName, lastName));
         currentSeats.put(number, currentSeats.get(number) + 1);
      }
   }
   
   // This method will return the total number of courses currently exist in
   // this course with department. 
   public int totalCourses() {
      return courseInfo.size();
   }
   
   // This method will find student with parameters and return true if it was
   // successful, false otherwise. 
   public boolean findStudentWithFull(String firstName, String lastName) {
      Iterator<Students> iter = studentSet.iterator();
      
      while(iter.hasNext()) { // Iterate through the set of students.
         // When we find the student with parameters first name and last name
         // from the students set, return true.
         if(iter.next().firstLastEquals(firstName, lastName)) {
            return true;
         }
      }
      
      return false;
   }
   
   // This method will return the total number of student with last name from 
   // the parameter string. 
   public int findStudentWithLast(String lastName) {
      // Iterator to loop through the set of students.
      Iterator<Students> iter = studentSet.iterator();
      int total = 0;
      
      while(iter.hasNext()) { // Iterate through the set of students.
         // If you found a student with a last name parameter from the set,
         // add 1 to the return Integer. 
         if(iter.next().lastEquals(lastName)) {
          total++;  
         }
      }
      
      return total;
   }
   
   // This method will remove student with parameters name from the set and 
   // decrease the current number of students taking certain course with
   // number. 
   public boolean removeStudent(String firstName, String lastName) {
      // Iterator to loop through the set of students.
      Iterator<Students> iter = studentSet.iterator();
      
      while(iter.hasNext()) { // Iterate through the set of students.
         Students s = iter.next();
         // When we find student with parameters first name and last name, 
         // remove that student from the student set and decrease the current
         // seat by 1, and return true.
         if(s.firstLastEquals(firstName, lastName)) {
            studentSet.remove(s);  
            currentSeats.put(number, currentSeats.get(number) - 1);
            return true;
         }
      }
      
      return false;
   }
   
   // This method will return the total number of students currently enrolled
   // in the course. 
   public int totalStudents(int number) {
      // Return -1 if the course does not exist. 
      if (!courseInfo.containsKey(number)) { 
         return -1;
      }
      
      return currentSeats.get(number);
   }
   
   // This method will return true if the course is full and can't accept 
   // anymore students, and false if the course is still available. 
   public boolean isFull(int number) {
      // If the current Seats and max number of seats of course is the same,
      // it means the course is full, so the method should return true.
      if (courseInfo.get(number) == currentSeats.get(number)) {
         return true;
      }
      
      // When the course is not full, so return false. 
      return false;
   }
   
}
