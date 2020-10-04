import com.marufeb.model.*;
import com.marufeb.model.Stack;
import javafx.scene.control.Tab;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class DealerTest {

    private final List<String> players = Arrays.asList("Jack", "Arnold", "Oswald");
    private final Stack shuffledStack = new Stack(52);
    private final List<Queue<Card>> cover = new ArrayList<>();
    private final Variations v;

    @Parameterized.Parameters(name = "{index} : mode {0}")
    public static Collection<Variations> data() {
        return Arrays.asList(Variations.FIRST, Variations.SECOND, Variations.THIRD);
    }

    public DealerTest(Variations v) {
        this.v = v;
    }

    @Before
    public void setUp() {
        cover.clear();
        Table.init(players, Variations.FIRST);
        players.forEach(it -> cover.add(new ArrayDeque<>()));
        cover.get(0).addAll(Arrays.asList(
                new Card(10, Card.Type.DIAMONDS),
                new Card(9, Card.Type.HEART)
        ));
        cover.get(1).addAll(Arrays.asList(
                new Card(2, Card.Type.SPADES),
                new Card(10, Card.Type.CLUBS)
        ));
        cover.get(2).addAll(Arrays.asList(
                new Card(12, Card.Type.HEART),
                new Card(8, Card.Type.SPADES)
        ));
    }

    @Test
    public void testShuffle() {
        List<Card> old = shuffledStack.getCards();
        Dealer.shuffle(shuffledStack.getCards());

        assert shuffledStack.getCards().equals(old) : "Stack was not shuffled";
    }

    @Test
    public void testGiveCards() {
        Table.init(players.subList(0, 2), v);
        Dealer.giveCards(cover.subList(0,2), shuffledStack);

        assert cover.stream()
                .limit(2)
                .allMatch(it->it.size() == shuffledStack.getCards().size()/2) : "Cards were given wrong";
    }

    @Test
    public void testCheckGameStatus() {
        Optional<String> result = Dealer.checkGameStatus(v);

        assert result.isPresent() && result.get().equals("TIE GAME") : "Cannot determinate when is TIE GAME";

        cover.forEach(Queue::clear);
        cover.get(0).addAll(Arrays.asList(new Card(2, Card.Type.DIAMONDS), new Card(2, Card.Type.SPADES)));
        cover.get(1).addAll(Collections.singletonList(new Card(10, Card.Type.CLUBS)));
        if (v==Variations.THIRD)
            cover.get(2).addAll(Arrays.asList(new Card(11, Card.Type.CLUBS), new Card(1, Card.Type.HEART)));

        Table.setCoverStacks(cover, players.subList(0, (v==Variations.THIRD?3:2)));
        Table.setVariation(v);

        Table.showCard();
        result = Dealer.checkGameStatus(v);
        assert result.isPresent() && result.get().contains("wins") : "Cannot determinate when there's a WINNER";

        System.out.println(result.get());

    }

    @Test
    public void testCheckGameWinner() {
        Optional<String> result;
        cover.forEach(Queue::clear);
        cover.get(0).addAll(Arrays.asList(new Card(2, Card.Type.DIAMONDS), new Card(2, Card.Type.SPADES)));
        cover.get(1).addAll(Collections.singletonList(new Card(10, Card.Type.CLUBS)));
        if (v==Variations.THIRD)
            cover.get(2).addAll(Arrays.asList(new Card(11, Card.Type.CLUBS), new Card(1, Card.Type.HEART)));

        Table.setCoverStacks(cover, players.subList(0, (v==Variations.THIRD?3:2)));
        Table.setVariation(v);

        assert Table.showCard();

        assert !Table.showCard();

        result = Dealer.checkWin(v);

        if (result.isEmpty()) {
            result = Table.establishWinner();
        }

        assert result.isPresent() : "Cannot determinate when there's a winner";

        System.out.println(result.get());
    }

    @Test
    public void testCollectCards() {
        cover.forEach(Queue::clear);
        cover.get(0).addAll(Arrays.asList(new Card(2, Card.Type.DIAMONDS), new Card(2, Card.Type.SPADES)));
        cover.get(1).addAll(Collections.singletonList(new Card(10, Card.Type.CLUBS)));
        if (v==Variations.THIRD)
            cover.get(2).addAll(Arrays.asList(new Card(11, Card.Type.CLUBS), new Card(1, Card.Type.HEART)));

        Table.setCoverStacks(cover, players.subList(0, (v==Variations.THIRD?3:2)));
        Table.setVariation(v);

        assert Table.showCard();

        Dealer.checkGameStatus(v);
        // todo: complete this test and deliver final version!
        if (v == Variations.FIRST)
            assert Table.getCoverStacks().get(1).size() == 2 : "Error occurred when collecting cards";
        else if (v == Variations.SECOND)
            assert Table.getPlayerPoints().get("Arnold").size() == 2 : "Error occurred when collecting cards";
        else
            assert Table.getPlayerPoints().get("Arnold").size() == 0 : "Error occurred when collecting cards";

    }

}
