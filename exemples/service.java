import java.sql.*;
import java.sql.DriverManager;
import javax.faces.bean.ManagedBean;


@ManagedBean(name="signIn")

public class SignIn {
    
    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private String name, password;
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    // Getters and Setters
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /*
     * Goal            : Validate the sign in
     * Parameters      : Username and Password inserted from the input fields (index.xhtml)
     * Security        : Decrypt from database and compare with the inserted password ?? [To be approved]
     * Dificulty Level : [0-10] --> 1
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public String signInFunction(String username, String password) throws SQLException{
        
        // integer var
        int amount = -1;
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();

        try{
            // check if there's any user with that properties
            ResultSet x = ps.executeQuery("SELECT COUNT(*) as count FROM USERS WHERE username = '"+ username +"' and password = '"+ password +"'");
            // returns in 'list' form
            while (x.next()) {
                // check the int value
                amount = x.getInt("count");
            }
        }catch(SQLException  e){};

        // close the connection and the statement
        con.close();
        ps.close();

        // **if there's** a user direct to the new page "home.xhtml" *else* nothing happens
        if(amount == 1)
            return "home";
        return "";

    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /*
     * Goal            : Valide the sign up
     * Parameters      : Username, Password, Password2 from the input fields (index.xhtml)
     * Security        : If there's any user with the specific username and the passwords match, cipher the password ?? [To be approved]
     * Dificulty Level : [0-10] --> 1
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public String signUpFunction(String username, String pw, String pw2) throws SQLException{
        
        // if the passwords are not the same
        if(!pw.equals(pw2))
            return "";

        // integer var
        int flag = 0;
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();
        
        try{
            // try to insert in the database (username is the primary key!)
            ps.executeUpdate("INSERT INTO USERS VALUES('"+ username +"','"+ pw +"')");
            flag = 1;
        }catch(SQLException  e){};

        // close the connection and the statement
        ps.close();
        con.close();

        // **if there's not** a user, direct to the new page "home.xhtml" *else* nothing happens
        if(flag == 0)
            return "";
        return "home";

    }
   // ---------------------------------------------------------------------------------------------------------------------------------------------------

}