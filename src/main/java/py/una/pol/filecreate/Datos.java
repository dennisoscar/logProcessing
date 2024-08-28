package py.una.pol.filecreate;

public class Datos {
    // Atributos (variables de instancia)
    String generation;
    String fitness;
    String bloqueoPromedio;
    Double averageSur;
    String chromosome;
    Double standarDesviation;
    String tiempoSimulacion;
    String filaLog;

    // Constructor
    public Datos(String generation, String fitness, String bloqueoPromedio, Double averageSur, String chromosome, Double standarDesviation, String tiempoSimulacion) {
        this.generation = generation;
        this.fitness = fitness;
        this.bloqueoPromedio = bloqueoPromedio;
        this.averageSur = averageSur;
        this.chromosome = chromosome;
        this.standarDesviation = standarDesviation;
        this.tiempoSimulacion = tiempoSimulacion;
        this.filaLog = filaLog;
    }

    public Datos() {

    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getFitness() {
        return fitness;
    }

    public void setFitness(String fitness) {
        this.fitness = fitness;
    }

    public String getBloqueoPromedio() {
        return bloqueoPromedio;
    }

    public void setBloqueoPromedio(String bloqueoPromedio) {
        this.bloqueoPromedio = bloqueoPromedio;
    }

    public Double getAverageSur() {
        return averageSur;
    }

    public void setAverageSur(Double averageSur) {
        this.averageSur = averageSur;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Double getStandarDesviation() {
        return standarDesviation;
    }

    public void setStandarDesviation(Double standarDesviation) {
        this.standarDesviation = standarDesviation;
    }


    public String getTiempoSimulacion() {
        return tiempoSimulacion;
    }

    public void setTiempoSimulacion(String tiempoSimulacion) {
        this.tiempoSimulacion = tiempoSimulacion;
    }

    public String getFilaLog() {
        return filaLog;
    }

    public void setFilaLog(String filaLog) {
        this.filaLog = filaLog;
    }

    // Método (función)
    public void saludar() {
        System.out.println("Hola, mi generación es " + generation);
    }
}
