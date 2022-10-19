package lists;

import com.company.dbclientappv2.Contact;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactList {
    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    public static void retrieveContacts() {
        try {
            ResultSet rs = DatabaseQueries.retrieveTable("contacts");
            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(contactId, contactName, email);
                allContacts.add(contact);
            }
        }catch(SQLException e) {e.printStackTrace();}
    }

    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }
    public static ObservableList<Integer> getContactIdList(){
        ObservableList<Integer> contactIdList = FXCollections.observableArrayList();
        for(Contact contact : getAllContacts()){
            contactIdList.add(contact.getContactID());
        }
        return contactIdList;
    }

    /**@param id gets contact with id matching*/
    public static Contact getMatchingContact(int id) {
        for (Contact contact : allContacts) {
            if (contact.getContactID() == id) {
                return contact;
            }
        }
        return null;
    }
}
