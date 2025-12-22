package main.presentation;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Menú dedicado a mostrar los resultados del clustering
 * <p>
 * Esta clase permite visualizar mediante un gráfico o una tabla las asignaciones de todas las respuestas
 * </p>
 * También permite visualizar los resultados para clusters individuales. Cabe destacar que las gráficas
 * entre diferentes clusters no son comparables, porque están escaladas, pero sirven para adquirir una idea
 * de cómo se distribuyen relativamente las respuestas en un mismo cluster.
 * @author Javier Zhangpan
 */
public class PanelResultadoAnalisis extends JPanel {
    private final ArrayList<ArrayList<ArrayList<Float>>> distanciasPorCluster;
    private final ArrayList<ArrayList<Float>> distanciasGlobal;
    private final ArrayList<ArrayList<String>> nombres;
    private final int clusters;
    private final float eval;

    private GraficoTable estadoPanelCentral;

    private CardLayout cardLayout;
    private JPanel panelCentral;

    private JButton botonGraficos;
    private JButton botonTabla;
    private JComboBox<String> seleccionarCluster;

    private JTable tabla;

    private PanelGrafico panelGraficos;

    private static final String CARD_GRAFICOS = "gráficos";
    private static final String CARD_TABLA = "tabla";
    private static final String TODOS_CLUSTERS = "Todos";
    private static final String EVAL_TEXTO = "Evaluación del clustering: ";
    private static final String TEXTO_BOTON_GRAFICO = "Gráfico";
    private static final String TEXTO_BOTON_TABLA = "Tabla";
    private static final String TEXTO_COMBOBOX_CLUSTER = "Cluster";
    private static final String TEXTO_CLUSTERS = "clusters ";

    private static final String COL_RESPUESTA = "respuesta ";
    private static final String COL_CLUSTER = "cluster ";

    private static final int STRUT = 10;

    private record PanelComboBox<T>(JPanel panel, JComboBox<T> combo) {}
    private enum GraficoTable {GRAFICO, TABLA}
    private record Punto(float x, float y, int cluster) {}


    /**
     * Constructora por parámetros que construye el panel y
     * le asigna la información suficiente para crear los gráficos y tablas.
     *
     * @param distsClusters distancias por cluster
     * @param distsGlobal distancias globales
     * @param ns emails de cada respuesta agrupados por cluster
     * @param k número de clusters
     * @param e valor de evaluación
     */
    public PanelResultadoAnalisis(ArrayList<ArrayList<ArrayList<Float>>> distsClusters,
                                  ArrayList<ArrayList<Float>> distsGlobal,
                                  ArrayList<ArrayList<String>> ns, int k, float e) {
        this.distanciasPorCluster = distsClusters;
        this.distanciasGlobal = distsGlobal;
        this.nombres = ns;
        this.clusters = k;
        this.eval = e;

        setLayout(new BorderLayout(10, 10));

        crearPanelControl();
        crearPanelCentral();
        asignarListeners();

        estadoPanelCentral = GraficoTable.GRAFICO;
        actualizarGrafico(TODOS_CLUSTERS);
        seleccionarCluster.setSelectedItem(TODOS_CLUSTERS);
        cardLayout.show(panelCentral, CARD_GRAFICOS);
    }

    private void crearPanelControl() {
        JPanel control = new JPanel(new GridLayout(1, 3, STRUT, STRUT));

        botonGraficos = new JButton(TEXTO_BOTON_GRAFICO);
        botonTabla = new JButton(TEXTO_BOTON_TABLA);

        control.add(botonGraficos);
        control.add(botonTabla);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int i = 0; i < clusters; i++) {
            model.addElement(String.valueOf(i));
        }
        model.addElement(TODOS_CLUSTERS);

        PanelComboBox<String> combo =
                crearPanelConComboBox(TEXTO_COMBOBOX_CLUSTER, model);
        seleccionarCluster = combo.combo;

        control.add(combo.panel);

