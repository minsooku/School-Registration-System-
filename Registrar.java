package registrar;

//Minsoo Ku, mku1, 118994239, 0104
//I pledge on my honor that I have not given or received any unauthorized 
//assistance on this assignment.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// This class contains a course registration system, where one can add a 
// course to a registration system of a small institution, and add students
// to the course that one added. This contains multiple methods that allows to 
// track the number of students taking the courses or how many courses one 
// student is taking. It can also remove a course or student from the system.
// This class also extends Thread class to concurrently modify the 
// registration system. 
public class Registrar extends Thread {

   // Fields to store data. Used map to control the register system.
   private Map<String, Set<Courses>> courseRegistrar;
   private int maxCourses;

   // Static inner class that extends thread to run threads concurrently.
   class RegistrarWorker extends Thread {
      // String threadfile that receives doRegistrations' files. 
      private String threadFile;
      // Static object for synchronization. 
      private static Object o = new Object();

      // A constructor for Thread class. 
      public RegistrarWorker(String str) {
         this.threadFile = str;
      }

      @Override
      public void run() {
         // Synchronize objects.
         // Scanner to scan the file for thread. 
         Scanner scan;
         String str;

         try {
            scan = new Scanner(new File(threadFile));
            // Synchronization happens, puts lock for each object. 
            synchronized (o) {
               while (scan.hasNext()) {
                  str = scan.next();
                  // If the file has addcourse in the beginning.
                  if (str.equals("addcourse")) {
                     // Scan string, int, and int.
                     addNewCourse(scan.next(), scan.nextInt(), 
                           scan.nextInt());
                  } else if (str.equals("addregistration")) {
                  // If the file has addcourse in the beginning,
                     // scan string, int, String, and String.
                     addToCourse(scan.next(), scan.nextInt(), scan.next(), 
                           scan.next());
                  }
               }
            }
         } catch (FileNotFoundException e1) { // When the file is not found. 

         }
      }
   }

   // A constructor with a parameter int that gives maximum number of courses
   // for each student.
   public Registrar(int maxCoursesPerStudent) {
      if (maxCoursesPerStudent <= 0) {
         this.maxCourses = 1;
         courseRegistrar = new HashMap<>();
      }

      this.maxCourses = maxCoursesPerStudent;
      courseRegistrar = new HashMap<>();
   }
   
   // This method will add new course to the system, with all its information
   // parameters. 
   public Registrar addNewCourse(String department, int number, int numSeats) {
      if (department == null || department.isEmpty() 
            || number <= 0 || numSeats <= 0) {
         throw new IllegalArgumentException();
      }

      if (!courseRegistrar.containsKey(department)) {
         // If the system does not contain department, add department with
         // course information and return the current system. 
         courseRegistrar.put(department, new HashSet<>());
         courseRegistrar.get(department).add(new Courses(number, numSeats));
      } else { // If system already contains the parameter department
         for (Courses c : courseRegistrar.get(department)) { // Run for-each
            if (c.containsCourse(number)) { 
               // If the course already exists, do not modify anything. 
               return this;
            } else { // If the course does not exist, then add a new course.
               courseRegistrar.get(department)
               .add(new Courses(number, numSeats));
               return this;
            }
         }
      }
      
      return this;
   }

   // This method cancels course with the parameter course information, 
   // meaning it will remove the course from the system permanently. 
   public boolean cancelCourse(String department, int number) {
      if (department == null || department.isEmpty() || number <= 0) {
         throw new IllegalArgumentException();
      }

      // If the department does not exist, return false.
      if (!courseRegistrar.containsKey(department)) {
         return false;
      }

      // Run the for-each loop to search through all the set of courses to 
      // check if the course already exists. 
      for (Courses c : courseRegistrar.get(department)) {
         if (c.containsCourse(number)) { // If the course is found, return.
            c.removeCourse(number);
            return true;
         }
      }
      // If the course is not in the system, return false.
      return false;
   }

   // This method returns the total number of courses that exist in the system.
   public int numCourses() {
      int totalCourses = 0;

      // Run the nested for-each loop to search through all the set of courses  
      // to count the total number of courses in the system. 
      for (String str : courseRegistrar.keySet()) { // Loops through keys.
         for (Courses c : courseRegistrar.get(str)) { // Loops through courses.
            totalCourses += c.totalCourses();
         }
      }
      
      return totalCourses;
   }

   // This method will add student with first and last name to current system.
   public boolean addToCourse(String department, int number,
         String firstName, String lastName) {
      if (department == null || department.isEmpty() || number <= 0 
            || firstName == null || firstName.isEmpty()
            || lastName == null || lastName.isEmpty()) {
         throw new IllegalArgumentException();
      }

      // If the department does not exists, return false.
      if (!courseRegistrar.containsKey(department)) {
         return false;
      } else { // If the department exists, continue. 
         for (Courses c : courseRegistrar.get(department)) {
            // If the course exists, continue. 
            if (c.containsCourse(number)) {
               // If the course is full or there is already a student or the
               // student is already taking more than parameter maxCourses
               // return false/ 
               if (c.isFull(number) || 
                     c.findStudentWithFull(firstName, lastName)
                  || howManyCoursesTaking(firstName, lastName) >= maxCourses){
                  return false;
               } else { // If it pass all the conditions, add student to the
                        // course and return true.
                  c.addStudent(firstName, lastName);
                  return true;
               }
            }
         }
      }
      
      return false;
   }

