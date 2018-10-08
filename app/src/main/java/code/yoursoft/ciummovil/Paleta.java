package code.yoursoft.ciummovil;


import android.widget.ImageView;

 class Paleta {
    private String titulo;
    private String subtitulo;
    private int intValue;

    public Paleta(String tituloP, String subtituloP, int intValue) {
        this.titulo = tituloP;
        this.subtitulo = subtituloP;
        this.intValue = intValue;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }


    public int getIntValue() {
        return intValue;
    }


}