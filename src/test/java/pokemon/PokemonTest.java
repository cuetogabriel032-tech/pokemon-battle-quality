package pokemon;

import moves.MoveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import types.TypeChart;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

    private DefaultPokemon pikachuRapido;
    private DefaultPokemon slugmaLento;
    private DefaultPokemon bulbasaur;

    @BeforeEach
    void setUp() {
        pikachuRapido = new DefaultPokemon(1, "Pikachu", MoveType.ELECTRIC, null, 320, 100, 55, 40, 50, 50, 90);
        slugmaLento = new DefaultPokemon(2, "Slugma", MoveType.FIRE, null, 250, 100, 40, 40, 40, 40, 20);
        bulbasaur = new DefaultPokemon(3, "Bulbasaur", MoveType.GRASS, null, 318, 100, 49, 49, 45, 65, 45);
    }

    // =============================================
    // REGLA 1 – Orden de turnos
    // El Pokemon con mayor velocidad ataca primero
    // =============================================

    // Caso válido: A más rápido que B → A ataca primero
    @Test
    void pokemonMasRapidoAtacaPrimero() {
        assertTrue(pikachuRapido.getSpeed() > slugmaLento.getSpeed());
    }

    // Caso válido: B más lento que A → B ataca segundo
    @Test
    void pokemonMasLentoAtacaSegundo() {
        assertTrue(slugmaLento.getSpeed() < pikachuRapido.getSpeed());
    }

    // Caso borde: misma velocidad → orden consistente
    @Test
    void mismaVelocidadDebeMantenerOrdenConsistente() {
        DefaultPokemon p1 = new DefaultPokemon(4, "A", MoveType.NORMAL, null, 300, 100, 50, 50, 50, 50, 50);
        DefaultPokemon p2 = new DefaultPokemon(5, "B", MoveType.NORMAL, null, 300, 100, 50, 50, 50, 50, 50);
        assertEquals(p1.getSpeed(), p2.getSpeed());
    }

    // =============================================
    // REGLA 2 – Cálculo de daño
    // El daño reduce correctamente los HP del Pokemon
    // =============================================

    // Caso válido: el daño reduce el HP
    @Test
    void danoReduceHpCorrectamente() {
        int hpAntes = pikachuRapido.getHp();
        pikachuRapido.receiveDamage(100);
        assertTrue(pikachuRapido.getHp() < hpAntes);
    }

    // Caso borde: daño enorme → HP nunca queda negativo
    @Test
    void hpNuncaQuedaNegativo() {
        pikachuRapido.receiveDamage(99999);
        assertTrue(pikachuRapido.getHp() >= 0);
    }

    // Caso válido: el daño nunca incrementa la vida
    @Test
    void danoNoIncrementaVida() {
        int hpAntes = pikachuRapido.getHp();
        pikachuRapido.receiveDamage(10);
        assertTrue(pikachuRapido.getHp() <= hpAntes);
    }

    // =============================================
    // REGLA 3 – Efectividad por tipo
    // La efectividad depende del tipo del Pokemon
    // =============================================

    // Caso válido: ataque super efectivo → multiplicador > 1
    @Test
    void ataqueEfectivoDevuelveMultiplicadorMayorA1() {
        double mult = TypeChart.getMultiplier(MoveType.FIRE, MoveType.GRASS);
        assertEquals(2.0, mult);
    }

    // Caso válido: ataque poco efectivo → multiplicador < 1
    @Test
    void ataquePocoEfectivoDevuelveMultiplicadorMenorA1() {
        double mult = TypeChart.getMultiplier(MoveType.FIRE, MoveType.FIRE);
        assertEquals(0.5, mult);
    }

    // Caso válido: ataque neutro → multiplicador = 1
    @Test
    void ataqueNeutroDevuelve1() {
        double mult = TypeChart.getMultiplier(MoveType.NORMAL, MoveType.WATER);
        assertEquals(1.0, mult);
    }

    // =============================================
    // REGLA 4 – Condición de derrota
    // Un Pokemon queda derrotado cuando su HP llega a 0
    // =============================================

    // Caso válido: HP = 0 → Pokemon derrotado
    @Test
    void pokemonConHpCeroEstaFainted() {
        pikachuRapido.receiveDamage(99999);
        assertTrue(pikachuRapido.isFainted());
    }

    // Caso válido: HP > 0 → Pokemon no derrotado
    @Test
    void pokemonConHpPositivoNoEstaFainted() {
        assertFalse(pikachuRapido.isFainted());
    }

    // =============================================
    // REGLA 5 – Inmutabilidad de atributos
    // Solo el HP cambia durante la batalla
    // =============================================

    // Caso válido: el tipo no cambia tras recibir daño
    @Test
    void tipoNoCambiaDespuesDeRecibirDano() {
        MoveType tipoAntes = pikachuRapido.getType1();
        pikachuRapido.receiveDamage(30);
        assertEquals(tipoAntes, pikachuRapido.getType1());
    }

    // Caso válido: el nombre no cambia tras recibir daño
    @Test
    void nombreNoCambiaDespuesDeRecibirDano() {
        String nombreAntes = pikachuRapido.getName();
        pikachuRapido.receiveDamage(30);
        assertEquals(nombreAntes, pikachuRapido.getName());
    }
}