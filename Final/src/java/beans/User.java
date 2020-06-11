package beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@Stateful
@ManagedBean(name="User")
@SessionScoped

public class User implements Serializable{

    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private String username, password;
    private int is_logged;
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    

    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /*
     * Goal       : Validate the sign in
     * Parameters : Username and Password inserted from the input fields (index.xhtml)
     * Security   : Decrypt from database and compare with the inserted password ?? [To be approved]
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void signInFunction(String username, String password) throws SQLException, IOException{
        
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
        if(amount == 1){
            // update the 'session' variable
            this.username = username;
            this.password = password;
            this.is_logged = 1;
            
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.sendRedirect("home.xhtml");
        }      
        else{
            // error
            this.is_logged = -1;
        }
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /*
     * Goal       : Valide the sign up
     * Parameters : Username, Password, Password2 from the input fields (index.xhtml)
     * Security   : If there's any user with the specific username and the passwords match, cipher the password ?? [To be approved]
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void signUpFunction(String username, String pw, String pw2) throws SQLException, IOException{
        
        // if the passwords are not the same
        if(!pw.equals(pw2))
            return;

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
            return;
        
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        this.username = username;
        this.password = pw;
        response.sendRedirect("home.xhtml");

    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Validade if the current class variables are valid
     * Parameters : page name
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void validUserData(String page) throws SQLException, IOException{
        
        // integer variable
        int amount = 0;
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();
        
        try{
            // look for the data in db
            ResultSet x = ps.executeQuery("SELECT Count(*) as count FROM Users WHERE username = '" + this.username + "' and password = '" + this.password + "'");
            // returns in 'list' form
            while (x.next()) {
                // check the int value
                amount = x.getInt("count");
            }
        }catch(SQLException  e){};
        
        // close the connection and the statement
        ps.close();
        con.close();
        
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        if(amount == 0 || this.getUsername().isEmpty() || this.getPassword().isEmpty())
            response.sendRedirect("intro.xhtml");
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Restart all data
     * Parameters : None
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void restartData(){
        this.setUsername("");
        this.setPassword("");
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Check if it's guest or not
     * Parameters : None
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean checkGuest(){
        if(this.username.isEmpty())
            return true;
        return false;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean checkFlag(){
        return this.is_logged == -1;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

}
