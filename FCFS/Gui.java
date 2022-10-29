
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.*;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Gui extends JFrame {

    // Atributos
    private ArrayList<Nodo> list = new ArrayList<>();
    JTable tb1;
    Object datos[][];
    Object columnas[];

    // Contructor de la gui
    public Gui() {

        int Hl, Ta;
        ArrayList<Nodo> Temp = new ArrayList<>();
        int Ps = Integer.parseInt(JOptionPane.showInputDialog("cuantos procesos quiere?"));
        int NumPro = Ps;

        // Pedir datos al usuario
        for (int ct = Ps + 1; Ps != 0; Ps--) {
            String msg = "Hora de llegada P" + (ct - Ps);
            Hl = Integer.parseInt(JOptionPane.showInputDialog(msg));
            msg = "Tiempo de servicio P" + (ct - Ps);
            Ta = Integer.parseInt(JOptionPane.showInputDialog(msg));
            Nodo n = new Nodo(Hl, Ta);
            n.inicioBloqueo = Integer.parseInt(JOptionPane.showInputDialog("En que momento inicia el bloqueo?"));
            n.duracionBloqueo = Integer.parseInt(JOptionPane.showInputDialog("Cuanto dura el bloqueo?"));
            Temp.add(n);
        }

        // Organizar procesos SEGUN EL ORDEN DE LLEGADA
        boolean on = true;
        ArrayList<Nodo> FCFS0 = new ArrayList<>();
        ArrayList<Nodo> FCFS = new ArrayList<>();

        while (on) {
            int mini = 1000000;
            if (!Temp.isEmpty()) {
                int cont = 0, pos = 0;
                for (Nodo nodo : Temp) {
                    if (nodo.getH_LI() <= mini) {
                        mini = nodo.getH_LI();
                        pos = cont++;
                    } else {
                        cont++;
                    }
                }
                FCFS0.add(Temp.remove(pos));
            } else {
                on = false;
            }
        }

        on = true;
        while (on) {
            int mini = 1000000;
            if (!FCFS0.isEmpty()) {
                int cont = 0, pos = 0;
                for (Nodo nodo : FCFS0) {
                    if (nodo.getH_LI() <= mini) {
                        mini = nodo.getH_LI();
                        pos = cont++;
                    } else {
                        cont++;
                    }
                }
                FCFS.add(FCFS0.remove(pos));
            } else {
                on = false;
            }
        }

        // Generar medidas generales para la impreción
        int TimF = 0, num = 1;
        for (int cont = 0; cont < FCFS.size(); cont++) {
            FCFS.get(cont).setNumP(num++);
            if (cont == 0) {
                TimF = FCFS.get(0).getH_LI() + TimF + FCFS.get(cont).getT_S();
            } else {
                TimF = TimF + FCFS.get(cont).getT_S();
            }
            FCFS.get(cont).setT_F(TimF);
            FCFS.get(cont).setT_R(TimF - FCFS.get(cont).getH_LI());
        }

        // Ajustar arraylists para las metricas
        FCFS0.clear();
        FCFS0.addAll(FCFS);
        for (Nodo n: FCFS){
            list.add(n.copiarNodo());
        }

        // Recolectar datos y metricas de cada proceso
        int ultimaEjecucion = 0;
        Color c;
        for (int i = 0; i < 40; i++) {
            boolean ejecutado = false, turno = false;
            for (Nodo nodo : FCFS0) {
                if (nodo.getH_LI() > i || nodo.estado == 4) {
                    continue;
                } else {
                    if (nodo.estado == 2 && nodo.getT_S() > 0) {
                        nodo.duracionBloqueo -= 1;
                        if (nodo.duracionBloqueo == 0) {
                            nodo.estado = 3;
                        }
                    } else if (nodo.getT_S() > 0 && nodo.estado != 4) {
                        if (!turno) {
                            if (ultimaEjecucion != 0) {
                                if (FCFS0.get(ultimaEjecucion - 1).getT_S() > 0 && FCFS0.get(ultimaEjecucion - 1).estado != 2) {
                                    if (FCFS0.get(ultimaEjecucion - 1).getNumP() == nodo.getNumP()) {
                                        ejecutado = false;
                                    } else {
                                        ejecutado = true;
                                    }
                                } else {
                                    ejecutado = false;
                                    ultimaEjecucion = 0;
                                }
                            } else {
                                ejecutado = false;
                            }
                        } else {
                            ejecutado = true;
                        }
                        if (ejecutado) {
                            c = Color.GRAY;
                            nodo.tiempoDeEspera += 1;
                        } else {
                            c = Color.GREEN;
                            ultimaEjecucion = nodo.getNumP();
                            turno = true;
                        }
                        if (c != Color.GRAY) {
                            nodo.setT_S(nodo.getT_S() - 1);
                            nodo.tiempoEjecucion += 1;
                        }
                        if (nodo.tiempoEjecucion == nodo.inicioBloqueo && nodo.duracionBloqueo != 0) {
                            nodo.estado = 2;
                        }
                        if (nodo.getT_S() == 0) {
                            nodo.estado = 4;
                            nodo.tiempoFinalizacion = i+1;
                        }
                    }
                }
            }
        }
        
        // Comenzar personalizacion de la tabla 
        setTitle("Algoritmo FCFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 450);

        // Contenido del JTable
        int Ncol = NumPro + 1;
        datos = new Object[9][Ncol];
        for (int fil = 0; fil < 9; fil++) {
            switch (fil) {
                case 0:
                    datos[fil][0] = "Hora de llegada: ";
                    break;
                case 1:
                    datos[fil][0] = "Tiempo servicio: ";
                    break;
                case 2:
                    datos[fil][0] = "Inicio de bloqueo: ";
                    break;
                case 3:
                    datos[fil][0] = "Duración de bloqueo: ";
                    break;
                case 4:
                    datos[fil][0] = "Tiempo de espera: ";
                    break;
                case 5:
                    datos[fil][0] = "Tiempo de finalización: ";
                    break;
                case 6:
                    datos[fil][0] = "Tiempo de retorno: ";
                    break;
                case 7:
                    datos[fil][0] = "Tiempo perdido: ";
                    break;
                case 8:
                    datos[fil][0] = "Penalidad: ";
                    break;
            }
            int con = 0;
            for (int col = 1; col < Ncol; col++, con++) {
                switch (fil) {
                    case 0:
                        datos[fil][col] = list.get(con).getH_LI();
                        break;
                    case 1:
                        datos[fil][col] = list.get(con).getT_S();
                        break;
                    case 2:
                        datos[fil][col] = list.get(con).inicioBloqueo;
                        break;
                    case 3:
                        datos[fil][col] = list.get(con).duracionBloqueo;
                        break;
                    case 4:
                        datos[fil][col] = FCFS0.get(con).tiempoDeEspera;
                        break;
                    case 5:
                        datos[fil][col] = FCFS0.get(con).tiempoFinalizacion;
                        break;
                    case 6:
                        datos[fil][col] = FCFS0.get(con).tiempoFinalizacion - FCFS.get(con).getH_LI();
                        break;
                    case 7:
                        datos[fil][col] = FCFS0.get(con).tiempoFinalizacion - FCFS.get(con).getH_LI() - list.get(con).getT_S();
                        break;
                    case 8:
                        datos[fil][col] = (FCFS0.get(con).tiempoFinalizacion - FCFS.get(con).getH_LI())/ (float) list.get(con).getT_S();
                        break;
                }
            }
        }

        // Agregar numeros de procesos segun la columna
        int in = 0;
        columnas = new Object[Ncol];
        for (int i = 0; i < Ncol; i++) {
            if (i == 0) {
                columnas[i] = "Numero de proceso:";
            } else {
                columnas[i] = FCFS.get(in).getNumP();
                in++;
            }
        }

        // Agregar tabla a la interfaz
        tb1 = new JTable(datos, columnas);
        JScrollPane panel = new JScrollPane(tb1);
        getContentPane().add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Colorear bloques de proceso
    public void paint(Graphics g) {
        super.paint(g);

        int sizelist = list.size();

        // Dibujar tabla
        int total = 40 * 22; /////////////////////////////////////// Numero de columnas
        g.drawLine(50, 249, (total + 50), 249);
        sizelist *= 22;
        g.drawLine(50, 250, 50, (sizelist + 250));

        // Dibujas los numeros de quantum
        int lim = 21;
        for (int ps = 0; total > 0; total -= 22, ps++) {
            String proc = Integer.toString(ps);
            if (ps == 0) {
                g.drawString(proc, 50, 248);
            } else {
                g.drawString(proc, (lim + 50), 248);
                lim += 22;
            }
        }
        lim = 22;
        for (int ps = 1; sizelist > 0; sizelist -= 22, ps++) {
            String proc = Integer.toString(ps);
            if (ps == 1) {
                g.drawString(proc, 41, 271);
            } else {
                g.drawString(proc, 41, (lim + 271));
                lim += 22;
            }
        }

        // Establecer color para cada proceso
        for (Nodo nodo : list) {
            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);

            nodo.color = new Color(R, G, B);
        }

        // Dibujar procesos
        int posY = 250, tfin = 0;
        int iniq, inip;

        int ultimaEjecucion = 0;

        for (int i = 0; i < 40; i++) {

            iniq = i * 22;

            boolean ejecutado = false, turno = false;

            for (Nodo nodo : list) {
                if (nodo.getNumP() == 1) {
                    inip = 0;
                } else {
                    inip = (nodo.getNumP() - 1) * 22;
                }

                int posX = 50;
                if (nodo.getH_LI() > i || nodo.estado == 4) {
                    continue;
                } else {

                    if (nodo.estado == 2 && nodo.getT_S() > 0) {
                        nodo.duracionBloqueo -= 1;
                        g.setColor(Color.BLACK);
                        g.fillRect((posX + iniq), (posY + inip), 20, 20);

                        if (nodo.duracionBloqueo == 0) {
                            nodo.estado = 3;
                        }

                    } else if (nodo.getT_S() > 0 && nodo.estado != 4) {

                        if (!turno) {
                            if (ultimaEjecucion != 0) {
                                if (list.get(ultimaEjecucion - 1).getT_S() > 0 && list.get(ultimaEjecucion - 1).estado != 2) {
                                    if (list.get(ultimaEjecucion - 1).getNumP() == nodo.getNumP()) {
                                        ejecutado = false;

                                    } else {
                                        ejecutado = true;
                                    }
                                } else {
                                    ejecutado = false;
                                    ultimaEjecucion = 0;
                                }

                            } else {
                                ejecutado = false;
                            }
                        } else {
                            ejecutado = true;
                        }

                        if (ejecutado) {
                            g.setColor(Color.GRAY);
                            nodo.tiempoDeEspera += 1;
                        } else {
                            g.setColor(nodo.color);
                            ultimaEjecucion = nodo.getNumP();
                            turno = true;
                        }

                        g.fillRect((posX + iniq), (posY + inip), 20, 20);
                        if (g.getColor() != Color.GRAY) {
                            nodo.setT_S(nodo.getT_S() - 1);
                            nodo.tiempoEjecucion += 1;
                        }

                        if (nodo.tiempoEjecucion == nodo.inicioBloqueo && nodo.duracionBloqueo != 0) {
                            nodo.estado = 2;
                        }
                        if (nodo.getT_S() == 0) {
                            nodo.estado = 4;
                            nodo.tiempoFinalizacion = i;
                        }
                    }
                }
            }
            // Simular un segundo de congelamiento
            detenerElTiempo();
        }
        // Evitar que la pantalla vuelva a ejecutar Paint()
        try {
            Thread.sleep(500000);
        } catch (Exception e) {}
    }

    public static void detenerElTiempo() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
    }

}
