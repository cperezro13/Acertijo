package acertijo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Acertijo extends JPanel {

    private final int gridSize = 12;
    private final int cellSize = 60;
    private final Map<Point, Color> nodos;
    private final ArrayList<Linea> lineas;
    private Point nodoSeleccionado = null;
    private Color colorSeleccionado = null;
    private ArrayList<Point> caminoActual = new ArrayList<>();

    public Acertijo() {
        nodos = new HashMap<>();
        lineas = new ArrayList<>();
        generarNodos();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickedPoint = new Point(e.getX() / cellSize, e.getY() / cellSize);
                if (nodos.containsKey(clickedPoint)) {
                    if (lineaExistente(clickedPoint)) {
                        eliminarLinea(clickedPoint);
                    } else {
                        nodoSeleccionado = clickedPoint;
                        colorSeleccionado = nodos.get(clickedPoint);
                        caminoActual.clear();
                        caminoActual.add(nodoSeleccionado);
                    }
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (caminoActual.size() > 1 && nodos.containsKey(caminoActual.get(caminoActual.size() - 1)) && nodos.get(caminoActual.get(caminoActual.size() - 1)).equals(colorSeleccionado)) {
                    lineas.add(new Linea(new ArrayList<>(caminoActual), colorSeleccionado));
                }
                nodoSeleccionado = null;
                colorSeleccionado = null;
                caminoActual.clear();
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (nodoSeleccionado != null) {
                    Point newPoint = new Point(e.getX() / cellSize, e.getY() / cellSize);
                    if (esPuntoDentroDeLaCuadricula(newPoint) && !caminoActual.contains(newPoint) && esMovimientoValido(caminoActual.get(caminoActual.size() - 1), newPoint) && !puntoOcupado(newPoint)) {
                        caminoActual.add(newPoint);
                        repaint();
                    }
                }
            }
        });
    }

    private boolean esMovimientoValido(Point a, Point b) {
        return (a.x == b.x || a.y == b.y) && (Math.abs(a.x - b.x) == 1 || Math.abs(a.y - b.y) == 1);
    }

    private boolean esPuntoDentroDeLaCuadricula(Point p) {
        return p.x >= 0 && p.x < gridSize && p.y >= 0 && p.y < gridSize;
    }

    private boolean puntoOcupado(Point p) {
        for (Linea l : lineas) {
            if (l.puntos.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private boolean lineaExistente(Point p) {
        for (Linea l : lineas) {
            if (l.puntos.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private void eliminarLinea(Point p) {
        lineas.removeIf(l -> l.puntos.contains(p));
    }

    private void generarNodos() {
        nodos.put(new Point(2, 2), Color.RED);
        nodos.put(new Point(9, 11), Color.RED);
        nodos.put(new Point(5, 6), Color.BLUE);
        nodos.put(new Point(9, 9), Color.BLUE);
        nodos.put(new Point(11, 5), Color.GREEN);
        nodos.put(new Point(7, 7), Color.GREEN);
        nodos.put(new Point(7, 1), Color.ORANGE.darker());
        nodos.put(new Point(10, 10), Color.ORANGE.darker());
        nodos.put(new Point(9, 2), Color.MAGENTA);
        nodos.put(new Point(10, 7), Color.MAGENTA);
        nodos.put(new Point(8, 9), Color.YELLOW);
        nodos.put(new Point(10, 1), Color.YELLOW);
        nodos.put(new Point(10, 9), Color.CYAN);
        nodos.put(new Point(10, 11), Color.CYAN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= gridSize; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, gridSize * cellSize);
            g.drawLine(0, i * cellSize, gridSize * cellSize, i * cellSize);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));

        for (Point nodo : nodos.keySet()) {
            g2.setColor(nodos.get(nodo));
            g2.fillOval(nodo.x * cellSize + 15, nodo.y * cellSize + 15, 30, 30);
        }

        for (Linea l : lineas) {
            g2.setColor(l.color);
            for (int i = 0; i < l.puntos.size() - 1; i++) {
                Point p1 = l.puntos.get(i);
                Point p2 = l.puntos.get(i + 1);
                g2.drawLine(p1.x * cellSize + 30, p1.y * cellSize + 30, p2.x * cellSize + 30, p2.y * cellSize + 30);
            }
        }

        if (!caminoActual.isEmpty()) {
            g2.setColor(colorSeleccionado);
            for (int i = 0; i < caminoActual.size() - 1; i++) {
                Point p1 = caminoActual.get(i);
                Point p2 = caminoActual.get(i + 1);
                g2.drawLine(p1.x * cellSize + 30, p1.y * cellSize + 30, p2.x * cellSize + 30, p2.y * cellSize + 30);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Conecta los puntos :3");
        Acertijo game = new Acertijo();
        frame.add(game);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class Linea {

        ArrayList<Point> puntos;
        Color color;

        Linea(ArrayList<Point> puntos, Color color) {
            this.puntos = puntos;
            this.color = color;
        }
    }
}
