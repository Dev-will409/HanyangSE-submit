package edu.hanyang.submit;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import io.github.hyerica_bdml.indexer.ExternalSort;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import io.github.hyerica_bdml.indexer.ExternalSort;
import org.apache.commons.lang3.tuple.MutableTriple;

class DataManagers {
    public boolean isEOF = false;
    private DataInputStream dis = null;
    public MutableTriple<Integer, Integer, Integer> tuple = new MutableTriple<Integer, Integer, Integer>(0,0,0);
    public DataManagers(DataInputStream dis) throws IOException {
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
     * @param blocksize 허용된 메모리 블록 하나의 크기
     * @param nblocks   허용된 메모리 블록 개수
	**/

@Override
public void sort(String infile, String outfile, String tmpdir, int blocksize, int nblocks) throws IOException {
        //initial phase
        File in = new File(infile);
        File out = new File(outfile);
        File tmpd = new File(tmpdir);
        int cnt = 0;
        DataInputStream input = new DataInputStream(
        new BufferedInputStream (new FileInputStream(infile), (nblocks-1)*blocksize));
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>(nblocks);
        while (input.available()!=0) {
        cnt++;
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
        _externalMergesort(tmpdir, outfile, 0, blocksize, nblocks);
        }

private static void _externalMergesort(String tmpDir, String Outputfile, int Step, int blocksize, int nblocks) throws IOException {
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
        n_way(blocks,Outputfile, blocksize, nblocks);
        }
        else {
        for (File f : fileArr) {
        FileInputStream fileinput = new FileInputStream(f.getAbsolutePath());
        DataInputStream dos = new DataInputStream(fileinput);
        blocks.add(dos);
        cnt++;
        if(cnt == nblocks - 1) {
        Mkdir(tmpDir + File.separator + String.valueOf(Step+1));
        n_way(blocks, tmpDir + File.separator + String.valueOf(Step+1)+ File.separator + String.valueOf(cnt), blocksize, nblocks);
        blocks.clear();
        cnt = 0;
        continue;
        }
        }
        if(blocks.size() != 0) {
        Mkdir(tmpDir + File.separator + String.valueOf(Step+1));
        n_way(blocks, tmpDir + File.separator + String.valueOf(Step+1)+File.separator + String.valueOf(cnt), blocksize, nblocks);
        blocks.clear();
        }
        Mkdir(tmpDir + File.separator + String.valueOf(Step+1));
        _externalMergesort(tmpDir, Outputfile, Step+1, blocksize, nblocks);
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
        queue.add(new DataManager(files.get(counter)));
        counter++;
        }
        ArrayList<MutableTriple<Integer, Integer, Integer>> dataArr = new ArrayList<>();
        File output = new File(outputfile);
        DataOutputStream fos = new DataOutputStream(
        new BufferedOutputStream( new FileOutputStream(output), (nblocks-1)*blocksize));

        while (queue.size() != 0) {
        DataManager dm = queue.poll();
        MutableTriple<Integer, Integer, Integer> tmp = new MutableTriple<>();
        dm.getTuple(tmp);
        dataArr.add(tmp);
        if(dataArr.size() >= (nblocks-1)*blocksize/8 || queue.size() == 0) {
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
