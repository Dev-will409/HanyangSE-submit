package edu.hanyang.submit;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import io.github.hyerica_bdml.indexer.ExternalSort;
import org.apache.commons.lang3.tuple.MutableTriple;


public class HanyangSEExternalSort implements ExternalSort {

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

    /**
     * External sorting
     * @param infile    정렬되지 않은 데이터가 있는 파일
     * @param outfile   정렬된 데이터가 쓰일 파일
     * @param tmpdir    임시파일을 위한 디렉토리
     * @param blockSize 허용된 메모리 블록 하나의 크기
     * @param nBlocks   허용된 메모리 블록 개수
     */
    @Override
    public void sort(String infile, String outfile, String tmpdir, int blockSize, int nBlocks) throws IOException {
        //initial phase
        File in = new File(infile);
        File out = new File(outfile);
        File tmpd = new File(tmpdir);
        int cnt = 0;
        DataInputStream input = new DataInputStream(
                new BufferedInputStream (new FileInputStream(infile), (nBlocks-1)*blockSize));

        ArrayList <MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nBlocks);
        while (input.available()!=0) {
            cnt++;
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            tmp.setLeft(input.readInt());
            tmp.setMiddle(input.readInt());
            tmp.setRight(input.readInt());
            dataArr.add(tmp);
            if(dataArr.size() >= (nBlocks-1)*blockSize/24 || input.available() == 0) {
                dataArr.sort(new Comparator<MutableTriple<Integer, Integer, Integer>>() {
                    @Override
                    public int compare(MutableTriple<Integer, Integer, Integer> o1, MutableTriple<Integer, Integer, Integer> o2) {
                        return o1.compareTo(o2);
                    }
                });
                Mkdir(tmpd + File.separator + String.valueOf(0));
                DataOutputStream out_stream = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(tmpd + File.separator + String.valueOf(0)+ File.separator+String.valueOf(cnt)), nBlocks*blockSize));
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


        //n-way merge
        _externalMergesort(tmpdir, outfile, 0, blockSize, nBlocks);
    }

    private void _externalMergesort(String tmpDir, String outputFile, int step, int blockSize, int nBlocks) throws IOException {
        File[] fileArr = (new File (tmpDir + File.separator + String.valueOf(step))).listFiles();
        int cnt=0;
        List<DataInputStream> blocks = new ArrayList<DataInputStream>();
        if (fileArr.length <= nBlocks -1 ) {
            for (File f : fileArr) {
                //DataInputStream Dos = new DataInputStream(f.getAbsolutePath(), blockSize);
                FileInputStream fileInput = new FileInputStream(f.getAbsolutePath());
                DataInputStream dos = new DataInputStream(fileInput);
                blocks.add(dos);
            }
            n_way_merge(blocks,outputFile, blockSize, nBlocks);
        }
        else {
            for (File f : fileArr) {
                FileInputStream fileInput = new FileInputStream(f.getAbsolutePath());
                DataInputStream dos = new DataInputStream(fileInput);
                blocks.add(dos);
                cnt++;
                if(cnt == nBlocks - 1) {
                    Mkdir(tmpDir + File.separator + String.valueOf(step+1));
                    n_way_merge(blocks, tmpDir + File.separator + String.valueOf(step+1)+ File.separator + String.valueOf(cnt), blockSize, nBlocks);
                    blocks.clear();
                    cnt = 0;
                    continue;
                }
            }
            if(blocks.size() != 0) {
                Mkdir(tmpDir + File.separator + String.valueOf(step+1));
                n_way_merge(blocks, tmpDir + File.separator + String.valueOf(step+1)+File.separator + String.valueOf(cnt), blockSize, nBlocks);
                blocks.clear();
            }
            _externalMergesort(tmpDir, outputFile, step+1, blockSize, nBlocks);
        }
    }

    public void n_way_merge(List<DataInputStream> files, String outputFile, int blockSize, int nBlocks) throws IOException {
        PriorityQueue<DataManager> queue = new PriorityQueue<>(files.size(), new Comparator<DataManager>() {
            public int compare(DataManager o1, DataManager o2) {
                return o1.tuple.compareTo(o2.tuple);
            }
        });

        int counter = 0;
        while(true) {
            if(counter >= files.size()) break;
            queue.add(new DataManager(files.get(counter)));
            counter++;
        }
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>();
        File output = new File(outputFile);
        DataOutputStream fos = new DataOutputStream(
                new BufferedOutputStream( new FileOutputStream(output), (nBlocks-1)*blockSize));


        while (queue.size() != 0) {
            DataManager dm = queue.poll();
            MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
            /*
            fill in
             */
            dm.getTuple(tmp);
            dataArr.add(tmp);
            if(dataArr.size() >= (nBlocks-1)*blockSize/8 || queue.size() == 0) {
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
        }
        fos.close();
    }
 }


