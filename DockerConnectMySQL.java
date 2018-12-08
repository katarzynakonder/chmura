import java.util.Scanner;
import java.sql.*;

public class DockerConnectMySQL {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://10.0.10.3:3306/baza";
    static final String USER = "kkonder";
    static final String PASS = "password";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Boolean connect = false;
            do {
                try {
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    connect = true;
                } catch (Exception e) {
                    System.out.println("Łączenie...");
                    Thread.sleep(1000);
                }
		    //łączenie z bazą
            } while (!connect);
			System.out.println("Połączono z serwerem");
            stmt = conn.createStatement();
            String sql;
		//usuwa tabelę, jeśli taka istnieje
            sql = "DROP TABLE IF EXISTS Ludzie";
            stmt.executeUpdate(sql);
		//tworzy tabeli
            sql = "CREATE TABLE Ludzie (Id int, Imię varchar(255), Nazwisko varchar(255), Wiek int );";
            stmt.executeUpdate(sql);
		//dodaje do tabeli dane
            sql = "INSERT INTO Ludzie(Id, Imię, Nazwisko, Wiek) VALUES (1,'Andrzej', 'Duda', '55'),(2, 'Krzystof', 'Krawczyk', '74'),(3, 'Jacek', 'Placek', '15');";
            stmt.executeUpdate(sql);
            Scanner menu = new Scanner(System.in);
            String i;
            do {
                System.out.println(""); 
                System.out.println("Wybierz opcję");
                System.out.println("[1] Dodaj encję");
                System.out.println("[2] Pokaż zawartość");
                System.out.println("[3] Wyjście");
                i = (String) menu.next();
                switch (i) {
                    case "1": {	  
			    //dodanie danych do tabeli
                        Scanner insert = new Scanner(System.in);
                        sql = "SELECT Id FROM Ludzie ORDER BY Id DESC LIMIT 1;";
                        ResultSet rs = stmt.executeQuery(sql);
                        int e = 0;
                        if (rs.next()) {
                            e = rs.getInt("Id");
                        }
                        rs.close();
                        e++;
                        sql = "INSERT INTO Ludzie (Id, Imię, Nazwisko, Wiek) VALUES (" + e + ",'";
                        System.out.println("Podaj imię:");
                        sql += insert.nextLine();
                        sql += "', '";
                        System.out.println("Podaj Nazwisko:");
                        sql += insert.nextLine();
                        sql += "', '";
                        System.out.println("Podaj Wiek:");
                        sql += insert.nextLine();
                        sql += "');";
                        stmt.executeUpdate(sql);
						System.out.println("Dodano do bazy[!]");
                        break;
                    }
                    case "2": {
			    //pokazanie zawartości
                        sql = "SELECT Id, Imię, Nazwisko, Wiek FROM Ludzie";
                        ResultSet rs = stmt.executeQuery(sql);
                        System.out.printf("|%5s|%15s|%15s|%15s|\n", "Id: ", "Imię: ", "Nazwisko: ", "Wiek: ");
                        while (rs.next()) {
                            int id = rs.getInt("Id");
                            String imię = rs.getString("Imię");
                            String nazwisko = rs.getString("Nazwisko");
                            String wiek = rs.getString("Wiek");
                            System.out.printf("|%4d |%14s |%14s |%14s |\n", id, imię, nazwisko, wiek);
                        }
                        rs.close();
                        break;
                    }
                    case "3": {
			    //wyjście z programu
                        i = "3";
                        break;
                    }
                    default: {
			    //wybór nieprawidłowej opcji
						System.out.println("");
                        System.out.println("Wybierz prawidłową opcję[!]");
                        break;
                    }
                }
            } while (i != "3");
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
