package ski.uniza.fri.hra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ski.uniza.fri.mapa.GeneratorLokalit;
import ski.uniza.fri.mapa.Lokalita;
import ski.uniza.fri.predmety.IPredmet;
import ski.uniza.fri.vykreslovace.VykreslovacPozadiLokalit;
import ski.uniza.fri.vykreslovace.VykreslovacPredmetov;

import java.util.HashMap;
import java.util.Set;

/**
 * Trieda Ovladanie predstavuje v podstate zbierku príkazov, vďaka ktorým je možné hru a postavu ovládať.
 * Obsahuje metódy, ktoré kontrolujú, či postava v hre dosiahla okraj obrazovky a pokial ano, tak prepne lokalitu
 * na inú, v ktorej sa bude dať opat pohybovať a zbierať predmety na vytvorenie lode.
 *
 * @author Timea Funtíková
 * @version 1.0 (16.5.2021)
 */
public class Ovladanie implements IOvladanie {

    //--------------------------------------
    // Atribúty pre triedu OvladaniePostavy
    //--------------------------------------

    private GeneratorLokalit generatorLokalit;
    private VykreslovacPozadiLokalit vykreslovacPozadiLokalit;
    private Postava postava;
    private SpriteBatch batch;
    private Sprite[] sprites;
    private VykreslovacPredmetov vykreslovacPredmetov;
    private boolean hybeSa = false;

    private boolean hitVpravo = false;
    private boolean hitVlavo = false;
    private boolean hitHore = false;
    private boolean hitDole = false;
    private int spriteIndex = 0;

    /**
     * (Ovladanie) Končtruktor triedy, kde sa inicializujú dané parametre.
     *
     * @param postava
     * @param batch
     * @param vykreslovacPredmetov
     * @param generatorLokalit
     * @param vykreslovacPozadiLokalit
     */
    public Ovladanie(Postava postava, SpriteBatch batch, VykreslovacPredmetov vykreslovacPredmetov, GeneratorLokalit generatorLokalit, VykreslovacPozadiLokalit vykreslovacPozadiLokalit) {
        this.postava = postava;
        this.batch = batch;
        this.vykreslovacPredmetov = vykreslovacPredmetov;
        this.generatorLokalit = generatorLokalit;
        this.vykreslovacPozadiLokalit = vykreslovacPozadiLokalit;
        this.sprites = this.vykreslovacPredmetov.getPlayerMovingDown();
    }

    /**
     * (Ovladanie) Bezarametricky konstruktor triedy OvladaniePostavy.
     */
    public Ovladanie() {
    }

    /**
     * (Ovladanie) Metódy na vykonavanie pohybu. -----------REFAKTORING KOLIZII a HITOV ____________________
     */
    @Override
    public void nastavOvladaniePostavy() {
        this.nastavPohnutieHore();
        this.nastavPohnutieDole();
        this.nastavPohnutieDoprava();
        this.nastavPohnutieDolava();
        this.postava.nastalaKolizia();
    }


