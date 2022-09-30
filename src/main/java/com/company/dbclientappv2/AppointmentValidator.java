package com.company.dbclientappv2;

import java.sql.Timestamp;

public class AppointmentValidator {
    public static boolean checkAppointmentInputs(String title, String desc, String location, String type, Timestamp start, Timestamp end){
        //check appointment against all other appointments to prevent time collision
        //pass true if all cases above are correct
        return true;
    }
}
