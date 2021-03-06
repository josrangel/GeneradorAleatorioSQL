/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.develop.generadordatosjson;

import java.io.FileReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


/**
 *
 * @author Josue Rangel(josrangel)
 */
public class Principal {
    final static String RUTA="json\\";//PON LA RUTA DONDE SE ENCUENTRAN TUS ARCHIVOS JSON
    final static String ARCHIVO_NOMBRE=RUTA+"first-names.json";
    final static String ARCHIVO_MIDDLE=RUTA+"middle-names.json";
    final static String ARCHIVO_PLACES=RUTA+"places.json";
    final static int LIMITE_REGISTROS=100;
    final static int CARACTERES_TELEFONO=10;
    final static String[] DOMINIO={"gmail.com.mx","gmail.com","outlook.com","hotmail.com","yahoo.com"};
    final static String INICIO_INSERT="INSERT INTO user(user_account,user_adress,user_date_birth,user_email,user_last_name,user_name,user_password,user_phone,user_status) VALUES('";
    final static String FIN_INSERT="');";
    static Random aleatorio = new Random();
    
    
    public static void main(String args[]) {
        JsonParser parser = new JsonParser();
        try{
        FileReader fr = new FileReader(ARCHIVO_NOMBRE);
        JsonArray datosNombres = parser.parse(fr).getAsJsonArray();
        fr = new FileReader(ARCHIVO_MIDDLE);
        JsonArray datosMiddles = parser.parse(fr).getAsJsonArray();
        fr = new FileReader(ARCHIVO_PLACES);
        JsonArray datosPlaces = parser.parse(fr).getAsJsonArray();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < LIMITE_REGISTROS; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -365*20-aleatorio.nextInt(365*30));
            String fechaNacimiento=sdf.format(calendar.getTime());
            String nombre=obtenValorAletorio(datosNombres);
            String middle=obtenValorAletorio(datosMiddles);
            String telefono=obtenTelefonoAleatorio();
            String account=nombre.substring(0,2).toLowerCase()+middle.substring(0,2).toLowerCase()+fechaNacimiento.substring(2,4);
            String email=obtenerEmail(account);
            String direccion=obtenerDireccion(obtenValorAletorio(datosPlaces));
            String password=getSHA(account);
            String estatus="1";
            //String cuerpoInsert=account+" "+direccion+" "+fechaNacimiento+" "+email+" "+middle+" "+nombre+" "+password+" "+telefono+" "+estatus;
            String cuerpoInsert=account+"','"+direccion+"','"+fechaNacimiento+"','"+email+"','"+middle+"','"+nombre+"','"+password+"','"+telefono+"','"+estatus;
            System.out.println(INICIO_INSERT+cuerpoInsert+FIN_INSERT);
        }
        }catch(JsonIOException | JsonSyntaxException | FileNotFoundException | NoSuchAlgorithmException e){
            System.out.println(e);
        }
        
    }
    
    public static String obtenValorAletorio(JsonArray elementos){
        int arraySize=elementos.size();
        int valorDado = aleatorio.nextInt(arraySize);
        String nombre=elementos.get(valorDado).getAsString();
        /*for(JsonElement elemento: elementos){//imprime todo el contenido del JSON
             System.out.println(elemento.getAsString());
        }*/
        return nombre;
    }

    public static String obtenTelefonoAleatorio(){
        String numeroTelefonico="";
        for (int i = 0; i < CARACTERES_TELEFONO; i++) {
            numeroTelefonico += aleatorio.nextInt(10);
        }
        return numeroTelefonico;
    }
        
    public static String obtenerEmail(String cadena){
        int valorDado = aleatorio.nextInt(DOMINIO.length);
        String correo=cadena+"@"+DOMINIO[valorDado];
        /*for(JsonElement elemento: elementos){
             System.out.println(elemento.getAsString());
        }*/
        return correo;
    }
    
    public static String obtenerDireccion(String cadena){
        int valorDado = aleatorio.nextInt(249)+1;
        String direccion=cadena+" "+valorDado;
        return direccion;
    }
    
    public static String getSHA(String input) throws NoSuchAlgorithmException{  
        // Static getInstance method is called with hashing SHA  
        MessageDigest md = MessageDigest.getInstance("SHA-256");  
        // digest() method called  
        // to calculate message digest of an input  
        // and return array of byte 
        return toHexString(md.digest(input.getBytes(StandardCharsets.UTF_8)));  
    } 
    
    public static String toHexString(byte[] hash){ 
        // Convert byte array into signum representation  
        BigInteger number = new BigInteger(1, hash);  
        // Convert message digest into hex value  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
        // Pad with leading zeros 
        while (hexString.length() < 32){  
            hexString.insert(0, '0');  
        }  
        return hexString.toString();  
    } 
}
