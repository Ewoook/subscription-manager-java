package logic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {
    private static final String FILE_NAME = "data.JSON";
    private final ObjectMapper mapper;

    public JsonManager()
    {
        mapper = new ObjectMapper();

        //moduł do obsługi localDate
        mapper.registerModule(new JavaTimeModule());

        //daty beda zapisywac sie jako stringi a nie jako tablice
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //formatowanie JSON z wcieciami
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

    }

    public void saveSubscriptions(List<Subscription> subscriptions)
    {
        try{
            //zapis listy subskrypcji do pliku
            mapper.writeValue(new File(FILE_NAME), subscriptions);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<Subscription> loadSubscriptions()
    {
        File file = new File(FILE_NAME);

        System.out.println("CZYTAM PLIK Z: " + file.getAbsolutePath());

        if(!file.exists())
        {
            System.out.println("Plik nie istnieje - tworzę nową listę.");
            return new ArrayList<>();
        }

        try{

            //TypeReference potrzebny zeby jackson wiedzial ze to lista subskrypcji
            return mapper.readValue(file, new TypeReference<List<Subscription>>() {});
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }


    }



}




