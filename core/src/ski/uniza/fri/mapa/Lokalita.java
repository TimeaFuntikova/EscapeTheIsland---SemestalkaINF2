package ski.uniza.fri.mapa;
import ski.uniza.fri.predmety.IPredmet;
import java.util.HashMap;

/**
 * Trieda Lokalita predstavuje konkrétne miesto na mape. Skladá sa z vygenerovaných miest, ktoré sa v triede
 * priradia do zoznamu lokalít. Každá má ako parameter svoj popis.
 *
 * @author Timea Funtíková
 * @version 1.0 (20.4.2021)
 */
public class Lokalita implements IMapa {

    //------------------------------
    // Atribúty pre triedu Lokalita
    //------------------------------

    private String nazovLokality;
    private GeneratorLokalit generatorLokalit;
    private HashMap<String,IPredmet> predmetyVLokalite = new HashMap<>(); //pouziva sa?

    /**
     * (Loklita) Konštruktor ktorý si v sebe nainicializuje generator lokalit
     */
    public Lokalita(GeneratorLokalit generatorLokalit) {
        this("nema nazov", generatorLokalit);
        this.generatorLokalit = generatorLokalit;
    }

    /**
     * (Lokalita) Parametrický konštruktor, dá inštancám lokalít ich reťazcový názov.
     * @param nazov
     */
    public Lokalita(String nazov, GeneratorLokalit generatorLokalit) {
        this.nazovLokality = nazov;   //hasmapy sú inicializované pri deklaracii atributov;
        this.generatorLokalit = generatorLokalit;
    }

    public void initPredmetov() {
        this.predmetyVLokalite = this.generatorLokalit.dajPredmetyNaVykreslenie();
    }

    /**
     * (Lokalita) Getter na názov lokality.
     * @return
     */
    public String getNazovLokality() {
        return nazovLokality;
    }

    /**
     * Getter na zoznam všetych predmetov v lokalite.
     * @return Hashmap
     */
    public HashMap<String, IPredmet> getPredmetyVLokalite() {
        return predmetyVLokalite;
    }


    //-----------------------------------
    // Metódy implementované z interface.
    //-----------------------------------


    /**
     * (Lokalita) Odvolá sa na generatorLokalit, ktorý vygeneruje novú lokalitu zo svojho zoznamu lokalít.
     * @param paNovaLokalita
     */
    @Override
    public void zmenLokalitu(Lokalita paNovaLokalita){
        this.generatorLokalit.nastavAktualnuLokalitu(paNovaLokalita);
    }

    /**
     * (Lokalita) zavola metódu z triedy Ruksak, ktorá na základe toho, či je postava v aktuálnej miestnosti predmet
     * vezme a prida ho do ruksaka a zároveň ho vymaže z predmetov, ktoré sa nachádzajú v miestnosti.
     * @param predmet
     * @return
     */
    @Override
    public void vezmiPredmet(IPredmet predmet) {
        this.predmetyVLokalite.remove(predmet.dajNazov(), predmet);
    }

    /**
     * (Lokalita) Vracia noazov lokality v stringovej reprezentacii.
     * @return
     */
    @Override
    public String dajNazovLokality() {
        return this.nazovLokality;
    }

    /**
     * (Lokalita) Vracia lokalitu hraca
     * @return
     */
    @Override
    public Lokalita dajLokalituPostavy() {
        return this.generatorLokalit.getAktualnaLokalita();
    }

    /**
     * (Lokalita) Miestnosť sa naplní predmetami, ktoré v nej majú byť k dispozícii pre hráča.
     * @param predmet
     */
    @Override
    public void naplnMiestnost(IPredmet predmet) {
        this.predmetyVLokalite.put(predmet.dajNazov(), predmet);
    }
}
