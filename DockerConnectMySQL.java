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
                    System.out.println("Łączenie z serwerem bazy danych");
                    
                }
            } while (!connect);
			System.out.println("Połączono z serwerem bazy danych");
            stmt = conn.createStatement();
            String sql;
            sql = "DROP TABLE IF EXISTS Persons";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE Persons (PersonID int, LastName varchar(255), FirstName varchar(255), City varchar(255) );";
            stmt.executeUpdate(sql);
            sql = "INSERT INTO Persons(PersonID, LastName, FirstName, City) VALUES (1,'Komoda', 'Alfred', 'Lublin'),(2, 'Worsall', 'Raimund', 'Penambangan'),(3, 'Allcorn', 'Janie', 'Florencia');";
            stmt.executeUpdate(sql);
            Scanner menu = new Scanner(System.in);
            String i;
            do {
                System.out.println("");
                System.out.println("menu od sql");
                System.out.println("wpisz tylko liczbę");
                System.out.println("wybierz opcje:");
                System.out.println("(1) dodanie encji");
                System.out.println("(2) wyświetlenie");
                System.out.println("(3) wyjdz");
                i = (String) menu.next();
                switch (i) {
                    case "1": {
                        Scanner insert = new Scanner(System.in);
                        sql = "SELECT PersonID FROM Persons ORDER BY PersonID DESC LIMIT 1;";
                        ResultSet rs = stmt.executeQuery(sql);
                        int e = 0;
                        if (rs.next()) {
                            e = rs.getInt("PersonID");
                        }
                        rs.close();
                        e++;
                        sql = "INSERT INTO Persons (PersonID, LastName, FirstName, City) VALUES (" + e + ",'";
                        System.out.println("Podaj Nazwisko:");
                        sql += insert.nextLine();
                        sql += "', '";
                        System.out.println("Podaj Imię:");
                        sql += insert.nextLine();
                        sql += "', '";
                        System.out.println("Podaj Miasto:");
                        sql += insert.nextLine();
                        sql += "');";
                        stmt.executeUpdate(sql);
						System.out.println("Dodano encje");
                        break;
                    }
                    case "2": {
                        sql = "SELECT PersonID, FirstName, LastName, City FROM Persons";
                        ResultSet rs = stmt.executeQuery(sql);
                        System.out.printf("|%5s|%15s|%15s|%15s|\n", "ID: ", "Imię: ", "Nazwisko: ", "Miasto: ");
                        while (rs.next()) {
                            int id = rs.getInt("PersonID");
                            String first = rs.getString("FirstName");
                            String last = rs.getString("LastName");
                            String city = rs.getString("City");
                            System.out.printf("|%4d |%14s |%14s |%14s |\n", id, first, last, city);
                        }
                        rs.close();
                        break;
                    }
                    case "3": {
                        i = "3";
                        break;
                    }
                    default: {
						System.out.println("");
                        System.out.println("Wybierz odpowiednią opcje!");
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
