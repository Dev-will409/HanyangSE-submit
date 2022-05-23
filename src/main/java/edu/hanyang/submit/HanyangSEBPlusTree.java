package edu.hanyang.submit;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.github.hyerica_bdml.indexer.BPlusTree;


public class HanyangSEBPlusTree implements BPlusTree {



    public class Node{
        InternalNode parent;
    }

    private class InternalNode extends Node{
        int maxDegree;
        int minDegree;
    }



    /**
     * B+ tree를 open하는 함수(파일을 열고 준비하는 단계 구현)
     * @param metafile B+ tree의 메타정보 저장(저장할거 없으면 안써도 됨)
     * @param treefile B+ tree의 메인 데이터 저장
     * @param blocksize B+ tree 작업 처리에 이용할 데이터 블록 사이즈
     * @param nblocks B+ tree 작업 처리에 이용할 데이터 블록 개수
     * @throws IOException
     */
    @Override
    public void open(String metafile, String treefile, int blocksize, int nblocks) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("./data/posting_list.data","rw");
        raf.seek(nblocks);
        byte[] bytes = new byte[nblocks];
        System.out.println(raf.readLine());
    }

    /**
     * B+ tree에 데이터를 삽입하는 함수
     * @param key
     * @param value
     * @throws IOException
     */
    @Override
    public void insert(int key, int value) throws IOException {
        // TODO: your code here...
    }

    /**
     * B+ tree에 있는 데이터를 탐색하는 함수
     * @param key 탐색할 key
     * @return 탐색된 value 값
     * @throws IOException
     */
    @Override
    public int search(int key) throws IOException {
        // TODO: your code here...
        return -1;
    }

    /**
     * B+ tree를 닫는 함수, 열린 파일을 닫고 저장하는 단계
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        // TODO: your code here...
    }

    public static void main(String[] args) {
        String metapath = "./tmp/bplustree.meta";
        String savepath = "./tmp/bplustree.tree";
        int blocksize = 52;
        int nblocks = 10;

        File treefile = new File(savepath);
        if (treefile.exists()) {
            if (! treefile.delete()) {
                System.err.println("error: cannot remove files");
                System.exit(1);
            }
        }

        HanyangSEBPlusTree tree = new HanyangSEBPlusTree();
        try {
            tree.open(metapath, savepath, blocksize, nblocks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


