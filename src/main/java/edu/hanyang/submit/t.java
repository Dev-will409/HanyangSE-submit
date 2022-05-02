package edu.hanyang.submit;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class t {

    public static ArrayList<MutableTriple<Integer, Integer, Integer>> mergesort(ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr, int start, int end) {
        if (start < end) {
            ArrayList<MutableTriple<Integer, Integer, Integer>> tmp = new ArrayList<>(end);
            int mid = (start + end) / 2;
            mergesort(dataArr, start, mid);
            mergesort(dataArr, mid + 1, end);
            int p = start;
            int q = mid + 1;
            int idx = p;
            while (p <= mid || q <= end) {
                if (q > end || p <= mid && (dataArr.get(p).getLeft() <= dataArr.get(q).getLeft() && dataArr.get(p).getMiddle() <= dataArr.get(q).getMiddle() && dataArr.get(p).getRight() <= dataArr.get(q).getRight())) {
                    tmp.add(dataArr.get(p++));
                    idx++;
                } else {
                    tmp.add(dataArr.get(q++));
                    idx++;
                }
            }
            return tmp;
        }
        return null;
    }



    public static void main(String[] args) throws IOException {
        String path = "data/input_10000000.data";
        int nblocks = 5;
        int i = 0;
        File in = new File (path);
        FileInputStream input = new FileInputStream(in);
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nblocks);
        while (input.available()!=0) {
            i++;
            if (i == 6) break;
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            tmp.setLeft(input.read());
            tmp.setMiddle(input.read());
            tmp.setRight(input.read());
            dataArr.add(tmp);
        }
        ArrayList<MutableTriple<Integer, Integer, Integer>> temp = new ArrayList<>(nblocks);
        System.out.print(mergesort(dataArr, 0, 4));
    }
}
