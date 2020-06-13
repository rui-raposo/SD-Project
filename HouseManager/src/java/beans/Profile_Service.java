package beans;

import classes.Houses;
import classes.HousesProcess;
import java.io.IOException;
import java.io.Serializable;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@Stateful
@ManagedBean(name="Profile_Service")
@SessionScoped

public class Profile_Service implements Serializable{
    
    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<HousesProcess> myHouses = new ArrayList<>();
    private Part uploadedImg;
    private final String path = "C:\\Users\\PC-HP\\Documents\\NetBeansProjects\\HouseManager\\web\\pages\\houses_img";
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    // Getters and Setters
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public Part getUploadedImg() {
        return uploadedImg;
    }

    public void setUploadedImg(Part uploadedImg) {
        this.uploadedImg = uploadedImg;
    }
    
    public ArrayList<HousesProcess> getMyHouses(){    
        return myHouses;
    }
    
    public void setMyHouses(ArrayList<HousesProcess> myHouses) {    
        this.myHouses = myHouses;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Get the requestes/owned houses.
     * Parameters : Username
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void updateHousesList(String username) throws SQLException {
        // restart the arraylist
        this.myHouses = new ArrayList<HousesProcess>();
        String address, zip_code, owner, state, properties, path;
        float value;
        int evaluation, pos;
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();
        try{
            // get all the requested/owned houses.
            ResultSet x = ps.executeQuery("SELECT H.IMG, H.PROPERTIES, P.POSITION, H.ADDRESS, H.ZIP_CODE, H.VALUE, H.EVALUATION, H.OWNER FROM HOUSES H INNER JOIN "
                    + "PROCESS P ON H.ADDRESS = P.ADDRESS AND H.ZIP_CODE = P.ZIP_CODE WHERE P.USERNAME = '" + username + "'");
            // loop to data
            while(x.next()){
                // get address
                address = x.getString("address");
                // get zip code
                zip_code = x.getString("zip_code");
                // get value
                value = x.getFloat("value");
                // get evaluation
                evaluation = x.getInt("evaluation");
                // get owner
                owner = x.getString("owner");
                // get position
                pos = x.getInt("position");
                // get properties
                properties = x.getString("properties");
                // get image path
                path = x.getString("img");
                // create object
                Houses obj = new Houses(owner, address, zip_code, properties, value, evaluation, path);
                // append in arraylists method
                this.getCurrentStatus(username, address, zip_code, pos, obj);
            }
        }catch(SQLException e){};
        
        // close the connection and the statement
        con.close();
        ps.close();
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Append in another arraylist!
     * Parameters : Username, Address, Zip_Code, Position, Object
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void getCurrentStatus(String username, String address, String zip_code, int position, Houses object) throws SQLException{
    
        // integer var
        int min_pos = -1;
        
        // string variables
        String color = "", status = "";
        
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");

        // prepare to create a statement
        Statement ps = con.createStatement();
        
        try{
            // looking for the min position
            ResultSet x = ps.executeQuery("SELECT MIN(position) as minimo FROM PROCESS WHERE ADDRESS = '" + address + "' "
                    + "AND ZIP_CODE = '" + zip_code + "'");
            // loop to get data
            while(x.next()){
                min_pos = x.getInt("minimo");
            }
            // compare with the username's position
            if(min_pos == position){
                status = "Deliver";
                color = "red";
            }
            else{
                status = "Queue";
                color = "gray";
            }
        }catch(SQLException e){};
        
        // close the connection and the statement
        con.close();
        ps.close();
        
        // create the final object
        HousesProcess house_proccess = new HousesProcess(object, color, status);

        // append in arraylist
        this.myHouses.add(house_proccess);
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Deliver the house
     * Parameters : Username, Address, Zip Code, Button_Color
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void deliverHouse(String username, String address, String zip_code, String color) throws SQLException{
        
        if(color.equals("gray"))
            return;
        
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");

        // prepare to create a statement
        Statement ps = con.createStatement();
        
        try{
            // delete the respective row
            ps.executeUpdate("DELETE FROM PROCESS WHERE USERNAME = '" + username + "' AND ADDRESS = '" + address + "' AND ZIP_CODE = '" + zip_code + "'");
        }catch(SQLException e){};
        
        // close the connection and the statement
        con.close();
        ps.close();
        
        // update the view
        this.updateHousesList(username);
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    
    /*
     * Goal       : Append a new house
     * Parameters : Username, Address, Zip Code, Lease, Features
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void insertNewHouse(String username, String address, String zip_code, String zip_code2, Float lease, String features, String file) throws SQLException{
        
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123");
        
        String fileName = "default.png";
        
        // prepare to create a statement
        Statement ps = con.createStatement();

        try (InputStream input = uploadedImg.getInputStream()) {
            fileName = address + zip_code  + uploadedImg.getSubmittedFileName();
            fileName = fileName.replaceAll("\\s+","");
	        Files.copy(input, new File(path, fileName).toPath());
	    }catch (IOException e) {
	        e.printStackTrace();
	    }
        
        // format zip_code
        String new_zip = zip_code + " - " + zip_code2;
        
        try{
            ps.executeUpdate("INSERT INTO HOUSES VALUES('" + address + "','" + new_zip + "','" + username + "'," + lease + ", 1, '" + features + "', 'houses_img/" + fileName + "')");
        }catch(SQLException e){};
        
        // close the connection and the statement
        con.close();
        ps.close();
        
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------


    /*
     * Goal       : Update the house rate
     * Parameters : Username, button's color, value(rate), address and zip_code
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void updateRate() throws SQLException{
        
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String username = params.get("username");
        String color = params.get("color");
        String value =  params.get("rate"); 
        int i_value = Integer.parseInt(value);
        String address =  params.get("address"); 
        String zip_code =  params.get("zip");

        this.deliverHouse(username, address, zip_code, color);

        Statement ps;
        Statement ps2;
        Statement ps3;
        
        try( 

            // connection to the database
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo","raposo0990123")) {

            // prepare to create a statement
            ps = con.createStatement();
            ps2 = con.createStatement();
            ps3 = con.createStatement();
            
            ResultSet x,y;
            // value
            int amount = 0;
            int rate = 1;
            int rate_final;

            try{
                
                x = ps.executeQuery("SELECT evaluation as eval FROM Houses WHERE address = '" + address + "' and zip_code = '" + zip_code + "'");
                while(x.next()){
                    rate = x.getInt("eval");
                }
                
                y = ps2.executeQuery("SELECT Count(*) as amount FROM History WHERE address = '" + address + "' and zip_code = '" + zip_code + "'");
                while(y.next()){
                    System.out.println(y);
                    amount = y.getInt("amount");
                }
                
                if(amount <= 1 )
                    rate_final = i_value;
                else
                    rate_final = (rate+i_value)/(amount);
                

                if(rate_final <= 5)
                    ps3.executeUpdate("UPDATE Houses SET evaluation = " + rate_final + " WHERE address = '" + address + "' and zip_code = '" + zip_code + "'");
                
                // close database access
                con.close();

            }catch(SQLException  e){};
            
            
        }

        // close the connection to the database
        ps.close();
        ps2.close();
        ps3.close();

    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
}
