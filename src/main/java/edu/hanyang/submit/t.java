package edu.hanyang.submit;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class DataManager {
    public boolean isEOF = false;
    private DataInputStream dis = null;
    public MutableTriple<Integer, Integer, Integer> tuple = new MutableTriple<Integer, Integer, Integer>(0,0,0);
    public DataManager(DataInputStream dis) throws IOException {
        this.dis = dis;
        readNext();
    }

    private  boolean readNext() throws IOException {
        if (isEOF) return false;
        tuple.setLeft(dis.read()); tuple.setMiddle(dis.read()); tuple.setRight(dis.read());
        return true;
    }

    public void getTuple(MutableTriple<Integer, Integer, Integer> ret) throws  IOException {
        ret.setLeft(tuple.getLeft()); ret.setMiddle(tuple.getMiddle()); ret.setRight(tuple.getRight());
        isEOF = (! readNext());
    }

}
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
    public void n_way(List<DataInputStream> files, String outputfile) throws IOException {
        PriorityQueue<DataManager> queue = new PriorityQueue<>(5, new Comparator<DataManager>() {
            @Override
            public int compare(DataManager o1, DataManager o2) {
                return o1.tuple.compareTo(o2.tuple);
            }
        });

        for (DataInputStream f : files) {
            DataManager block = new DataManager(f);
            queue.add(block);
        }

        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>();
        File output = new File(outputfile);
        FileOutputStream fos = new FileOutputStream(output);

        while (queue.size() != 0) {
            DataManager dm = queue.poll();
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            dm.getTuple(tmp);
            dataArr.add(tmp);
            if(dataArr.size() == 5 || queue.size() == 0) {
                for(int i=0; i<5; i++) {
                    fos.write(dataArr.get(i).getLeft());
                    fos.write(dataArr.get(i).getMiddle());
                    fos.write(dataArr.get(i).getRight());
                }
                dataArr.clear();
            }
            if(dm.isEOF) {
                queue.add(dm);
            }
        }
        fos.close();
    }

    public static void main(String[] args) throws IOException {
        String path = "data/input_10000000.data";

        int nblocks = 5;
        int i = 0;

        File in = new File (path);
        FileInputStream input = new FileInputStream(in);
        /*
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
        */
        //merge_sort(dataArr, 0, dataArr.size()-1);
        //System.out.print(dataArr);
        //input.close();
        /*
        DataInputStream IN = new DataInputStream(input);
        DataManager d = new DataManager(IN);
        System.out.println(IN.read());
        MutableTriple<Integer, Integer, Integer> t = new MutableTriple<>();
        d.getTuple(t);
        System.out.println(d.tuple);
        queue.add(d);
        MutableTriple<Integer, Integer, Integer> tt = new MutableTriple<>();
        d.getTuple(tt);
        System.out.println(d.tuple);
        queue.add(d);
        MutableTriple<Integer, Integer, Integer> ttt = new MutableTriple<>();
        d.getTuple(ttt);
        System.out.println(d.tuple);
        queue.add(d);
        */
        MutableTriple<Integer, Integer, Integer> t = new MutableTriple<>(1,1,1);
        MutableTriple<Integer, Integer, Integer> tt = new MutableTriple<>(0,1,3);
        System.out.println(tt.compareTo(t));

    }
}
