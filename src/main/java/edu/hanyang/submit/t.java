package edu.hanyang.submit;

import org.apache.commons.lang3.tuple.MutableTriple;
import scala.Int;

import java.awt.desktop.AppReopenedEvent;
import java.io.*;
import java.util.*;
/*
class DataManager {
    public boolean isEOF = false;
    private DataInputStream dis = null;
    public MutableTriple<Integer, Integer, Integer> tuple = new MutableTriple<Integer, Integer, Integer>(0,0,0);
    public DataManager(DataInputStream dis) throws IOException {
        this.dis = dis;
        isEOF = readNext();
    }

    private boolean readNext() throws IOException {
        if (dis.available() == 0) return false;
        tuple.setLeft(dis.readInt()); tuple.setMiddle(dis.readInt()); tuple.setRight(dis.readInt());
        return true;
    }

    public void getTuple(MutableTriple<Integer, Integer, Integer> ret) throws  IOException {
        ret.setLeft(tuple.getLeft()); ret.setMiddle(tuple.getMiddle()); ret.setRight(tuple.getRight());
        isEOF = readNext();
    }

}
public class t {
    public static void Mkdir(String path) {
        File Folder = new File(path);
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }else {
        }
    }
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
    public static void sortl(String infile, String outfile, String tmpdir, int blocksize, int nblocks) throws IOException {
        //initial phase
        File in = new File(infile);
        File out = new File(outfile);
        File tmpd = new File(tmpdir);
        int cnt = 0;
        DataInputStream input = new DataInputStream(
                new BufferedInputStream (new FileInputStream(infile), (nblocks-1)*blocksize));
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nblocks);
        System.out.println("init start");
        while (input.available()!=0) {
            System.out.println(++cnt);
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            tmp.setLeft(input.readInt());
            tmp.setMiddle(input.readInt());
            tmp.setRight(input.readInt());
            dataArr.add(tmp);
            if(dataArr.size() >= (nblocks-1)*blocksize/24 || input.available() == 0) {
                dataArr.sort(new Comparator<MutableTriple<Integer, Integer, Integer>>() {
                    @Override
                    public int compare(MutableTriple<Integer, Integer, Integer> o1, MutableTriple<Integer, Integer, Integer> o2) {
                        return o1.compareTo(o2);
                    }
                });
                Mkdir(tmpd + File.separator + String.valueOf(0));
                DataOutputStream out_stream = new DataOutputStream(
                new BufferedOutputStream (new FileOutputStream(tmpd + File.separator + String.valueOf(0)+ File.separator+String.valueOf(cnt)), nblocks*blocksize));
                out_stream.writeBytes(dataArr.toString());

                for(int i=0; i<dataArr.size(); i++) {
                    out_stream.writeInt(dataArr.get(i).getLeft());
                    out_stream.writeInt(dataArr.get(i).getMiddle());
                    out_stream.writeInt(dataArr.get(i).getRight());
                }


                out_stream.flush();
                out_stream.close();
                dataArr.clear();
                continue;
            }
        }
        input.close();
        System.out.println("init finish");


        //n-way merge
        System.out.println("go external");
        _externalMergesortt(tmpdir, outfile, 0, blocksize, nblocks);
    }
    private static void _externalMergesortt(String tmpDir, String Outputfile, int Step, int blocksize, int nblocks) throws IOException {
        File[] fileArr = (new File (tmpDir + File.separator + String.valueOf(Step))).listFiles();
        int cnt=0;
        List<DataInputStream> blocks = new ArrayList<DataInputStream>();
        if(fileArr.length == 0) return;
        if (fileArr.length <= nblocks -1 ) {
            for (File f : fileArr) {
                FileInputStream fileinput = new FileInputStream(f.getAbsolutePath());
                DataInputStream dos = new DataInputStream(fileinput);
                blocks.add(dos);
            }
            System.out.println("last nway");
            n_way(blocks,Outputfile, blocksize, nblocks);
        }
        else {
            for (File f : fileArr) {
                FileInputStream fileinput = new FileInputStream(f.getAbsolutePath());
                DataInputStream dos = new DataInputStream(fileinput);
                blocks.add(dos);
                cnt++;
                if(cnt == nblocks - 1) {
                    System.out.println("do nway");
                    Mkdir(tmpDir + File.separator + String.valueOf(Step+1));
                    n_way(blocks, tmpDir + File.separator + String.valueOf(Step+1)+ File.separator + String.valueOf(cnt), blocksize, nblocks);
                    blocks.clear();
                    cnt = 0;
                    continue;
                }
            }
            if(blocks.size() != 0) {
                System.out.println("do nway2");
                Mkdir(tmpDir + File.separator + String.valueOf(Step+1));
                n_way(blocks, tmpDir + File.separator + String.valueOf(Step+1)+File.separator + String.valueOf(cnt), blocksize, nblocks);
                blocks.clear();
            }
            System.out.println("do external exclusion");
            Mkdir(tmpDir + File.separator + String.valueOf(Step+1));
            _externalMergesortt(tmpDir, Outputfile, Step+1, blocksize, nblocks);
        }
    }
    public static void n_way(List<DataInputStream> files, String outputfile, int blocksize, int nblocks) throws IOException {
        PriorityQueue<DataManager> queue = new PriorityQueue<>(5, new Comparator<DataManager>() {
            @Override
            public int compare(DataManager o1, DataManager o2) {
                return o1.tuple.compareTo(o2.tuple);
            }
        });
        int counter = 0;
        while(true) {
            if(counter >= files.size()) break;
            System.out.println(counter+"dododo");
            queue.add(new DataManager(files.get(counter)));
            counter++;
        }
        int k = 0;
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>();
        File output = new File(outputfile);
        DataOutputStream fos = new DataOutputStream(
            new BufferedOutputStream( new FileOutputStream(output), (nblocks-1)*blocksize));
        System.out.println("do queue");
        while (queue.size() != 0) {
            DataManager dm = queue.poll();
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            dm.getTuple(tmp);
            dataArr.add(tmp);
            if(dataArr.size() >= (nblocks-1)*blocksize/8 || queue.size() == 0) {
                k += dataArr.size();
                System.out.println(k + " is sorted\n");
                for(int i=0; i<dataArr.size(); i++) {
                    fos.writeInt(dataArr.get(i).getLeft());
                    fos.writeInt(dataArr.get(i).getMiddle());
                    fos.writeInt(dataArr.get(i).getRight());
                }
                dataArr.clear();
            }
            if(dm.isEOF) {
                queue.add(dm);
            }
            else {
                System.out.println("queue 한개 다씀");
            }
        }
        fos.close();
    }

    public static void main(String[] args) throws IOException {
        long stime = System.currentTimeMillis();
        String infile = "data/input_10000000.data";
        int nblocks = 1000;
        int blocksize = 1024*8;
        String outfile = "data/sort.txt";
        String tmpdir = "tmp/";
        //sortl(infile, outfile, tmpdir, blocksize, nblocks);
        File in = new File(infile);
        File out = new File(outfile);
        File tmpd = new File(tmpdir);
        int cnt = 0;
        DataInputStream input = new DataInputStream(
                new BufferedInputStream (new FileInputStream(infile), (nblocks-1)*blocksize));

        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>((nblocks-1)*blocksize);
        System.out.println("init start");
        try{
            while(true) {
                int data = -1;
                while (dataArr.size() != (nblocks - 1) * blocksize) {
                    dataArr.add(new MutableTriple<>(input.readInt(), input.readInt(), input.readInt()));
                    if(input.available() == 0) break;
                }
                dataArr.sort(new Comparator<MutableTriple<Integer, Integer, Integer>>() {
                    @Override
                    public int compare(MutableTriple<Integer, Integer, Integer> o1, MutableTriple<Integer, Integer, Integer> o2) {
                        return o1.compareTo(o2);
                    }
                });
                Mkdir(tmpd + File.separator + String.valueOf(0));
                DataOutputStream out_stream = new DataOutputStream(
                        new BufferedOutputStream (new FileOutputStream(tmpd + File.separator + String.valueOf(0)+ File.separator+String.valueOf(cnt)), nblocks*blocksize));
                for (int i = 0; i < dataArr.size(); i++) {
                    out_stream.write(Integer.parseInt(String.valueOf(dataArr.get(i).getLeft())));
                    out_stream.write(Integer.parseInt(String.valueOf(dataArr.get(i).getMiddle())));
                    out_stream.write(Integer.parseInt(String.valueOf(dataArr.get(i).getRight())));
                }
                cnt += dataArr.size()*3;
                System.out.println(cnt);
                dataArr.clear();
                out_stream.flush();
                out_stream.close();
            }

        }
        catch (EOFException e){
        }
        long etime = System.currentTimeMillis();
        input.close();
        System.out.println("복사 시간: "+(etime-stime));
        System.out.println("init finish");

        /*
        DataInputStream input = new DataInputStream(
                new BufferedInputStream (new FileInputStream(infile), (nblocks-1)*blocksize));
        for(int i=0; i<1000; i++) {
            System.out.println(input.read());
        }
         */
        /*
        List<DataInputStream> files = new ArrayList<DataInputStream>();
        int counter = 0;
        FileInputStream fileinput = new FileInputStream(infile);
        DataInputStream dos = new DataInputStream(fileinput);
        DataManager aa = new DataManager(dos);
        files.add(dos);
        PriorityQueue<DataManager> queue = new PriorityQueue<>(5, new Comparator<DataManager>() {
            @Override
            public int compare(DataManager o1, DataManager o2) {
                return o1.tuple.compareTo(o2.tuple);
            }
        });
        System.out.println(files.size());
        while(true) {
            if(counter >= files.size())
                break;
            System.out.println(counter+"dododo");
            queue.add(new DataManager(files.get(counter)));
            counter++;
        }
        */

        //int nblocks = 5;
        //int blocksize = 30;
        /*
        File in = new File(path);
        File out = new File("sort.txt");
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(in), nblocks*blocksize);

        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nblocks);
        while (input.available()!=0) {
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            tmp.setLeft(input.read());
            tmp.setMiddle(input.read());
            tmp.setRight(input.read());
            dataArr.add(tmp);
            if(dataArr.size() == nblocks*blocksize) {
                dataArr.sort(new Comparator<MutableTriple<Integer, Integer, Integer>>() {
                    @Override
                    public int compare(MutableTriple<Integer, Integer, Integer> o1, MutableTriple<Integer, Integer, Integer> o2) {
                        return o1.compareTo(o2);
                    }
                });
                BufferedOutputStream out_stream = new BufferedOutputStream(new FileOutputStream(out), nblocks*blocksize);
                for(int i=0; i<dataArr.size()/3; i++) {
                    out_stream.write(dataArr.get(i).getLeft());
                    out_stream.write(dataArr.get(i).getMiddle());
                    out_stream.write(dataArr.get(i).getRight());
                }
                out_stream.flush();
                out_stream.close();
                break;

            }
        }
        input.close();
        //merge_sort(dataArr, 0, dataArr.size()-1);
        System.out.print(dataArr);
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
        //MutableTriple<Integer, Integer, Integer> t = new MutableTriple<>(1,1,1);
        //MutableTriple<Integer, Integer, Integer> tt = new MutableTriple<>(0,1,3);
        //System.out.println(tt.compareTo(t));

  //  }
//}


