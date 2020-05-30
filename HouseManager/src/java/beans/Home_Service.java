package beans;

import classes.Houses;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@Stateful
@ManagedBean(name="Home_Service")
@SessionScoped

public class Home_Service implements Serializable{
    
    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<Houses> list_houses = new ArrayList<>();
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    // Getters and Setters
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public ArrayList<Houses> getList_houses() {
        return list_houses;
    }

    public void setList_houses(ArrayList<Houses> list_houses) {
        this.list_houses = list_houses;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Get the available houses to be requested.
     * Parameters : Username
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void updateHousesArray(String username) throws SQLException{
        
        // restart the arraylist
        this.list_houses = new ArrayList<Houses>();
        // variables for future loop
        String address, zip_code, owner, properties;
        float value;
        int evaluation, count = 0;
        
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");

        // prepare to create a statement
        Statement ps = con.createStatement();
        Statement ps2 = con.createStatement();
        
        try{
            // check if there's any user with that properties
            ResultSet x = ps.executeQuery("SELECT H.PROPERTIES, H.ADDRESS, H.ZIP_CODE, H.VALUE, H.EVALUATION, H.OWNER FROM "
            + "HOUSES H LEFT JOIN PROCESS P ON H.ADDRESS = P.ADDRESS AND H.ZIP_CODE = P.ZIP_CODE");
            // returns in 'list' form
            while (x.next()) {
                // get address
                address = x.getString("address");
                // get zip code
                zip_code = x.getString("zip_code");
                // check if there's any house with that address (username)
                ResultSet aux = ps2.executeQuery("SELECT Count(*) as count FROM Process WHERE address = '" + address + "' and zip_code "
                + "= '" + zip_code + "' and username = '" + username + "'");
                while(aux.next())
                    count = aux.getInt("count");
                // verify
                if(count > 0)
                    continue;
                // get value
                value = x.getFloat("value");
                // get evaluation
                evaluation = x.getInt("evaluation");
                // get owner
                owner = x.getString("owner");
                // get properties
                properties = x.getString("properties");
                // create object
                Houses obj = new Houses(owner, address, zip_code, properties, evaluation);
                // check if its already in array
                if(checkData(obj)){
                    // append in arraylist
                    list_houses.add(obj);
                }       
            }
        }catch(SQLException  e){
        };
        // close the connection and the statement
        con.close();
        ps.close();
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Check if there's any repeated value
     * Parameters : Object
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean checkData(Houses obj){
        for(Houses i : list_houses){
            if(i.getAddress().equals(obj.getAddress()) && i.getZip_Code().equals(obj.getZip_Code()))
                return false;
        }
        return true;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Request a house.
     * Parameters : Username, Address, Zip_Code
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void requestHouse(String username, String address, String zip_code) throws SQLException, IOException{
        
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        if(username.isEmpty()){
            response.sendRedirect("intro.xhtml");
        }
        
        // integer variable
        int c = 0;
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();
        Statement ps2 = con.createStatement();
        
        try{
            // getting the max value in position table's attribue (PROCESS)
            ResultSet x = ps.executeQuery("SELECT MAX(position) as maximo FROM PROCESS WHERE ADDRESS = '" + address + "' AND ZIP_CODE = '" + zip_code + "'");
            // get data
            while(x.next()){
                c = x.getInt("maximo");
                c++;
            }
            // insert in database
            ps2.executeUpdate("INSERT INTO PROCESS VALUES('" + username + "','" + address + "','" + zip_code + "','Good'," + c + ")");
        }catch(SQLException  e){};
        
        // close the connection and the statement
        con.close();
        ps.close();
        // update array
        this.updateHousesArray(username);
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

}   
