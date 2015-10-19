package org.asuki.dp.gof23;

import static java.util.Arrays.asList;
import static org.asuki.dp.gof23.Iterator.ItemType.ANY;
import static org.asuki.dp.gof23.Iterator.ItemType.POTION;
import static org.asuki.dp.gof23.Iterator.ItemType.RING;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Iterator {

    interface ItemIterator {
        boolean hasNext();

        Item next();
    }

    static class ItemIteratorImpl implements ItemIterator {

        private Treasure treasure;
        private ItemType type;
        private int idx;

        public ItemIteratorImpl(Treasure treasure, ItemType type) {
            this.treasure = treasure;
            this.type = type;
            this.idx = -1;
        }

        @Override
        public boolean hasNext() {
            nextIdx();
            return idx != -1;
        }

        @Override
        public Item next() {
            return treasure.getItems().get(idx);
        }

        private void nextIdx() {

            List<Item> items = treasure.getItems();

            int tempIdx = idx;
            while (true) {
                tempIdx++;
                if (tempIdx >= items.size()) {
                    tempIdx = -1;
                    break;
                }
                if (type.equals(ANY)
                        || items.get(tempIdx).getType().equals(type)) {
                    break;
                }
            }

            idx = tempIdx;
        }
    }

    static class Treasure {

        @Getter
        private List<Item> items;

        public Treasure() {
            //@formatter:off
            items = asList(
                    new Item(POTION, "PotionA"), 
                    new Item(RING,"RingA"), 
                    new Item(POTION, "PotionB"), 
                    new Item(POTION,"PotionC"), 
                    new Item(RING, "RingB"));
            //@formatter:on
        }

        ItemIterator iterator(ItemType type) {
            return new ItemIteratorImpl(this, type);
        }

    }

    @AllArgsConstructor
    static class Item {

        @Getter
        private final ItemType type;
        private final String name;

        @Override
        public String toString() {
            return name;
        }

    }

    enum ItemType {
        ANY, RING, POTION
    }
}