   // This method will return the amount of students currently enrolled in
   // the course with information from the parameters. 
   public int numStudentsInCourse(String department, int number) {
      if (department == null || department.isEmpty()) {
         throw new IllegalArgumentException();
      }

      // If the department does not exist, return -1.
      if (!courseRegistrar.containsKey(department)) {
         return -1;
      }

      // Run the for-each loop to find the right course and return the number.
      for (Courses c : courseRegistrar.get(department)) {
         if (c.containsCourse(number)) { // If the course is found, return.
            return c.totalStudents(number);
         }
      }

      // If the course is not found in the system, return -1. 
      return -1;
   }

   // This method will return the total amount of students in the course with
   // given parameters who have last name same as parameter lastName. 
   public int numStudentsInCourseWithLastName(String department,
         int number, String lastName) {
      if (department == null || department.isEmpty() || number <= 0 
            || lastName == null || lastName.isEmpty()) {
         throw new IllegalArgumentException();
      }
      
      // Run the for-each loop to find the course with given parameters. 
      for (Courses c : courseRegistrar.get(department)) {
         if (c.containsCourse(number)) { // If the course is found, return.
            return c.findStudentWithLast(lastName);
         }
      }

      return -1;
   }

   public boolean isInCourse(String department, int number,
         String firstName, String lastName) {
      if (department == null || department.isEmpty() || number <= 0 
            || firstName == null || firstName.isEmpty()
            || lastName == null || lastName.isEmpty()) {
         throw new IllegalArgumentException();
      }

      // If the department does not exist, return false.
      if (!courseRegistrar.containsKey(department)) {
         return false;
      }
      // Run the for-each loop to find the course with given parameters. 
      for (Courses c : courseRegistrar.get(department)) {
         if (c.containsCourse(number)) { // If the course is found, continue.
            // If the course contains student with first and last name same
            // as the parameters, return true.
            if (c.findStudentWithFull(firstName, lastName)) {
               return true;
            } else { // Return false otherwise, to stop the for-each loop. 
               return false;
            }
         }
      }

      // Return false otherwise.
      return false;
   }

   // This method will return how many courses the student with parameters is 
   // taking. 
   public int howManyCoursesTaking(String firstName, String lastName) {
      if (firstName == null || firstName.isEmpty() || lastName == null 
            || lastName.isEmpty()) {
         throw new IllegalArgumentException();
      }

      int total = 0;

      // Use the nested for-each loop to find the total number of courses
      // the student is currently taking. 
      for (String str : courseRegistrar.keySet()) {
         for (Courses c : courseRegistrar.get(str)) {
            // If you find the student in the course, add 1 to total. 
            if (c.findStudentWithFull(firstName, lastName)) {
               total++;
            }
         }
      }

      // Return the total number of courses the student is currently taking. 
      return total;
   }

   // This method will drop course for student with parameters, and will
   // return true if successful, otherwise false. 
   public boolean dropCourse(String department, int number, 
         String firstName, String lastName) {
      if (department == null || department.isEmpty() || number <= 0 
            || firstName == null || firstName.isEmpty()
            || lastName == null || lastName.isEmpty()) {
         throw new IllegalArgumentException();
      }

      // If the department does not exist, return false.
      if (!courseRegistrar.containsKey(department)) {
         return false;
      }

      // Run the for-each loop to go through the list of courses with a 
      // parameter department. 
      for (Courses c : courseRegistrar.get(department)) {
         if (c.containsCourse(number)) { // If the course is found, return.
            // If the student is found, remove student from the course and
            // return true.
            if (c.findStudentWithFull(firstName, lastName)) {
               c.removeStudent(firstName, lastName);
               return true;
            }
         }
      }

      return false;
   }

   // This method will cancel registration for student with parameters, 
   // meaning it will drop all the courses that the student is currently 
   // enrolled in. 
   public boolean cancelRegistration(String firstName, String lastName) {
      if (firstName == null || firstName.isEmpty() || lastName == null 
            || lastName.isEmpty()) {
         throw new IllegalArgumentException();
      }

      // Use flag to determine if the dropping process was successful. 
      boolean flag = false;

      // Use the nested for-each loop to find the courses that the student is 
      // currently taking, and remove student with parameters from each course
      // and return true if it was successful, false otherwise. 
      for (String str : courseRegistrar.keySet()) {
         for (Courses c : courseRegistrar.get(str)) {
            if (c.findStudentWithFull(firstName, lastName)) {
               c.removeStudent(firstName, lastName);
               flag = true;
            }
         }
      }
      
      return flag;
   }

   // This method use threads to add courses concurrently to current object
   // Registrar. One thread is for each String element of the parameter 
   // collection of filenames. 
   public void doRegistrations(Collection<String> filenames) {
      if (filenames == null) {
         throw new IllegalArgumentException();
      }

      // Private fields with array of threads that is the size of collection 
      // filenames. 
      int i = 0;
      Thread[] allThreads = new Thread[filenames.size()];

      // Store each thread of collection. 
      for (String str : filenames) {
         allThreads[i++] = new Thread(new RegistrarWorker(str));
      }

      // This will start all the threads. 
      for (Thread t : allThreads) {
         t.start();
      }

      // This will make sure each thread is finished before the method is
      // finished. 
      for (Thread t : allThreads) {
         try {
            t.join();
         } catch (InterruptedException e) { // Catch exception from .join().
            e.printStackTrace();
         }
      }

   }
}
