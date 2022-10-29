
import java.awt.Color;


public class Nodo{
    
    // Datos basicos
    private int NumP;
    private int H_LI;
    private int T_S;
    
    // Pintura del algoritmo
    int tiempoEjecucion = 0;
    int inicioBloqueo = 0;
    int duracionBloqueo = 0;
    Color color;
    
    // Medidas y otros
    private int T_F;
    private int T_R;
    private float T_NR;
    
    // Estadisticas
    int tiempoDeEspera = 0;
    int tiempoFinalizacion;
    int tiempoRetorno;
    int tiempoPerdido;
    int penalidad;
    
    int estado = 0;
    // 0 = No hallegado, 1 = en espera, 2 = bloqueado, 3 = listo, 4 = finalizado

    // Constructor para clonacion
    public Nodo(Nodo n){
        this.H_LI = n.H_LI;
        this.T_S = n.T_S;
        this.NumP = n.getNumP();
        this.duracionBloqueo = n.duracionBloqueo;
        this.inicioBloqueo = n.inicioBloqueo;
        this.T_F = n.getT_F();
        this.T_R = n.getT_R();;
        this.T_NR = n.getT_NR();;
    }
    
    // Clonación de nodo
    public Nodo copiarNodo(){
        return new Nodo(this);
    }
    
    public Nodo (int a, int b){
        H_LI = a;
        T_S = b;
    }

    public void setH_LI(int H_LI) {
        this.H_LI = H_LI;
    }

    public void setNumP(int NumP) {
        this.NumP = NumP;
    }

    public void setT_F(int T_F) {
        this.T_F = T_F;
    }

    public void setT_NR(float T_NR) {
        this.T_NR = T_NR;
    }

    public void setT_R(int T_R) {
        this.T_R = T_R;
    }

    public void setT_S(int T_S) {
        if (T_R == 0){
            estado = 4;
        }
        this.T_S = T_S;
    }

    public int getH_LI() {
        return H_LI;
    }

    public int getNumP() {
        return NumP;
    }

    public int getT_F() {
        return T_F;
    }

    public float getT_NR() {
        return T_NR;
    }

    public int getT_R() {
        return T_R;
    }

    public int getT_S() {
        return T_S;
    }
    
}
