package beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@Stateless
@ManagedBean(name="Service")

public class Service_Stuff implements Serializable{

    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private String array;
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

    // Getters and Setters
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public String getArray() {
        return array;
    }

    public void setArray(String array) {
        this.array = array;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /* 
     * Goal       : Generate the graphic values
     * Parameters : None
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public String setGraphic() throws SQLException{
        
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();
        Statement ps1 = con.createStatement();
        // integer rate variable
        int total = 1, rate = -1;
        float rate2 = 0;
        ResultSet x,y;
        array = "";
        
        try{

            y = ps1.executeQuery("SELECT COUNT(*) as count FROM Houses");
            while(y.next()){
                total = y.getInt("count");
            }

            if(total != 0 ){
                // loop
                for(int j = 1; j <= 5; j++ ){
                    // query in database
                    x = ps.executeQuery("SELECT COUNT(*) as contador FROM Houses WHERE evaluation = " + j + " ");
                    // loop to get data
                   
                    while(x.next()){
                        rate = x.getInt("contador");
                        rate2 = (float) rate/total;
                        rate2 = rate2*100;
                    }
                    // append in array
                    array += rate2 + ",";
                }
            }

        }catch(SQLException  e){};
        
        // close the connection to the database and statement
        con.close();
        ps.close();
        ps1.close();

        return array;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

}
