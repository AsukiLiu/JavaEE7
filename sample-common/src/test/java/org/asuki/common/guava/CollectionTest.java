package org.asuki.common.guava;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static com.google.common.collect.Range.*;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.union;
import static java.lang.System.out;
import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.ClassToInstanceMap;
//import com.google.common.collect.Constraints;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.collect.Range;
import com.google.common.collect.Table;

public class CollectionTest {

    private Customer customer1 = new Customer(1, "Name1");
    private Customer customer2 = new Customer(2, "Name2");
    private Customer customer3 = new Customer(3, "Name3");
    private Customer customer4 = new Customer(null, "Unknown");

    @Test
    public void testLists() {

        List<Integer> items = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        List<List<Integer>> pages = partition(items, 3);

        assertEquals(pages.toString(), "[[1, 2, 3], [4, 5, 6], [7]]");
    }

    @Test
    public void testSets() {

        ImmutableSet<Customer> customersA = ImmutableSet.of(customer1,
                customer2, customer3);
        ImmutableSet<Customer> customersB = ImmutableSet.of(customer3,
                customer4);

        assertEquals(union(customersA, customersB).size(), 4);
        assertEquals(intersection(customersA, customersB),
                ImmutableSet.of(customer3));

        List<String> list = newArrayList("aa", "bb", "cc", "cc");
        Set<String> set = newHashSet(list);
        assertEquals(set.toString(), "[aa, bb, cc]");

        Set<Integer> a = newHashSet(Arrays.asList(1, 2, 3));
        Set<Integer> b = newHashSet(Arrays.asList(3, 4, 5));

        assertEquals(intersection(a, b).toString(), "[3]");
        assertEquals(difference(a, b).toString(), "[1, 2]");
        assertEquals(union(a, b).toString(), "[1, 2, 3, 4, 5]");
    }

    @Test
    public void testMaps() {

        SortedMap<String, String> map = new TreeMap<>();
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", null);
        map.put("4", "four");

        SortedMap<String, String> filtered = Maps.filterValues(map,
                Predicates.notNull());

        assertEquals(filtered.size(), 3);
    }

//    @Test(expectedExceptions = NullPointerException.class)
//    public void testConstraints() {
//
//        HashSet<Customer> customers = newHashSet();
//        customers.add(null);
//
//        Set<Customer> noMoreNulls = Constraints.constrainedSet(customers,
//                Constraints.notNull());
//        noMoreNulls.add(null);
//
//        fail("No exception happened!");
//    }

    @Test
    public void testMultimap() {

        Multimap<String, String> multiMap = ArrayListMultimap.create();
        multiMap.put("key", "value1");
        multiMap.put("key", "value2");

        Collection<String> collection = multiMap.get("key");
        assertEquals(collection.size(), 2);

        // @formatter:off
        List<Customer> customers = Arrays.asList(
                new Customer(1, "Asuki","Tokyo"), 
                new Customer(2, "Tom", "London"), 
                new Customer(3,"Mike", "London"));
        // @formatter:on

        ListMultimap<String, Customer> customersByAddress = Multimaps.index(
                customers, new Function<Customer, String>() {
                    @Override
                    public String apply(Customer customer) {
                        return customer.getAddress();
                    }
                });

        List<Customer> tokyoCustomers = customersByAddress.get("London");
        assertEquals(tokyoCustomers.size(), 2);
    }

    @Test
    public void testMuliSet() {
        Multiset<String> multiSet = HashMultiset.create();
        multiSet.add("key");
        multiSet.add("key");

        multiSet.addAll(Arrays.asList("key", "other"));
        assertEquals(multiSet.count("key"), 3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBiMap() {
        BiMap<String, String> biMap = HashBiMap.create();
        biMap.put("key", "value");

        assertEquals(biMap.get("key"), "value");
        assertEquals(biMap.inverse().get("value"), "key");

        biMap.put("otherKey", "value");

        fail("No exception happened!");
    }

    @Test
    public void testTable() {

        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        // Map<Vertex, Map<Vertex, Double>> nestedMap = Maps.newHashMap();
        Table<Vertex, Vertex, Double> weightedGraph = HashBasedTable.create();
        weightedGraph.put(v1, v2, 4d);
        weightedGraph.put(v1, v3, 20d);
        weightedGraph.put(v2, v3, 5d);

        // v2 to 4, v3 to 20
        for (Map.Entry<Vertex, Double> entry : weightedGraph.row(v1).entrySet()) {
            out.println(entry.getKey() + "" + entry.getValue());
        }

        // v1 to 20, v2 to 5
        for (Map.Entry<Vertex, Double> entry : weightedGraph.column(v3)
                .entrySet()) {
            out.println(entry.getKey() + "" + entry.getValue());
        }
    }

    @Test
    public void testClassToInstanceMap() {
        ClassToInstanceMap<String> stringInstanceMap = MutableClassToInstanceMap
                .create();
        ClassToInstanceMap<Customer> customerInstanceMap = MutableClassToInstanceMap
                .create();

        stringInstanceMap.put(String.class, "Google");
        assertEquals(stringInstanceMap.getInstance(String.class), "Google");

        Customer expect = new Customer(10, "Google");
        customerInstanceMap.putInstance(Customer.class, expect);

        Customer actual = customerInstanceMap.getInstance(Customer.class);
        assertEquals(actual.toString(), "Customer{name=Google, id=10}");
    }

    @Test(dataProvider = "rangeData")
    public void testRanges(Range<Integer> range, boolean isContain,
            boolean isNotContain) {

        assertEquals(range.contains(0), isContain);
        assertEquals(range.contains(10), isNotContain);
    }

    @DataProvider(name = "rangeData")
    private Object[][] rangeData() {
        // @formatter:off
        return new Object[][] { 
                { closed(0, 10), true, true },
                { closedOpen(0, 10), true, false },
                { open(0, 10), false, false },
                { openClosed(0, 10), false, true } 
        };
        // @formatter:on
    }

    @AllArgsConstructor
    @ToString
    private static class Vertex {

        @Getter
        @Setter
        private int mark;
    }

}
