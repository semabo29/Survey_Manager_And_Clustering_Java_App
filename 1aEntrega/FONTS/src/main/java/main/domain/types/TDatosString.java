package main.domain.types;

import main.domain.exceptions.AmbosTextosSonVacios;
import java.text.Normalizer;
import java.util.*;

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

    public TDatosString() {
        this.texto = "";
    }
    public TDatosString(String str) {
        this.texto = str;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public Float calcularDistancia(TDatos t) {
        TDatosString aux = (TDatosString) t;
        return stringsDistancia(texto, aux.getTexto());
    }

    private static Float stringsDistancia(String strA, String strB) {
        float diffLength = Math.abs(strA.length()- strB.length());
        float maxLength = Math.max(strA.length(), strB.length());

        if(diffLength == 0.0 && maxLength == 0.0) throw new AmbosTextosSonVacios("Ambos strings son vacíos, lo que provoca una división por cero");

        return (levDist(strA,strB) - diffLength) / (maxLength - diffLength);
    }

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

    @Override
    public TDatos getComponenteCentroide(ArrayList<TDatos> datos) {
        HashMap<String, Integer> diccionario = new HashMap<>();
        for(TDatos d : datos) {
            TDatosString dato = (TDatosString) d;
            String[] words = Arrays.stream(
                            dato.getTexto()
                                    .replaceAll(
                                            "(?<![\\deE])[\\p{Punct}&&[^.,eE+-]]+|(?<!\\d)[.,]|[.,](?!\\d)|(?<!\\d),(?=\\d{1,3}(?:\\D|$))|(?<![eE])[+-](?!\\d)",
                                            ""
                                    )
                                    .split("\\s+")
                    )
                    .map(w -> Normalizer.normalize(w, Normalizer.Form.NFD))
                    .map(w -> w.replaceAll("\\p{M}", ""))
                    .map(String::toLowerCase)
                    .toArray(String[]::new);

            for(String word : words) {
                if(!palabrasNoFuncionales.contains(word)) {
                    if(diccionario.containsKey(word)) diccionario.replace(word, diccionario.get(word), diccionario.get(word)+1);
                    else diccionario.put(word,1);
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
}
