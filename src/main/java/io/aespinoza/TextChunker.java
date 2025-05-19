package io.aespinoza;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    /**
     * Crea chunks a partir de un texto dado, utilizando tokenización (palabras) y
     * considerando que 1 palabra ≈ 1.2 tokens. Los parámetros chunkSize y overlap se
     * reciben en tokens y se convierten a una cantidad aproximada de palabras.
     *
     * @param text       El texto a procesar.
     * @param tokenChunkSize El tamaño del chunk en tokens.
     * @param tokenOverlap   La cantidad de tokens que se solapan entre dos chunks consecutivos.
     * @return Una cadena en formato JSON con la estructura:
     *         {"errCod":0,"errDes":"Ok","Chunks":[chunk1,chunk2,...]}
     *         En caso de error, se devuelve el JSON con el código y descripción de error correspondientes y un arreglo vacío en "Chunks".
     */
    public static String createChunks(String text, int tokenChunkSize, int tokenOverlap) {
        // Validación de parámetros
        if (text == null) {
            return "{\"errCod\":1,\"errDes\":\"El texto es nulo\",\"Chunks\":[]}";
        }
        if (tokenChunkSize <= 0) {
            return "{\"errCod\":2,\"errDes\":\"El tamaño del chunk debe ser mayor que 0\",\"Chunks\":[]}";
        }
        if (tokenOverlap < 0) {
            return "{\"errCod\":3,\"errDes\":\"El overlap no puede ser negativo\",\"Chunks\":[]}";
        }
        if (tokenChunkSize <= tokenOverlap) {
            return "{\"errCod\":4,\"errDes\":\"El tamaño del chunk (en tokens) debe ser mayor que el overlap (en tokens)\",\"Chunks\":[]}";
        }

        // Conversión de tokens a palabras usando la relación 1 palabra ≈ 1.2 tokens.
        double conversionFactor = 1.2;
        int wordChunkSize = (int) Math.ceil((double) tokenChunkSize / conversionFactor);
        int wordOverlap = (int) Math.ceil((double) tokenOverlap / conversionFactor);

        if (wordChunkSize <= wordOverlap) {
            return "{\"errCod\":5,\"errDes\":\"El tamaño del chunk (convertido a palabras) debe ser mayor que el overlap\",\"Chunks\":[]}";
        }

        // Separación del texto en tokens (palabras) usando espacios en blanco.
        String[] words = text.split("\\s+");

        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < words.length) {
            int end = Math.min(start + wordChunkSize, words.length);
            // Se unen los tokens (palabras) para formar el chunk.
            String chunk = String.join(" ", java.util.Arrays.copyOfRange(words, start, end));
            chunks.add(chunk);
            // Se avanza en la lista de palabras descontando el overlap.
            start += (wordChunkSize - wordOverlap);
        }

        // Construcción del JSON de respuesta.
        StringBuilder json = new StringBuilder();
        json.append("{\"errCod\":0,\"errDes\":\"Ok\",\"Chunks\":[");
        for (int i = 0; i < chunks.size(); i++) {
            json.append("\"").append(chunks.get(i)).append("\"");
            if (i < chunks.size() - 1) {
                json.append(",");
            }
        }
        json.append("]}");

        return json.toString();
    }

    // Método main para realizar pruebas.
    public static void main(String[] args) {
        // Ejemplo de uso:
        String texto = "La inteligencia artificial ha revolucionado múltiples industrias, desde la medicina hasta el entretenimiento. Los modelos de lenguaje como GPT permiten generar contenido, responder preguntas y asistir en tareas complejas. El uso de RAG mejora aún más estas capacidades al incorporar información externa relevante en tiempo real.";
        int tokenChunkSize = 30; // Tamaño del chunk en tokens.
        int tokenOverlap = 10;    // Overlap en tokens entre chunks.

        String resultado = createChunks(texto, tokenChunkSize, tokenOverlap);
        System.out.println(resultado);
    }
}