package registrar;

//Minsoo Ku, mku1, 118994239, 0104
//I pledge on my honor that I have not given or received any unauthorized 
//assistance on this assignment.

// This class will store information of students, with their first name and
// last name. It will allow each course to track of different students who
// are taking the course. 
public class Students {
   // Fields that is used to store the information of students. 
   private String firstName;
   private String lastName;
   
   // A constructor that stores the information of students. 
   public Students(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
   }
   
   // This method will return true if the student with parameters are found,
   // false otherwise. 
   public boolean firstLastEquals(String firstName, String lastName) {
      // Return true if both first name and last name is the same as the 
      // parameters. 
      return this.firstName.equals(firstName) 
            && this.lastName.equals(lastName);
   }
   
   // This method will return true if the student with the last name from 
   // the parameter is found, and false otherwise. 
   public boolean lastEquals(String lastName) {
      return this.lastName.equals(lastName);
   }
}

