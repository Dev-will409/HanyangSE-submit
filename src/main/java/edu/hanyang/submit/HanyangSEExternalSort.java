package edu.hanyang.submit;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import io.github.hyerica_bdml.indexer.ExternalSort;
import org.apache.commons.lang3.tuple.MutableTriple;


public class HanyangSEExternalSort implements ExternalSort {

    /**
     * External sorting
     * @param infile    정렬되지 않은 데이터가 있는 파일
     * @param outfile   정렬된 데이터가 쓰일 파일
     * @param tmpdir    임시파일을 위한 디렉토리
     * @param blocksize 허용된 메모리 블록 하나의 크기
     * @param nblocks   허용된 메모리 블록 개수
     */
    @Override
    public void sort(String infile, String outfile, String tmpdir, int blocksize, int nblocks) throws IOException {
        //initial phase
        ArrayList <MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nElement);
        /*
        fill in
         */

        //n-way merge
        _externalMergesort(tmpdir, outfile, 0);
    }

    private void _externalMergesort(String tmpDir, String outputfile, int step) throws IOException {
        File[] fileArr = (new File (tmpDir + File.separator + String.valueOf(prevStep))).listFiles();
        if (fileArr.length <= nblocks -1 ) {
            for (File f : fileArr) {
                DataInputStream Dos = new DataInputStream(f.getAbsolutePath(), blocksize);
                /*
                fill in
                 */
            }
        }
        else {
            for (File f : fileArr) {


                cnt++;
                if(cnt == nblocks - 1) {
                    n_way_merge(/* fill in*/);
                }
            }
            _externalMergesort(tmpDir, outputfile, step+1);
        }
    }

    public void n_way_merge(List<DataInputStream> files, String outputFile) throws IOException {
        PriorityQueue<DataManager> queue = new PriorityQueue<>(files.size(), new Comparator<DataManager>() {
            public int compare(DataManager o1, DataManager o2) {
                return o1.tuple.compareTo(o2.tuple);
            }
        });

        while (queue.size() != 0) {
            DataManager dm = queue.poll();
            MutableTriple<Integer, Integer, Integer> tmp = dm.getTuple();
            /*
            fill in
             */
        }
    }
 }
