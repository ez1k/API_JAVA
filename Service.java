/**
 * @author Wolańczyk Adam S22484
 */

package API;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Service {
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String NBP_A = "http://api.nbp.pl/api/exchangerates/tables/a/";
    private static final String NBP_B = "http://api.nbp.pl/api/exchangerates/tables/b/";
    private static final String EXCHANGE_test = "https://api.exchangerate.host/convert?from=";
    private static final String EXCHANGE = "https://v6.exchangerate-api.com/v6/6d89eb4dd26c895af00a9f62/latest/";

    private String country;
    private String country_code;
    private String currency;
    private String city;


    public Service(String given_country) {
        city = "";
        country = given_country;


        Map<String, String> countries = new HashMap<>();
        for (String country_code : Locale.getISOCountries()) {
            Locale l = new Locale("", country_code);
            countries.put(l.getDisplayCountry(Locale.ENGLISH), country_code);
        }
        country_code = countries.get(given_country);

        try {
            currency = Currency.getInstance(new Locale("", country_code)).getCurrencyCode();
            setCurrency(currency);
        } catch (Exception e) {
            System.err.println("Wrong country");
            System.exit(0);
        }

    }

    private void setCurrency(String currency) {
        this.currency = currency;
    }
    private void setCity(String city) {
        this.city = city;
    }



    public String getWeather(String selected_city) {
        city = selected_city;

        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        String key = ("97d8990dc09a98a2a1479ec044bac0eb");
        String data = connect_and_get_data(WEATHER_URL + selected_city + "," + country + "&APPID=" + key + "&units=metric");
        String DescRegex = "\"description\":\"([^\"]+)\"";
        String TempRegex = "\"temp\":(-?\\d+\\.\\d+)";
        String FeltTempRegex = "\"feels_like\":(-?\\d+\\.\\d+)";
        String humRegex = "\"humidity\":(\\d+)";
        String windRegex = "\"speed\":(\\d+\\.\\d+)";
        String desc = Get_Data_from_JSON(data, DescRegex);
        String temp = Get_Data_from_JSON(data, TempRegex);
        String Felttemp = Get_Data_from_JSON(data, FeltTempRegex);
        String humidity = Get_Data_from_JSON(data, humRegex);
        String wind = Get_Data_from_JSON(data, windRegex);
        return  "Description: " + desc +
                "\nTemperature: " + temp + " °C\n" +
                "feels_like: " + Felttemp + " °C\n" +
                "Humidity: " + humidity + "%" +
                "\nWind speed: " + wind + " m/s";
    }

    public Double getRateFor(String given_currency) {
        String data = connect_and_get_data(EXCHANGE_test + currency + "&to=" + given_currency);
        String numberRegex = "\\d+\\.\\d+";
        Pattern pattern = Pattern.compile(numberRegex);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String matches = matcher.group();
            Double result = Double.parseDouble(matches);
            return result;
        }

        return 1.0;

    }

    public double getNBPRate() {

        if(!currency.equals("PLN")) {
            String data = connect_and_get_data(NBP_A);
            String CurrRegex = "\"code\":\"" + currency + "\",\"mid\":(\\d+\\.\\d+)";
            Pattern pattern = Pattern.compile(CurrRegex);
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                String matches = matcher.group(1);
                Double result = Double.parseDouble(matches);
                return result;

            }
            else {
                data = connect_and_get_data(NBP_B);
                CurrRegex = "\"code\":\"" + currency + "\",\"mid\":(\\d+\\.\\d+)";
                pattern = Pattern.compile(CurrRegex);
                matcher = pattern.matcher(data);
                if (matcher.find()) {
                    String matches = matcher.group(1);
                    Double result = Double.parseDouble(matches);
                    return result;

                } else {return 0;}
            }} else{return 1.0;}
    }



    public String connect_and_get_data(String string ) {
        String data = "";
        try {

            URL url = new URL(string);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = null;
                while ((line = br.readLine()) != null) {

                    data = line;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    public String Get_Data_from_JSON(String JSON, String Regex)
    {
        String CurrRegex = Regex;
        Pattern pattern = Pattern.compile(CurrRegex);
        Matcher matcher = pattern.matcher(JSON);
        if (matcher.find()) {
            String matches = matcher.group(1);
            return matches;

        }
        return "";
    }

    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public String getCurrency()
    {
        return currency;
    }
}
