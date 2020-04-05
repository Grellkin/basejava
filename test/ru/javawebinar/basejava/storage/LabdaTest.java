package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LabdaTest {
    @Test
    public void first() {
        int res = minValue(new int[]{1,4,4,4,2,6,7,4,2,1,3});
        Assert.assertEquals(123467, res);
    }

    private int minValue(int[] values){
        return Arrays.stream(values).distinct().sorted().reduce(0, (a,b) -> a*10 + b);
    }

    @Test
    public void second() {
        List<Integer> integers = Arrays.asList(1,2,5,7,4,7,8,3,3,2,1);
        Assert.assertArrayEquals(new Integer[]{1,5,7,7,3,3,1}, oddOrEven(integers).toArray());
    }

    private List<Integer> oddOrEven(List<Integer> integers){
        return integers.stream().filter(a -> a%2==0).count()%2 == 0 ?
                integers.stream().filter(a -> a%2!=0).collect(Collectors.toList()) :
                integers.stream().filter(a -> a%2==0).collect(Collectors.toList());
    }
}
