package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContactList {
    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    public static Contact getMatchingContact(int id) {
        for (Contact contact : allContacts) {
            if (contact.getContactID() == id) {
                return contact;
            }
        }
        return null;
    }
}
