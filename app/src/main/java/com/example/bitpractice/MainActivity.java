package com.example.bitpractice;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bitpractice.databinding.ActivityMainBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

//import com.example.bitpractice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{

    //<editor-fold desc =”설명”>
    //private FileInputStream inputStream;     //File 읽어오기
    private InputStream inputStream;

    private String inputWord = null;
    private String readWord;

    private Button mBtnAdd;
    private Button mBtnSearch;

    //</editor-fold>
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBtnAdd = binding.btnAdd;
        mBtnSearch = binding.btnSearch;

        mBtnAdd.setEnabled(false);
        mBtnSearch.setEnabled(false);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        binding.btnReadFile.setOnClickListener(View ->
        {
            //Log.d("[CHECK]", "[btnReadFile PUSH]");  //정상작동_251002 11:33
            readFile();

            mBtnAdd.setEnabled(true);
            mBtnSearch.setEnabled(true);
        });


        mBtnSearch.setOnClickListener(View ->
        {
            searchWord();
        });
    }


    /**
     * wordFile을 읽어오고 저장된 내용을 byte[] 저장
     * inputStream raw 파일에 저장된 Resources를 읽어오기 위한 내장 키워드
     * b 읽어온 txt 파일을 byte[]로 저장
     * readWord byte[]를 String으로 변환하여 문자열 단위 처리
     */
    public void readFile()     //public void readFile()
    {
        //내장 메모리 (/data/data/패키지명/files 폴더) 경로에 있는 경우
        //inputStream = openFileInput("words_100k.txt");

        try
        {
            inputStream = getResources().openRawResource(R.raw.words_100k);

            byte[] b = new byte[inputStream.available()];

            inputStream.read(b);
            inputStream.close();     //읽어온 후 연결끊기

            //String readWord = new String(b);
            readWord = new String(b, StandardCharsets.UTF_8);     //UTF-8 명시적변환이 중요

            if(readWord.length() != 0)
            {
                Log.d("[DEBUGGING]", "readFile() 처리 완료");
            }

            //<editor-fold desc = "DEBUGGONG CODE_251002">

            // 1. 총 글자수 확인
            // 결과) txt 683,976자 = 결과 683976 _ 확인 완료_251002 12:00
            // Log.d("[DEBUGGING]", "[readFile()]" + readWord + "\nWordLength_readWord : " + readWord.length());

            // 2. 총 단어수 확인
            // split로 줄바꿈을 잘라내서 개수 확인
            // "\\R"로 걸어주면 모든 줄바꿈 문자열에 대응함
            // 참고) 윈도우 : \r\n    Linux,mac,android : \n)
            // 결과) txt 생성 10만단어 = 결과 100000 _ 확인 완료_251002 12:11
            /*String readWord = new String(b, StandardCharsets.UTF_8);
            String[] checkWordCount = readWord.split("\n");
            Log.d("[DEBUGGING]", "[readFile()]" + readWord + "\nWordCount_readWord : " + checkWordCount.length);*/


            // 3. 실제 단어 확인
            // contains로 검색용 단어 넣고 반환 받아봄
            // 결과) 3개 모두 true 반환
            /*String readWord = new String(b, StandardCharsets.UTF_8);
            Boolean checkWord = readWord.contains("fanlight");

            Log.d("[DEBUGGING]", "[readFile()]" + "\nWordCount_readWord : " + checkWord);*/

            //</editor-fold>
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 읽어온 파일에서 입력된 단어를 찾기 위함
     * 확인을 위해 임의 단어 study, developer 추가
     */
    public void searchWord()
    {
        /*
        * 미사용_251002 12:54
        * btn setEnabled 처리로 파일 읽기전 비활성화 처리 해버림 -> 해당 로직 필요없음
        if(inputStream == null)
        {
            Toast.makeText(MainActivity.this,"파일을 먼저 읽어주세요.",Toast.LENGTH_SHORT).show();
        }
        */
        Log.d("[DEBUGGING]", "Search PUSH");

        inputWord = binding.inputWord.getText().toString().trim();

        if(inputWord.isEmpty())
        {
            Toast.makeText(MainActivity.this,"입력누락",Toast.LENGTH_SHORT).show();
            return;
        }

        if(uniqueCheck(inputWord))
        {
            String[] checkWord = readWord.split("\n");
            Boolean found = false;
            int lineNum = -1;

            for(int i =0; i < checkWord.length; i++)
            {
                if(checkWord[i].equals(inputWord))
                {
                    found = true;
                    lineNum = i+1;
                    break;
                }
            }

            if(!found)
            {
                binding.tvResult.setText("검색 결과 : words_100k.txt 에 존재하지 않는 단어입니다.");
                binding.tvResult.setTextColor(Color.RED);
                return;
            }
            else
            {
                String searchResult = inputWord;
                binding.tvResult.setText("검색 결과" + "\n검색 단어 : " + searchResult + "\nIndexNo : " + lineNum);
                binding.tvResult.setTextColor(Color.BLACK);
            }
        }
        else
        {
            binding.tvResult.setText("검색 결과 : " + "알파벳 중복 없는 단어로 다시 검색 해주세요.");
            binding.tvResult.setTextColor(Color.RED);
            Toast.makeText(MainActivity.this,"알파벳 중복",Toast.LENGTH_SHORT).show();
            return;
        }
    }


    /**
     *
     * @param text @+id inputWord 에서 입력받은 값
     * @return 검증결과
     * mask 비트마스크 변수(=flag 역할)
     * bitIndex 문자열을 a부터 z까지 배열로
     */
    public static boolean uniqueCheck(String text)
    {
        //비트마스크
        long mask = 0;

        //입력값을 한글자씩 char textArray에 담아서 쉬프트연산자를 이용해 검증
        for(char textArray : text.toCharArray())
        {
            int bitIndex = -1;

            if(textArray >= 'a' && textArray <= 'z')     //소문자 0~25
            {
                bitIndex = textArray - 'a';
            }
            else if(textArray >= 'A' && textArray <= 'Z')     //대문자 26~51
            {
                bitIndex = textArray - 'A' + 26;     //소문자 z가 [25]
            }

            else
            {
                continue; // 알파벳 외 문자는 무시
            }


            long bitValue = 1L << bitIndex;   //bit shift 연산

            //bit and 연산
            //0은 꺼진 값 = 비트 켜져있다면 중복
            if((mask & bitValue) != 0)
            {
                return false;
            }

            mask |= bitValue;
            // |= 연산자는 or 할당 연산( a += 10; 이랑 동일 개념)
        }

        //<editor-fold desc = "소문자만 검사_251002 16:08">
        /*
        //비트마스크
        int mask = 0;

        //입력값을 한글자씩 char textArray에 담아서 쉬프트연산자를 이용해 검증
        for(char textArray : text.toCharArray())
        {
            if(textArray >= 'a' && textArray <= 'z')
            {
                int bitIndex = textArray - 'a';
                int bitValue = 1 << bitIndex;   //bit shift 연산

                //bit and 연산
                //0은 꺼진 값 = 비트 켜져있다면 중복
                if((mask & bitValue) != 0)
                {
                    return false;
                }

                mask |= bitValue;
                // |= 연산자는 or 할당 연산( a += 10; 이랑 동일 개념)
            }
        }
        */
        //</editor-fold desc>

        //<editor-fold desc = "DEBUGGONG CODE_251002">

        // toCharArray 배열 확인
        /*String test = "textWord";
        char[] textArray = test.toCharArray();

        Log.d("[DEBUGGING]", "[toCharArray] : " + Arrays.toString(textArray));*/
        //</editor-fold>

        return true;
    }
}