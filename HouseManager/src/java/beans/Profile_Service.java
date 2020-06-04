package beans;

import classes.Houses;
import classes.HousesProcess;
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

@Stateful
@ManagedBean(name = "Profile_Service")
@SessionScoped

public class Profile_Service implements Serializable {

    // Class variables
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<HousesProcess> myHouses = new ArrayList<>();
    private boolean popupFlag = false;
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

    // Getters and Setters
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public ArrayList<HousesProcess> getMyHouses() {
        return myHouses;
    }

    public void setMyHouses(ArrayList<HousesProcess> myHouses) {
        this.myHouses = myHouses;
    }

    public boolean isPopupFlag() {
        return popupFlag;
    }

    public void setPopupFlag(boolean popupFlag) {
        this.popupFlag = popupFlag;
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
        String address, zip_code, owner, state, properties;
        float value;
        int evaluation, pos;
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo", "raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();
        try {
            // get all the requested/owned houses.
            ResultSet x = ps.executeQuery("SELECT H.PROPERTIES, P.POSITION, H.ADDRESS, H.ZIP_CODE, H.VALUE, H.EVALUATION, H.OWNER FROM HOUSES H INNER JOIN "
                    + "PROCESS P ON H.ADDRESS = P.ADDRESS AND H.ZIP_CODE = P.ZIP_CODE WHERE P.USERNAME = '" + username + "'");
            // loop to data
            while (x.next()) {
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
                // create object
                Houses obj = new Houses(owner, address, zip_code, properties, evaluation);
                // append in arraylists method
                this.getCurrentStatus(username, address, zip_code, pos, obj);
            }
        } catch (SQLException e) {
        };

        // close the connection and the statement
        con.close();
        ps.close();
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

    /* Goal       : Change user's password.
     * Parameters : new_password_1, new_password_2, current_password, username
     * Security   : ???
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void changePassword(String username, String password1, String password2, String password_curr) throws SQLException {

        // check if the password1 and password2 match
        if (!password1.equals(password2)) {
            return;
        }

        // String variable 
        String aux = "";
        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo", "raposo0990123");
        // prepare to create a statement
        Statement ps = con.createStatement();

        try {
            // verify if the curr_password match with database one
            ResultSet x = ps.executeQuery("SELECT PASSWORD FROM USERS WHERE USERNAME = '" + username + "'");
            // loop to get data
            while (x.next()) {
                aux = x.getString("password");
            }
        } catch (SQLException e) {
        };

        // compare data
        if (password_curr.equals(aux)) {
            ps.executeUpdate("UPDATE USERS SET password = '" + password1 + "' WHERE username = '" + username + "'");
        }

        // close the connection to the database and statement
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
    public void getCurrentStatus(String username, String address, String zip_code, int position, Houses object) throws SQLException {

        // integer var
        int min_pos = -1;

        // string variables
        String color = "", status = "";

        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo", "raposo0990123");

        // prepare to create a statement
        Statement ps = con.createStatement();

        try {
            // looking for the min position
            ResultSet x = ps.executeQuery("SELECT MIN(position) as minimo FROM PROCESS WHERE ADDRESS = '" + address + "' "
                    + "AND ZIP_CODE = '" + zip_code + "'");
            // loop to get data
            while (x.next()) {
                min_pos = x.getInt("minimo");
            }
            // compare with the username's position
            if (min_pos == position) {
                status = "Deliver";
                color = "red";
            } else {
                status = "Queue";
                color = "gray";
            }
        } catch (SQLException e) {
        };

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
    public void deliverHouse(String username, String address, String zip_code, String color) throws SQLException {

        if (color.equals("gray")) {
            return;
        }

        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo", "raposo0990123");

        // prepare to create a statement
        Statement ps = con.createStatement();

        try {
            // delete the respective row
            ps.executeUpdate("DELETE FROM PROCESS WHERE USERNAME = '" + username + "' AND ADDRESS = '" + address + "' AND ZIP_CODE = '" + zip_code + "'");
        } catch (SQLException e) {
        };

        // close the connection and the statement
        con.close();
        ps.close();

        // Popup flag
        popupFlag = true;

    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * Goal       : Append a new house
     * Parameters : Username, Address, Zip Code, Lease, Features
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    public void insertNewHouse(String username, String address, String zip_code, String zip_code2, Float lease, String features) throws SQLException {

        // connection to the database
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/ProjectDB", "raposo", "raposo0990123");

        // prepare to create a statement
        Statement ps = con.createStatement();

        // format zip_code
        String new_zip = zip_code + " - " + zip_code2;

        try {
            ps.executeUpdate("INSERT INTO HOUSES VALUES('" + address + "','" + new_zip + "','" + username + "'," + lease + ", 5, '" + features + "')");
        } catch (SQLException e) {
        };

        // close the connection and the statement
        con.close();
        ps.close();

    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
      /*
     * Goal       : Update the rating of the house
     * Parameters : User Rating
     * Security   : None
     */
    // ---------------------------------------------------------------------------------------------------------------------------------------------------
    
    public void updateRating(int rating){
        
    }
}
