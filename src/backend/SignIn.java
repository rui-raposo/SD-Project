
import java.sql.*;
import java.sql.DriverManager;
import javax.faces.bean.ManagedBean;




@ManagedBean(name="signIn")
public class SignIn {
    
    private String name, password;

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
    
    public String signInFunction(String username, String password) throws SQLException{
        
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        Statement ps = con.createStatement();
        try{
            ResultSet  x = ps.executeQuery("SELECT COUNT(*) as count FROM USERS WHERE username = '"+ username +"' and password = '"+ password +"'");
            while (x.next()) {
                int amount = x.getInt("count"); 
                con.close();
                ps.close();
                if(amount == 1){
                    return "home";
                }
            }
        }catch(SQLException  e){
            con.close();
            ps.close();
        };
        return "";
    }
    
    public String signUpFunction(String username, String pw, String pw2) throws SQLException{
        
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        Statement ps = con.createStatement();
        
        if(!pw.equals(pw2)){
            ps.close();
            con.close();
            return "";            
        }
        
        try{
            ps.executeUpdate("INSERT INTO USERS VALUES('"+ username +"','"+ pw +"')");
            ps.close();
            con.close();
        }catch(SQLException  e){
            ps.close();
            con.close();
            return "";
        };
        return "home";
    }
    
}
