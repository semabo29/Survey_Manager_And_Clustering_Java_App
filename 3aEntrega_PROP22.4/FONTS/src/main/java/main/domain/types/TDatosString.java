package main.domain.types;

import main.domain.exceptions.AmbosTextosSonVacios;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Representa un dato de tipo texto.
 * <p>
 * Implementa {@link TDatos}.
 * </p>
 * @author Javier Zhangpan
 */
public class TDatosString implements TDatos{

    String texto;
    private static final Set<String> palabrasNoFuncionales = new HashSet<>(Set.of(
            "a", "acá", "ahí", "al", "algo", "algún", "alguna", "algunas", "alguno", "algunos",
            "allá", "allí", "ante", "antes", "aquel", "aquella", "aquellas", "aquello", "aquellos",
            "aquí", "así", "aunque", "bajo", "bien", "cada", "casi", "como", "con", "contra",
            "cual", "cuales", "cualquier", "cualquiera", "cualesquiera", "cuan", "cuando", "cuanto",
            "cuantos", "de", "del", "desde", "donde", "dos", "el", "ella", "ellas", "ello", "ellos",
            "en", "entre", "era", "erais", "eran", "eras", "eres", "es", "esa", "esas", "ese", "eso",
            "esos", "esta", "estaba", "estaban", "estado", "estáis", "estamos", "están", "estar",
            "estas", "este", "esto", "estos", "estoy", "fin", "fue", "fueron", "fui", "fuimos",
            "ha", "habéis", "había", "habían", "haber", "habrás", "habrá", "habrán", "habremos",
            "habría", "habrían", "han", "has", "hasta", "hay", "haya", "he", "hemos", "hube",
            "hubo", "la", "las", "le", "les", "lo", "los", "mas", "me", "mi", "mis", "mía", "mías",
            "mientras", "mio", "míos", "misma", "mismo", "mismos", "muy", "nada", "ni", "no",
            "nos", "nosotras", "nosotros", "nuestra", "nuestras", "nuestro", "nuestros", "nunca",
            "o", "os", "otra", "otros", "para", "pero", "poco", "por", "porque", "que", "quien",
            "quienes", "se", "sea", "sean", "ser", "será", "serán", "si", "sido", "siempre",
            "sin", "sobre", "sois", "solamente", "solo", "somos", "son", "soy", "su", "sus",
            "suya", "suyas", "suyo", "suyos", "también", "tan", "tanto", "te", "tendrá",
            "tendrán", "tenemos", "tener", "tenía", "tenían", "ti", "tiene", "tienen", "toda",
            "todas", "todo", "todos", "tu", "tus", "tuyo", "tuyos", "un", "una", "uno", "unos",
            "vosotras", "vosotros", "vuestra", "vuestras", "vuestro", "vuestros", "ya", "yo"
    ));

    /**
     * Constructora por defecto.
     */
    public TDatosString() {
        this.texto = "";
    }

    /**
     * Constructora con texto.
     *
     * @param str texto
     */
    public TDatosString(String str) {
        this.texto = str;
    }

    /**
     * Devuelve el texto de la respuesta.
     * @return {@link String} con el texto de la respuesta
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Cambia el texto de la respuesta.
     * @param texto texto de la respuesta.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Calcula la distancia normalizada entre este texto y otro {@link TDatosString}.
     *
     * @param t otro dato
     * @return distancia normalizada entre los textos
     */
    @Override
    public Float calcularDistancia(TDatos t) {
        TDatosString aux = (TDatosString) t;
        return stringsDistancia(texto, aux.getTexto());
    }

    private static Float stringsDistancia(String strA, String strB) {
        float maxLength = Math.max(strA.length(), strB.length());

        if(maxLength == 0.0) throw new AmbosTextosSonVacios("Ambos strings son vacíos, lo que provoca una división por cero");

        return levDist(strA,strB) / maxLength;
    }

    //implementación con programación dinámica (tabulación)
    // O(n*m) de complejidad temporal y espacial
    private static int levDist(String strA, String strB) {
        ArrayList<ArrayList<Integer>> E = new ArrayList<>();
        int n = strA.length();
        int m = strB.length();

        //inicializar matriz
        for (int i = 0; i <= n; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j <= m; j++) {
                row.add(0);
            }
            E.add(row);
        }

        //casos base
        for(int i = 0; i <= n; ++i) E.get(i).set(0,i);
        for(int j = 0; j <= m; ++j) E.getFirst().set(j,j);

        //casos recursivos
        for(int i = 1; i <= n; ++i) {
            for(int j = 1; j <= m; ++j) {
                int delta = (strA.charAt(i-1) != strB.charAt(j-1)) ? 1 : 0;

                int insert = E.get(i).get(j - 1) + 1;
                int delete = E.get(i - 1).get(j) + 1;
                int replace = E.get(i - 1).get(j - 1) + delta;

                E.get(i).set(j, Math.min(insert, Math.min(delete, replace)));
            }
        }

        return E.get(n).get(m);
    }

    /**
     * Calcula el componente de tipo {@link TDatosString} de centroide de un cluster.
     *
     * @param datos conjunto de datos del cluster de tipo {@link TDatosString}
     * @return nuevo {@link TDatosString} representando la media (palabras más frecuentes)
     */
    @Override
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos) {
        Map<String, Integer> diccionario = new TreeMap<>();
        for(TDatos d : datos) {
            TDatosString dato = (TDatosString) d;
            String[] words = procesarTexto(dato.getTexto());

            for(String word : words) {
                if (word.isBlank()) continue;
                if(!palabrasNoFuncionales.contains(word)) {
                    diccionario.put(word, diccionario.getOrDefault(word, 0) + 1);
                }
            }
        }

        int max = 0;
        for(Map.Entry<String,Integer> entry : diccionario.entrySet()) {
            if(entry.getValue() > max) {
                max = entry.getValue();
            }
        }

        ArrayList<String> strs = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : diccionario.entrySet()) {
            if(entry.getValue() == max) {
                strs.add(entry.getKey());
            }
        }

        String str = String.join(" ", strs);

        return new TDatosString(str);
    }

    public static String[] procesarTexto(String text) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (Character.isLetter(c)) {
                sb.append(c);
            } else {
                if (!sb.isEmpty()) {
                    out.add(sb.toString());
                    sb.setLength(0);
                }
            }
        }
        if (!sb.isEmpty()) out.add(sb.toString());

        out.replaceAll(src -> Normalizer.normalize(src, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase());

        return out.toArray(new String[0]);
    }

    /**
     * Comprueba si otro {@link TDatosString} tiene el mismo texto.
     * La función asume que pertenecen a la misma pregunta.
     *
     * @return {@code true} si tienen el mismo texto,
     * {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TDatosString other = (TDatosString) obj;

        return  texto.equals(other.texto);
    }

    /**
     * Devuelve el texto del dato.
     *
     * @return contenido del dato
     */
    @Override
    public String getContenido() {
        return texto;
    }
}
