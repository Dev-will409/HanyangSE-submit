package edu.hanyang.submit;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class t {
    public static void merge(ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr, int left, int mid, int right) {
        int l = left;
        int r = mid +1;
        int idx = left;
        ArrayList<MutableTriple<Integer, Integer, Integer>> tmp = new ArrayList<>(dataArr.size());
        for(int i=0; i<dataArr.size(); i++) {
            MutableTriple<Integer, Integer, Integer> t = new MutableTriple<>();
            t.setLeft(null);
            t.setMiddle(null);
            t.setRight(null);
            tmp.add(t);
        }
        while(l <= mid && r <= right) {
            if(dataArr.get(l).getLeft() < dataArr.get(r).getLeft()) {
                tmp.set(idx, dataArr.get(l));
                idx++;
                l++;
            }
            else if (dataArr.get(l).getLeft() > dataArr.get(r). getLeft()) {
                tmp.set(idx, dataArr.get(r));
                idx++;
                r++;
            }
            else {
                if(dataArr.get(l).getMiddle() < dataArr.get(r).getMiddle()) {
                    tmp.set(idx, dataArr.get(l));
                    idx++;
                    l++;
                }
                else if (dataArr.get(l).getMiddle() > dataArr.get(r).getMiddle()) {
                    tmp.set(idx, dataArr.get(r));
                    idx++;
                    r++;
                }
                else {
                    if(dataArr.get(l).getRight() <= dataArr.get(r).getRight()) {
                        tmp.set(idx, dataArr.get(l));
                        idx++;
                        l++;
                    }
                    else {
                        tmp.set(idx, dataArr.get(r));
                        idx++;
                        r++;
                    }
                }
            }
        }
        if(l > mid) {
            while(r <= right) {
                tmp.set(idx, dataArr.get(r));
                idx++;
                r++;
            }
        }
        else {
            while(l<=mid) {
                tmp.set(idx, dataArr.get(l));
                idx++;
                l++;
            }
        }
        for(int i=left; i <= right; i++) {
            dataArr.set(i, tmp.get(i));
        }
    }
    public static void merge_sort(ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr, int left, int right) {
        for(int size = 1; size <= right; size+=size) {
            for(int l=0; l<=right-size; l +=(2*size)) {
                int low = l;
                int mid = l+size -1;
                int high = Math.min(l+(2*size)-1, right);
                merge(dataArr, low, mid, high);
            }
        }
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
        merge_sort(dataArr, 0, dataArr.size()-1);
        System.out.print(dataArr);
    }
}
