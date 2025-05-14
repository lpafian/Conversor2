import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;

public class CurrencyConverterApp {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¡Bienvenido al conversor de monedas!");

        while (true) {
            System.out.println("\n*****************************************");
            System.out.println("1. DOLAR =>> PESO ARGENTINO");
            System.out.println("2. PESO ARGENTINO =>> PESO DOLAR");
            System.out.println("3. DOLAR =>> PESO REAL BRASILEÑO");
            System.out.println("4. REAL BRASILEÑO  =>> DOLAR");
            System.out.println("5. DOLAR =>> PESO PESO COLOMBIANO");
            System.out.println("6. PESO COLOMBIANO =>> DOLAR");
            System.out.println("7. SALIR");
            System.out.print("INGRESE UNA OPCION VALIDA: ");
            System.out.println("\n******************************************");
            int opcion = scanner.nextInt();

            if (opcion == 7) {
                System.out.println("SALIENDO ¡Hasta pronto!");
                break;
            }

            System.out.print("INGRESE EL VALOR QUE DESEA CONVERTIR...: ");
            double monto = scanner.nextDouble();

            String fromCurrency = "", toCurrency = "";
            switch (opcion) {
                case 1: fromCurrency = "USD"; toCurrency = "ARS"; break;
                case 2: fromCurrency = "ARS"; toCurrency = "USD"; break;
                case 3: fromCurrency = "BRL"; toCurrency = "USD"; break;
                case 4: fromCurrency = "USD"; toCurrency = "BRL"; break;
                case 5: fromCurrency = "EUR"; toCurrency = "USD"; break;
                case 6: fromCurrency = "USD"; toCurrency = "EUR"; break;
                default: System.out.println("Opción inválida. Por favor intente de nuevo."); continue;
            }

            double tasaCambio = obtenerTasaCambio(fromCurrency, toCurrency);
            if (tasaCambio != -1) {
                double resultado = monto * tasaCambio;
                System.out.printf("EL VALOR DE  %.2f %s CORRESPONDE AL VALOR FINAL DE =>>> %.2f %s.\n", monto, fromCurrency, resultado, toCurrency);
            }
        }
        scanner.close();
    }

    private static double obtenerTasaCambio(String fromCurrency, String toCurrency) {
        try {
            URL url = new URL(API_URL + fromCurrency);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getJSONObject("rates").getDouble(toCurrency);
        } catch (Exception e) {
            System.out.println("ERROR AL OBTENER LA MONEDA DE CAMBIO: " + e.getMessage());
            return -1;
        }
    }
}