    private void nastavPohnutieDolava() {
        if (!this.hybeSa && (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))) {
            this.hybeSa = true;
            if (this.postava.getX() - this.vykreslovacPredmetov.getPlayerTexture().getWidth() <= 0) {
                this.hitVlavo = true;
                this.zmenLokalitu(this.vykreslovacPozadiLokalit);
                this.hitVlavo = false;
            } else {
                this.postava.nastavPoziciuHraca(this.postava.getX() - 10, this.postava.getY());
            }
            this.batch.draw(this.vykreslovacPredmetov.getPlayerTexture(), (float) this.postava.getX(), (float) this.postava.getY());
            this.hybeSa = false;
            System.out.println("----------posun dolava sa vydaril.");
        }
    }

    private void nastavPohnutieDoprava() {
        if (!this.hybeSa && (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))) {
            // Gdx.input.setInputProcessor(new InputAdapter() {});
            this.hybeSa = true;
            if (this.postava.getX() > Gdx.graphics.getWidth() - this.vykreslovacPredmetov.getPlayerTexture().getWidth()) {
                this.hitVpravo = true;
                this.zmenLokalitu(this.vykreslovacPozadiLokalit);
                this.hitVpravo = false;
            } else {
                this.sprites = this.vykreslovacPredmetov.getPlayerMovingRight();
                this.postava.nastavPoziciuHraca(this.postava.getX() + 10, this.postava.getY());
            }
            this.batch.draw(this.vykreslovacPredmetov.getPlayerTexture(), (float) this.postava.getX(), (float) this.postava.getY());
            this.hybeSa = false;
            System.out.println("----------posun doprava sa vydaril.");
        }
    }

    //implementovane pohyby ale nie vykreslene
    private void nastavPohnutieDole() {
        if (!this.hybeSa && (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))) {
            this.sprites = this.vykreslovacPredmetov.getPlayerMovingDown();
            this.hybeSa = true;
            if (this.postava.getY() - this.vykreslovacPredmetov.getPlayerTexture().getHeight() < 0) {
                this.hitDole = true;
                this.zmenLokalitu(this.vykreslovacPozadiLokalit);
                this.hitDole = false;
            } else {
                //this.batch.draw(this.vykreslovacPredmetov.getPlayerMovingDown(), (float) this.postava.getX(), (float) this.postava.getY());
                this.postava.nastavPoziciuHraca(this.postava.getX(), this.postava.getY() - 10);
            }
            this.batch.draw(this.vykreslovacPredmetov.getPlayerTexture(), (float) this.postava.getX(), (float) this.postava.getY());
            this.hybeSa = false;
            System.out.println("----------posun dole sa vydaril.");
        }
    }

    private void nastavPohnutieHore() {
        if (!this.hybeSa && (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))) {
            this.hybeSa = true;
            if (this.postava.getY() > Gdx.graphics.getHeight() - this.vykreslovacPredmetov.getPlayerTexture().getHeight()) {
                this.hitHore = true;
                this.zmenLokalitu(this.vykreslovacPozadiLokalit);
                this.hitHore = false;
            } else {
                this.postava.nastavPoziciuHraca(this.postava.getX(), this.postava.getY() + 10);
            }
            this.batch.draw(this.vykreslovacPredmetov.getPlayerTexture(), (float) this.postava.getX(), (float) this.postava.getY());
            this.hybeSa = false;
            System.out.println("----------posun hore sa vydaril.");
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.sprites[this.spriteIndex], (float) this.postava.getX(), (float) this.postava.getY());
    }

    private void vypisAktualnejLokality() {
        System.out.println("Aktuálna lokalita sa zmenila na : " + this.postava.getAktualnaLokalita().dajNazovLokality());
    }

    private void zmenaNaVykreslenieAVypis() {
        this.generatorLokalit.vykresliPozadieLokality(this.vykreslovacPozadiLokalit, this.batch);
        this.generatorLokalit.naplnovacPosunuty();
        this.postava.setEnergy(-10);
        vypisAktualnejLokality();
    }

    /**
     * (Ovladanie) Zmena lokalit podľa toho, či postava dosiahla okraje obrazovky a nachádza sa tam východ.
     * Vykreslenie a nastavenie novej lokality.
     *
     * @param vykreslovacPozadiLokalit
     */
    @Override
    public void zmenLokalitu(VykreslovacPozadiLokalit vykreslovacPozadiLokalit) {
        this.vykreslovacPozadiLokalit = vykreslovacPozadiLokalit;
        this.postava.getAktualnaLokalita().initPredmetov();

        //ošetrenie plaže
        if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("plaz") && this.hitVpravo) {
            Lokalita novaZmenena = this.generatorLokalit.getLes();
            //this.postava.dajPoziciuPostavy();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(50, Gdx.graphics.getHeight() / 6); // na x nereaguje
            this.postava.vykresliSaNaZaciatok();

        }
        //ošetrenie lesa doľava na plaz:
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("les") && this.hitVlavo) {
            Lokalita novaZmenena = this.generatorLokalit.getPlaz();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6);

        }
        //ošetrenie lesa doprava:
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("les") && this.hitVpravo) {
            Lokalita novaZmenena = this.generatorLokalit.getLietadlo();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(0, Gdx.graphics.getHeight() / 6);

        } else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("les") && this.hitDole) {
            Lokalita novaZmenena = this.generatorLokalit.getCesta();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        }
        //ošetrenie lietadla naspat dolava :
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("lietadlo") && this.hitVlavo) {
            Lokalita novaZmenena = this.generatorLokalit.getLes();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6);

        } //osetrenie lesu hore :
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("les") && this.hitHore) {
            Lokalita novaZmenena = this.generatorLokalit.getVodopad();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth() / 2, 0);

        }
        //osetrenie cesty hore do lesa
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("cesta") && this.hitHore) {
            Lokalita novaZmenena = this.generatorLokalit.getLes();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth() / 2, 0);
        }

        //cesta vlavo na utes:
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("cesta") && this.hitVlavo) {
            Lokalita novaZmenena = this.generatorLokalit.getUtes();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6);

        }

        //z utesu jedine na cestu vpravo:
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("utes") && this.hitVpravo) {
            Lokalita novaZmenena = this.generatorLokalit.getCesta();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(0, Gdx.graphics.getHeight() / 6);

        }

        //osetrenie vodopadnu spat dole:
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("vodopad") && this.hitDole) {
            Lokalita novaZmenena = this.generatorLokalit.getLes();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        }
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("vodopad") && this.hitHore) {
            Lokalita novaZmenena = this.generatorLokalit.getSecret();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth() / 2, 0);

        }
        else if (this.postava.getAktualnaLokalita().dajNazovLokality().equals("secret") && this.hitDole) {
            Lokalita novaZmenena = this.generatorLokalit.getVodopad();
            this.postava.getAktualnaLokalita().zmenLokalitu(novaZmenena);
            this.zmenaNaVykreslenieAVypis();
            this.postava.nastavPoziciuHraca(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());

        }else {
            if (hitVpravo) {
                this.postava.getAktualnaLokalita();
                this.postava.setX(Gdx.graphics.getWidth() - (int) this.vykreslovacPredmetov.getPlayerTexture().getWidth());
            } else if (hitDole) {
                this.postava.setY(0);
            } else if (hitVlavo) {
                this.postava.setX(0);
            } else if (hitHore) {
                this.postava.setY(Gdx.graphics.getHeight() - (int) this.vykreslovacPredmetov.getPlayerTexture().getHeight());
            }
        }
    }
}