import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.io.Console;

public class DockerConnectMySQL {
//stałe potrzebne do łączenia się z bazą danych
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://10.0.10.3:3306/baza";

   static final String USER = "kkonder";
   static final String PASS = "password";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
		 
   try{
	   
         Class.forName("com.mysql.jdbc.Driver");
         System.out.println("Connecting to database...");

	 Boolean connect = false;
    //łączenie z bazą dancyh
      while(!connect)
      {
         try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            connect = true;
         }
         catch(Exception e) {
         //odczekanie 5 sekund w przypadku błędnego połaczenia		 
            System.out.println("Connecting to database...");
            Thread.sleep(5000);
         }
      }
      stmt = conn.createStatement();
      String sqlSelect, sqlNewDb, sqlDrop;
	  Boolean stop = false;
	//usuniecie tabeli jezeli istnieje
	  sqlDrop ="DROP TABLE IF EXISTS test";
      System.out.println("Droping database...");
      stmt.executeUpdate(sqlDrop); 
	  //stworzenie nowej tabeli
	  sqlNewDb="CREATE TABLE test ( imie varchar(255), nazwisko varchar(255), losowaCyfra int)"; 
      System.out.println("Creating database...");
      stmt.executeUpdate(sqlNewDb); 
	   //generator liczb losowych
      Random generator = new Random();
	      //wypełnianie telebi przykładowymi danymi
      System.out.println("Filling table...");
      stmt.executeUpdate("INSERT INTO test ( imie, nazwisko, losowaCyfra)"+ 
			 "VALUES('Jan', 'Nowak', " + generator.nextInt(250) +")"+
			       ",('Aldona', 'Mazurek'," + generator.nextInt(250) + ")"+
			       ",('Aleksander','Kowalski', "+ generator.nextInt(250) +")");  
				   
	Scanner input = new Scanner(System.in);
	//pętla programu
	   while(!stop)
	   {
			System.out.println("Wybierz funkcje:");
      		System.out.println("[1] Wyświetl zawartość tabeli");
      		System.out.println("[2] Dodaj encję");
      		System.out.println("[3] Wyjdź");
		   
		   int val = input.nextInt();
		   
		   switch(val) {
			    case 1:
				//wyswitlanie zawartoci tabeli
		   		ResultSet rs = stmt.executeQuery("SELECT imie, nazwisko, losowaCyfra FROM test");

      				while(rs.next()){
         				String imie = rs.getString("imie");
         				String nazwisko = rs.getString("nazwisko");
						int losowaCyfra = rs.getInt("losowaCyfra");
         				System.out.println("imie: " + imie + ", nazwisko:  " + nazwisko+", losowa cyfra: " + losowaCyfra);
      				}
				   rs.close();
		   		break;
				case 2:
				//wstawianie nowej endcji do tabeli
					String imie, nazwisko;
					System.out.println("Wpisz imię:");
					imie=input.next();
					System.out.println("Wpisz nazwisko:");
					nazwisko=input.next();
					int losowa = generator.nextInt(250);
					System.out.println("Wprowadzanie danych do tabeli...");
					stmt.executeUpdate("INSERT INTO test ( imie, nazwisko, losowaCyfra)"+ 
									   "VALUES('" + imie + "','" + nazwisko + "',"+ losowa +")");
				break;
				case 3:
				//wjscie z programu
					stop=true;
					break;
				default:
				//wybór innej opcji niz podane
					System.out.println("Zła opcja..");
			}
	   }
	   //zamkniecie polaczenia
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

