package com.exmple.showxlsuniversities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private SideBar sideBar;
    private ArrayList<UniversitiesModel> list = new ArrayList();
    private SortAdapter sortAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        sideBar = (SideBar) findViewById(R.id.side_bar);
        sideBar.setOnStrSelectCallBack((index, selectStr) -> {
            for (int i = 0; i < list.size(); i++) {
                if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                    listView.setSelection(i); // 选择到首字母出现的位置
                    return;
                }
            }
        });

        new Thread(() -> {
            AssetManager localAssetManager = getAssets();
            try {
                Workbook localWorkbook = Workbook.getWorkbook(localAssetManager.open("universities.xls"));
                Sheet localSheet = localWorkbook.getSheet(0);    //下游标取值
                int i = localWorkbook.getNumberOfSheets();
                int j = localSheet.getRows();
                int k = localSheet.getColumns();
                for (int m = 0; m < j; m++) {
                    CountryModel localCountryModel = new CountryModel();
                    localCountryModel.setCityName(localSheet.getCell(0, m).getContents());
                    localCountryModel.setCode(localSheet.getCell(1, m).getContents());
                    localCountryModel.setId(localSheet.getCell(2, m).getContents());

                    list.add(new UniversitiesModel(localSheet.getCell(1, m).getContents()));

                }
                localWorkbook.close();
            } catch (Exception localException) {
            }
            runOnUiThread(() -> {
                Collections.sort(list);
                sortAdapter = new SortAdapter(this, list);
                listView.setDivider(null);
                listView.setAdapter(sortAdapter);
            });
        }).start();
    }
}