        add(control, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        panelGraficos = new PanelGrafico();
        JScrollPane scrollTabla = crearTabla();

        panelCentral.add(panelGraficos, CARD_GRAFICOS);
        panelCentral.add(scrollTabla, CARD_TABLA);

        panel.add(panelCentral);

        JPanel header = new JPanel();
        JLabel evalLabel = new JLabel(EVAL_TEXTO + eval);
        evalLabel.setBorder(BorderFactory.createEmptyBorder(STRUT, STRUT, STRUT, STRUT));
        evalLabel.setFont(evalLabel.getFont().deriveFont(Font.BOLD));
        header.add(evalLabel);

        panel.add(header);
        panel.add(panelCentral);

        add(panel, BorderLayout.CENTER);
    }

    private JScrollPane crearTabla() {
        Vector<String> cols = new Vector<>();
        cols.add(COL_RESPUESTA);
        cols.add(COL_CLUSTER);
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            //tabla no editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(model);
        return new JScrollPane(tabla);
    }

    private void asignarListeners() {
        asignaListenerBotonGraficos();
        asignaListenerBotonTabla();
        asignaListenerSeleccionarCluster();
    }

    private void asignaListenerBotonGraficos() {
        botonGraficos.addActionListener(e -> {
            if(estadoPanelCentral == GraficoTable.GRAFICO) return;

            estadoPanelCentral = GraficoTable.GRAFICO;

            String cluster = (String) seleccionarCluster.getSelectedItem();
            if(cluster != null) actualizarGrafico(cluster);

            cardLayout.show(panelCentral, CARD_GRAFICOS);
        });
    }

    private void asignaListenerBotonTabla() {
        botonTabla.addActionListener(e -> {
            if(estadoPanelCentral == GraficoTable.TABLA) return;

            estadoPanelCentral = GraficoTable.TABLA;

            String cluster = (String) seleccionarCluster.getSelectedItem();
            if(cluster != null) actualizarTabla(cluster);

            cardLayout.show(panelCentral, CARD_TABLA);
        });
    }

    private void asignaListenerSeleccionarCluster() {
        seleccionarCluster.addActionListener(e -> {
            String cluster = (String) seleccionarCluster.getSelectedItem();
            if (cluster != null) {
                actualizarPanelCentral(cluster);
            }
        });
    }

    private void actualizarPanelCentral(String cluster) {
        switch (estadoPanelCentral) {
            case TABLA -> actualizarTabla(cluster);
            case GRAFICO -> actualizarGrafico(cluster);
        }
    }

