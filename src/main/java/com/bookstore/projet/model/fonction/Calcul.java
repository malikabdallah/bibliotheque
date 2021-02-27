package com.bookstore.projet.model.fonction;

import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calcul {



   public  static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

   //yyyy-MM-dd
    public static int compareDate(String date1,String date2) throws ParseException {



        Date dd1 = format.parse(date1);
        Date dd2 = format.parse(date2);

        if (date1.compareTo(date2) <= 0) {

            return 1;
        }
        return -1;
    }
}
