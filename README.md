# Android 本地assets文件夹中放xls 表单数据然后读取显示。按拼音分组显示，最后再模糊检索显示
![2781551-ef4cfb0aa28e4909](https://user-images.githubusercontent.com/13359093/209094530-ec428107-259a-482b-9bf1-25059a6429f6.gif)
#### 1、 用到第三方的两个类库， 读取xls类库和按照拼音排列分组显示类库导入gradle,
``` implementation 'cc.solart:turbo-recyclerview-helper:1.0.3-beta'
    implementation 'se.emilsjolander:stickylistheaders:2.7.0'
    implementation 'com.github.rey5137:material:1.3.1'
    implementation 'com.belerweb:pinyin4j:2.5.1'   //按照拼音排列分组显示类库
    implementation 'net.sourceforge.jexcelapi:jxl:2.6.12'  //读取xls类库
```
    
#### 2、 自定义一个实体类 UniversitiesModel 实现Comparable类的接口，
``` /**
 * @ClassName UniversitiesModel
 * @Description TODO
 * @Author SeanLim
 * @Date 2021-9-17 10:04
 * @E-mail linlin.1016@qq.com
 * @Version 1.0
 */
public class UniversitiesModel implements Comparable<UniversitiesModel> {
    private String universitiesName;
    private String pinyin;
    private String firstLetter;
    public UniversitiesModel(String UniversitiesName) {
        this.universitiesName = UniversitiesName;
        pinyin = PYHelper.getPinYin(UniversitiesName); // 学校名拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    public String getUniversitiesName() {
        return universitiesName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    @Override
    public int compareTo(UniversitiesModel another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }
} 
```
#### 3、用线程获取xls里数据填充到RecyclerView 列表中
```
/**
         * 线程获取xls学校数据
         */
        new Thread(() -> {
            AssetManager localAssetManager = getAssets();
            try {
                Workbook localWorkbook = Workbook.getWorkbook(localAssetManager.open("universities.xls"));
                Sheet localSheet = localWorkbook.getSheet(0);    //下游标取值
                int j = localSheet.getRows();
                for (int m = 0; m < j; m++) {
                    universitiesModels.add(new UniversitiesModel(localSheet.getCell(1, m).getContents() + "id" + localSheet.getCell(0, m).getContents()));
                }
                localWorkbook.close();
            } catch (Exception localException) {
            }

            // 获取完之后更新UI界面
            runOnUiThread(() -> {
                Collections.sort(universitiesModels);
                schoolAdapter = new SchoolAdapter(this, universitiesModels);
                listView.setDivider(null);
                listView.setAdapter(schoolAdapter);
                loadingProgress.setVisibility(View.GONE);
            });
        }).start();
```
#### 4、最后就是模糊搜索数据，从xls获取到的数据 List 去对比找学校名称,return results得到的就是从List 索引到的全部数据。
```
 /**
     * 模糊搜索数据
     */
    public List search(String name, List list) {
        List results = new ArrayList();
        Pattern pattern = Pattern.compile(name);
        for (int i = 0; i < list.size(); i++) {
            Matcher matcher = pattern.matcher(universitiesModels.get(i).getUniversitiesName());
            if (matcher.find()) {
                results.add(list.get(i));
            }
        }
        searchModels.addAll(results);
        return results;
    }
```
![2781551-f53b80242b368757](https://user-images.githubusercontent.com/13359093/209096503-85ae2529-70a8-4c11-bc3f-f686547f66fe.png)

![2781551-5faa56285dc9a842](https://user-images.githubusercontent.com/13359093/209096540-9bb0534b-756c-4aad-8fb8-e049e967bb10.png)
###### 最后就完成从xls 表格中读取数据，按照字母排序显示和搜索显示。