    private void actualizarTabla(String cluster) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        //quitar entradas previas
        model.setRowCount(0);
        if(cluster.equals(TODOS_CLUSTERS)) {
            for(int i = 0; i < nombres.size(); ++i) {
                ArrayList<String> clusterNombres = nombres.get(i);
                for(String nombre : clusterNombres) {
                    model.addRow(new Object[]{nombre, i});
                }
            }
        }
        else {
            ArrayList<String> clusterNombres = nombres.get(Integer.parseInt(cluster));
            for(String nombre : clusterNombres) {
                model.addRow(new Object[]{nombre, cluster});
            }
        }
    }

    private void actualizarGrafico(String cluster) {
        if(cluster.equals(TODOS_CLUSTERS)) {
            ArrayList<Punto> puntos = getPuntosGlobal();
            panelGraficos.setPuntos(puntos, clusters);
        }
        else {
            ArrayList<ArrayList<Float>> puntos;
            int cl = Integer.parseInt(cluster);
            puntos = MDSClasico(distanciasPorCluster.get(cl), nombres.get(cl).size());
            panelGraficos.setPuntos(puntos, cl, clusters);
        }
    }

    private ArrayList<Punto> getPuntosGlobal() {
        ArrayList<Punto> puntos = new ArrayList<>();
        int offset = 0;

        ArrayList<ArrayList<Float>> coordenadas = getMDSGlobal();

        for (int c = 0; c < clusters; c++) {
            ArrayList<String> clusterNames = nombres.get(c);

            for (int i = 0; i < clusterNames.size(); i++) {
                puntos.add(new Punto(coordenadas.get(offset + i).get(0), coordenadas.get(offset + i).get(1), c));
            }

            offset += clusterNames.size();
        }
        return puntos;
    }

    //crea un panel con un label "texto" y un combo box debajo con "opciones"
    private <T> PanelComboBox<T> crearPanelConComboBox(String texto, DefaultComboBoxModel<T> opciones) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(STRUT, 0, STRUT, 0));

        JLabel label = new JLabel(texto);
        panel.add(label);

        JComboBox<T> comboBox = new JComboBox<>(opciones);
        panel.add(comboBox);

        return new PanelComboBox<>(panel, comboBox);
    }

    private ArrayList<ArrayList<Float>> getMDSGlobal() {
        return MDSClasico(distanciasGlobal, getNumeroElementos(nombres));
    }

    private ArrayList<ArrayList<Float>> MDSClasico(ArrayList<ArrayList<Float>> distancias, int n) {
        if(n < 2) return origen();

        //computar matriz de distancias cuadradas
        Matrix distanciasCuadradas = new Matrix(aPrimitivo(distancias));
        distanciasCuadradas.arrayTimesEquals(distanciasCuadradas);

        //computar matriz centro
        Matrix centro = matrizCentro(n);

        //aplicar double centering: doubleCentro = (-1/2)*centro*distanciasCuadradas*centro
        Matrix dobleCentro = centro.times(-0.5).times(distanciasCuadradas).times(centro);

        //eigenvalue decomposition de dobleCentro
        //eigenValueMatrix debería ser 2x2 con los 2 eigenvalues más grandes
        //eigenVectorMatrix debería ser 2x2 con los 2 eigenvectors asociados más grandes
        EigenvalueDecomposition decomposition = dobleCentro.eig();
        Matrix eigenValueMatrix  = decomposition.getD();
        Matrix eigenVectorMatrix = decomposition.getV();

        ArrayList<Integer> indices = buscarIndicesEigenValue(eigenValueMatrix);
        eigenValueMatrix = eigenValueMatrix2(eigenValueMatrix, indices);
        eigenVectorMatrix = eigenVectorMatrix2(eigenVectorMatrix, indices);

        //calcular matriz de output
        Matrix outMat = eigenVectorMatrix.times(sqrtElements(eigenValueMatrix));

        return toArrayFloat(outMat);
    }

    private ArrayList<ArrayList<Float>> origen() {
        ArrayList<ArrayList<Float>> origen = new ArrayList<>();
        ArrayList<Float> p = new ArrayList<>();
        p.add(0f);
        p.add(0f);
        origen.add(p);
        return origen;
    }

    private Matrix matrizCentro(int n) {
        //ecuación Centro = Id - (1/n * Uno)
        Matrix Id = Matrix.identity(n,n);
        Matrix Uno = new Matrix(n,n,1.0);
        return Id.minus(Uno.times((double)1/n));
    }

    private static int getNumeroElementos(ArrayList<ArrayList<String>> nombres) {
        if(nombres.isEmpty()) return 0;
        int sum = 0;
        for(ArrayList<String> fila : nombres) {
            for(int i = 0; i < fila.size(); ++i) {
                ++sum;
            }
        }
        return sum;
    }

    private static double[][] aPrimitivo(ArrayList<ArrayList<Float>> list) {
        double[][] result = new double[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Float> row = list.get(i);
            result[i] = new double[row.size()];
            for (int j = 0; j < row.size(); j++) {
                result[i][j] = row.get(j);
            }
        }
        return result;
    }

    private static Matrix sqrtElements(Matrix M) {
        int r = M.getRowDimension();
        int c = M.getColumnDimension();
        Matrix R = new Matrix(r, c);
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                double v = Math.max(0, M.get(i, j));
                R.set(i, j, Math.sqrt(v));
            }
        }
        return R;
    }

    private static ArrayList<ArrayList<Float>> toArrayFloat(Matrix M) {
        int r = M.getRowDimension();
        int c = M.getColumnDimension();
        ArrayList<ArrayList<Float>> output = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            ArrayList<Float> fila = new ArrayList<>();
            for (int j = 0; j < c; j++) {
                fila.add((float) M.get(i,j));
            }
            output.add(fila);
        }
        return output;
    }

    private static ArrayList<Integer> buscarIndicesEigenValue(Matrix D) {
        int n = D.getRowDimension();

        int max1 = 0, max2 = 1;
        if (D.get(1,1) > D.get(0,0)) {
            max1 = 1; max2 = 0;
        }

        for (int i = 2; i < n; i++) {
            double v = D.get(i, i);

            if (v > D.get(max1, max1)) {
                max2 = max1;
                max1 = i;
            } else if (v > D.get(max2, max2)) {
                max2 = i;
            }
        }
        ArrayList<Integer> result = new ArrayList<>();
        result.add(max1);
        result.add(max2);
        return result;
    }

    private static Matrix eigenValueMatrix2(Matrix D, ArrayList<Integer> idx) {
        Matrix Lambda = new Matrix(2, 2);
        Lambda.set(0, 0, D.get(idx.get(0), idx.get(0)));
        Lambda.set(1, 1, D.get(idx.get(1), idx.get(1)));
        return Lambda;
    }

    private static Matrix eigenVectorMatrix2(Matrix V, ArrayList<Integer> idx) {
        int n = V.getRowDimension();
        Matrix Vk = new Matrix(n, 2);

        Vk.setMatrix(0, n - 1, 0, 0,
                V.getMatrix(0, n - 1, idx.get(0), idx.get(0)));

        Vk.setMatrix(0, n - 1, 1, 1,
                V.getMatrix(0, n - 1, idx.get(1), idx.get(1)));

        return Vk;
    }

    private static class PanelGrafico extends JPanel {
        private ArrayList<Punto> puntos = new ArrayList<>();
        private int numeroClusters = 1;
        private static final int LEYENDA_BOX = 12;
        private static final int LEYENDA_GAP = 6;
        private static final int LEYENDA_WIDTH = 120;
        private static final int LEYENDA_TOP_GAP = 60;
        private static final int LEYENDA_RIGHT_GAP = 10;

        void setPuntos(ArrayList<ArrayList<Float>> coordenadas, int cluster, int k) {
            numeroClusters = k;
            puntos.clear();
            for (ArrayList<Float> p : coordenadas) {
                puntos.add(new Punto(p.get(0), p.get(1), cluster));
            }
            repaint();
        }

        public void setPuntos(ArrayList<Punto> puntos, int k) {
            numeroClusters = k;
            this.puntos = puntos;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (puntos == null || puntos.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            //dejar espacio para la leyenda
            int w = getWidth() - LEYENDA_WIDTH;
            int h = getHeight();
            int padding = 40;

            // encontrar valores límite para luego escalar
            float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
            float minY = Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
            for (Punto p : puntos) {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y);
                maxY = Math.max(maxY, p.y);
            }

            // dibujar ejes
            g2.setColor(Color.BLACK);
            g2.drawLine(padding, h - padding, w - padding, h - padding);
            g2.drawLine(padding, padding, padding, h - padding);

            float dx = (maxX - minX == 0) ? 1 : (maxX - minX);
            float dy = (maxY - minY == 0) ? 1 : (maxY - minY);

            // dibujar puntos
            for (Punto p : puntos) {
                Color c = Color.getHSBColor((float) p.cluster / numeroClusters, 0.8f, 0.9f);
                g2.setColor(c);

                int x = (int) (padding + (p.x - minX) / (dx) * (w - 2 * padding));
                int y = (int) (h - padding - (p.y - minY) / (dy) * (h - 2 * padding));
                g2.fillOval(x - 4, y - 4, 8, 8);
            }

            dibujarLeyenda(g2);
        }

        private void dibujarLeyenda(Graphics2D g2) {
            //usar espacio reservado
            int x = getWidth() - LEYENDA_WIDTH + LEYENDA_RIGHT_GAP;
            int y = LEYENDA_TOP_GAP;

            g2.setColor(Color.BLACK);
            g2.drawString(TEXTO_CLUSTERS, x, y - 5);

            for (int c = 0; c < numeroClusters; c++) {
                //genera color en base a cluster
                Color color = Color.getHSBColor(
                        (float) c / numeroClusters, 0.8f, 0.9f);

                int yy = (y+LEYENDA_GAP) + c * (LEYENDA_BOX + LEYENDA_GAP);

                g2.setColor(color);
                g2.fillRect(x, yy, LEYENDA_BOX, LEYENDA_BOX);

                g2.setColor(Color.BLACK);
                g2.drawRect(x, yy, LEYENDA_BOX, LEYENDA_BOX);
                g2.drawString(COL_CLUSTER + c, x + LEYENDA_BOX + 6, yy + LEYENDA_BOX - 2);
            }
        }
    }
}