package edu.hanyang.submit;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class t {
    public void mergesort(ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr, int nblocks) {
        
    }
    public static void main(String[] args) throws IOException {
        String path = "data/input_10000000.data";
        int nblocks = 5;
        int i = 0;
        File in = new File (path);
        FileInputStream input = new FileInputStream(in);
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nblocks);
        while (input.available()!=0){
            i++;
            if(i==6) break;
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            tmp.setLeft(input.read());
            tmp.setMiddle(input.read());
            tmp.setRight(input.read());
            dataArr.add(tmp);
        }
        System.out.print(dataArr);
    }
}
