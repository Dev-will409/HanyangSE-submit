package edu.hanyang.submit;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.github.hyerica_bdml.indexer.ExternalSort;
import org.apache.commons.lang3.tuple.MutableTriple;

class DataManagers {
    public boolean isEOF = false;
    private DataInputStream dis = null;
    public MutableTriple<Integer, Integer, Integer> tuple = new MutableTriple<Integer, Integer, Integer>(0,0,0);
    public DataManagers(DataInputStream dis) throws IOException {
        ;
    }

    private  boolean readNext() throws IOException {
        if (isEOF) return false;
        tuple.setLeft(dis.readInt()); tuple.setMiddle(dis.readInt()); tuple.setRight(dis.readInt());
        return true;
    }

    public void getTuple(MutableTriple<Integer, Integer, Integer> ret) throws  IOException {
        ret.setLeft(tuple.getLeft()); ret.setMiddle(tuple.getMiddle()); ret.setRight(tuple.getRight());
        isEOF = (! readNext());
    }
 }
/*
public class HanyangSEExternalSort implements ExternalSort {

    /**
     * External sorting
     * @param infile    정렬되지 않은 데이터가 있는 파일
     * @param outfile   정렬된 데이터가 쓰일 파일
     * @param tmpdir    임시파일을 위한 디렉토리
     * @param blocksize 허용된 메모리 블록 하나의 크기
     * @param nblocks   허용된 메모리 블록 개수


    @Override
    public void sort(String infile, String outfile, String tmpdir, int blocksize, int nblocks) throws IOException {
        //initial phase
        ArrayList <MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nblocks);
        File in = new File (infile);
        FileInputStream input = new FileInputStream(in);
        while (input.available()!=0){
            MutableTriple<Integer, Integer , Integer> tmp = new MutableTriple<>();
            tmp.setLeft(input.read());
            tmp.setMiddle(input.read());
            tmp.setRight(input.read());
            dataArr.add(tmp);
        }


        //n-way merge
        _externalMergesort(tmpdir, outfile, 0);
    }

    private void _externalMergesort(String tmpDir, String Outputfile, int step) throws IOException {
        File[] fileArr = (new File (tmpDir + File.separator + String.valueOf(prevStep))).listFiles();
        if (fileArr.length <= nblocks -1 ) {
            for (FIle f : fileArr) {
                DataInputStream Dos = new DataInputStream(f.getAbsolute.Path(), blocksize);

            }
        }
        else {
            for (FIle f : fileArr) {


                cnt++;
                if(cnt == nblocks - 1) {
                    //n_way_merge();
                }
            }
            _externalMergesort(tmpDir, outputfile, step+1);
        }
    }

    public void n_way_merge(List<DataInputStream> files, String outputFile) throws IOException {
        priorityQueue<DataManager> queue = new PriorityQueue<>(files.size(), new Comparator<DataManager>() {
            public int compare(DataManager o1, dataManager o2) {
                return o1.tuple.compareTo(o2.tuple);
            }
        });

        while (queue.size() != 0) {
            DataManager dm = queue.poll();
            MutableTriple<Integer, Integer, Integer> tmp = dm.getTuple();

        }
    }

 }

*/