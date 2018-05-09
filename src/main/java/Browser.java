import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONObject;

public class Browser {

    private static final Map<String, String> letters = new HashMap<String, String>();
    static {
        letters.put("а", "a");
        letters.put("б", "b");
        letters.put("в", "v");
        letters.put("г", "g");
        letters.put("д", "d");
        letters.put("е", "e");
        letters.put("ё", "e");
        letters.put("ж", "zh");
        letters.put("з", "z");
        letters.put("и", "i");
        letters.put("й", "i");
        letters.put("к", "k");
        letters.put("л", "l");
        letters.put("м", "m");
        letters.put("н", "n");
        letters.put("о", "o");
        letters.put("п", "p");
        letters.put("р", "r");
        letters.put("с", "s");
        letters.put("т", "t");
        letters.put("у", "u");
        letters.put("ф", "f");
        letters.put("х", "h");
        letters.put("ц", "c");
        letters.put("ч", "ch");
        letters.put("ш", "sh");
        letters.put("щ", "sch");
        letters.put("ь", "");
        letters.put("ы", "y");
        letters.put("ъ", "");
        letters.put("э", "e");
        letters.put("ю", "yu");
        letters.put("я", "ya");
        letters.put(" ", "-");
        letters.put("А", "a");
        letters.put("Б", "b");
        letters.put("В", "v");
        letters.put("Г", "g");
        letters.put("Д", "d");
        letters.put("Е", "e");
        letters.put("Ё", "e");
        letters.put("Ж", "zh");
        letters.put("З", "z");
        letters.put("И", "i");
        letters.put("Й", "i");
        letters.put("К", "k");
        letters.put("Л", "l");
        letters.put("М", "m");
        letters.put("Н", "n");
        letters.put("О", "o");
        letters.put("П", "p");
        letters.put("Р", "r");
        letters.put("С", "s");
        letters.put("Т", "t");
        letters.put("У", "u");
        letters.put("Ф", "f");
        letters.put("Х", "h");
        letters.put("Ц", "c");
        letters.put("Ч", "ch");
        letters.put("Ш", "sh");
        letters.put("Щ", "sch");
        letters.put("Ь", "");
        letters.put("Ы", "y");
        letters.put("Ъ", "");
        letters.put("Э", "e");
        letters.put("Ю", "yu");
        letters.put("Я", "ya");
    }
    public static String toTranslit(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i<text.length(); i++) {
            String l = text.substring(i, i+1);
            if (letters.containsKey(l)) {
                sb.append(letters.get(l));
            }
            else {
                sb.append(l);
            }
        }
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("-MM-dd");
        String URLTelegraph = "http://telegra.ph/";
        String result = URLTelegraph + sb.toString() + formatForDateNow.format(date);
        return result;
    }
    public static String check(String first_name, String last_name, int user_id, String username) {
        // Set loggers
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_name");
        MongoCollection<Document> collection = database.getCollection("users");
        long found = collection.count(Document.parse("{id : " + Integer.toString(user_id) + "}"));
        if (found == 0) {
            Document doc = new Document("first_name", first_name)
                    .append("last_name", last_name)
                    .append("id", user_id)
                    .append("username", username);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            return "no_exists";
        } else {
            System.out.println("User exists in database.");
            mongoClient.close();
            return "exists";
        }


    }
    public static void main(String[] args) {

    }
}