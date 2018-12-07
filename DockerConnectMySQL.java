import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.io.Console;

public class DockerConnectMySQL {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://10.0.10.3:3306/baza";

   static final String USER = "kkonder";
   static final String PASS = "password";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;	 
   try{   
         Class.forName("com.mysql.jdbc.Driver");
	 Boolean connect = false;
      while(!connect)
      {
         try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            connect = true;
         }
         catch(Exception e) {
       	 
            System.out.println("Łączenie z bazą");
            Thread.sleep(5000);
         }
      }
      stmt = conn.createStatement();
      String sqlSelect, sqlNewDb, sqlDrop;
	  Boolean stop = false;
	  sqlDrop ="DROP TABLE IF EXISTS zadanko";
      stmt.executeUpdate(sqlDrop); 
	  sqlNewDb="CREATE TABLE zadanko ( imie varchar(255), nazwisko varchar(255), wiek int)"; 
      System.out.println("Tworzenie bazy");
      stmt.executeUpdate(sqlNewDb);
      
      stmt.executeUpdate("INSERT INTO zadanko ( imie, nazwisko, wiek)"+ 
			 "VALUES('Michał', 'Wiśniewski', '25')"+
			       ",('Kamil', 'Grosicki','53')"+
			       ",('Andrzej','Grabowski', '36')");  			   
	Scanner input = new Scanner(System.in);
	   while(!stop)
	   {	
      		System.out.println("[1] Pokaż tabelę");
      		System.out.println("[2] Dodaj encję");
      		System.out.println("[3] Koniec"); 
		   int val = input.nextInt();  
		   switch(val) {
			    case 1:
				
		   		ResultSet rs = stmt.executeQuery("SELECT imie, nazwisko, wiek FROM zadanko");

      				while(rs.next()){
         				String imie = rs.getString("imie");
         				String nazwisko = rs.getString("nazwisko");
						int wiek = rs.getInt("wiek");
         				System.out.println("imie: " + imie + ", nazwisko:  " + nazwisko+", wiek: " + wiek);
      				}
				   rs.close();
		   		break;
				case 2:
				
					String imie, nazwisko;
				        int wiek;
					System.out.println("Podaj imię:");
					imie=input.next();
					System.out.println("Podaj nazwisko:");
					nazwisko=input.next();
		
					System.out.println("Dodaj dane do tabeli");
					stmt.executeUpdate("INSERT INTO zadanko ( imie, nazwisko, wiek)"+ 
									   "VALUES('" + imie + "','" + nazwisko + "',"+ wiek +")");
				break;
				case 3:
				
					stop=true;
					break;
				default:
			
					System.out.println("Error");
			}
	   }
	   
      stmt.close();
      conn.close();
   }catch(SQLException se){
      se.printStackTrace();
   }catch(Exception e){
      e.printStackTrace();
   }finally{
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
   }
 }
}
