package com.example.ayeshashahid.course;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView course_list;
    String course_image;
    MyCustomAdapter dataAdapter = null,dataAdapter1 = null,featuredataAdapter=null,pricedataAdapter=null;
    ArrayList<Book> countryList;

    private static final String category_url ="http://192.168.1.24/CourseService/WebService1.asmx/Filter_list";
    private static final String course_list_url = "http://192.168.1.24/course_service/Course_service.asmx/Course_list";
    private ArrayList<Book> books,languageList;
    private ArrayList<Book> category,subcategory;
    private ArrayAdapter<Book> adapter;
    Spinner mySpinner, subcat,features,price;
    JSONObject jsonobject,result;
    JSONArray jsonarray,jArray;
    String cat_id,subcat_id;
   ArrayList<String> levelid, langid,priceid;
    int flag;
    ArrayList<String> categoriesList, subcategoriesList;
    ArrayAdapter adapter1;
    ArrayList<Book> feature=new ArrayList<Book>();
    ArrayList<Book> pric=new ArrayList<Book>();

    JSONObject jsnobject ;
    JSONObject   jsonobjec ;
    String j,d,lad,led;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        course_list = (ListView) findViewById(R.id.list);

        Book feat = new Book("0","Quizes",false);
        feature.add(feat);
        feat = new Book("1","Assignments",false);
        feature.add(feat);
        feat = new Book("2","Features",false);
        feature.add(feat);
        Book pri = new Book("0","Free",false);
        pric.add(pri);
        pri = new Book("1","Paid",false);
        pric.add(pri);
        pri = new Book("2","Price",false);
        pric.add(pri);

        mySpinner = (Spinner) findViewById(R.id.categories);
        features = (Spinner) findViewById(R.id.features);
        featuredataAdapter = new MyCustomAdapter(MainActivity.this,
                R.layout.spinner_item, feature);
        features.setAdapter(featuredataAdapter);
        features.setSelection(feature.size() - 1);
        price = (Spinner) findViewById(R.id.price);
        pricedataAdapter = new MyCustomAdapter(MainActivity.this,
                R.layout.spinner_item, pric);
        // Assign adapter to ListView
        price.setAdapter(pricedataAdapter);
        price.setSelection(pric.size() - 1);
        categoriesList = new ArrayList<String>();
        categoriesList.add("--Category--");
        subcat = (Spinner) findViewById(R.id.sub_categories);
        // Create an array to populate the spinner
        subcategoriesList = new ArrayList<String>();
        subcategoriesList.add("--SubCategory--");
        new DownloadJSON().execute();

        books = new ArrayList<Book>();

        new DownloadCourseList().execute();

    }

    private class DownloadCourseList extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {


            // JSON file URL address
            result = JSONfunctions.getJSONfromURL(course_list_url);
            Log.d("jsonobject", String.valueOf(result));
            try {
                jArray = result.getJSONArray("course_info");
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    Book book = new Book();
                    book.setName(jObject.getString("title"));
                    book.setCourse_category(jObject.getString("category"));
                    if (jObject.getString("tuition").equals("True")) {
                        book.setTutionfee("$" + jObject.getString("tuitionfee"));
                    } else
                        book.setTutionfee("Free");

                    if (jObject.getString("coursebanner").contains("data:image/jpeg;base64,"))
                        course_image = jObject.getString("coursebanner").replace("data:image/jpeg;base64,", "");

                    else
                        course_image = jObject.getString("coursebanner").replace("data:image/gif;base64,", "");

//      else
//                        course_image = jObject.getString("coursebanner").replace("data:image/png;base64,", "");
                    Log.d("image", course_image);

                    book.setImageUrl(course_image);
                    book.setAuthorName(jObject.getString("authorName"));

                    books.add(book);



                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void args) {

            setadapter(books);
        }
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array to populate the spinner
            category = new ArrayList<Book>();

            Book cate = new Book();
            cate.setCategoryid("0");
            cate.setCourse_category("--Category--");
            category.add(cate);

            countryList = new ArrayList<Book>();
            Book   levels;
            languageList = new ArrayList<Book>();
            Book   language;

            // JSON file URL address
            jsonobject = JSONfunctions.getJSONfromURL(category_url);
Log.d("jsonobject", String.valueOf(jsonobject));
            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("categories");
                JSONArray levelarray = jsonobject.getJSONArray("levels");
                JSONArray langarray = jsonobject.getJSONArray("languages");



                for (int i = 0; i < jsonarray.length(); i++) {
                 JSONObject   jsonobjec = jsonarray.getJSONObject(i);

                    cate = new Book();
                    // int a=jsonobject.optInt("CategoryID")-1;
                    cate.setCategoryid(jsonobjec.optString("categoryid"));
                    cate.setCourse_category(jsonobjec.optString("name"));

                    category.add(cate);

                    // Populate spinner with country names
                    categoriesList.add(jsonobjec.optString("name"));


                }
                for (int i = 0; i < levelarray.length(); i++) {
                    JSONObject levelobj = levelarray.getJSONObject(i);
                    levels = new Book(levelobj.optString("levelid"), levelobj.optString("levelname"), false);
                    countryList.add(levels);
                }
                levels = new Book("0","Levels",false);
                countryList.add(levels);

                for (int i = 0; i < langarray.length(); i++) {
                  JSONObject languaarray = langarray.getJSONObject(i);
                    language = new Book(languaarray.optString("languageid"),languaarray.optString("name"),false);
                    languageList.add(language);


                }
                language = new Book("0","Languages",false);
                languageList.add(language);

                Log.d("categories", String.valueOf(categoriesList));
            } catch (Exception e) {
                String err = (e.getMessage() == null) ? "Connection failed" : e.getMessage();
                Log.e("connection-err2:", err);
                //   Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categoriesList){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mySpinner.setAdapter(adapter);
            adapter();


            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    Log.d("cat2", categoriesList.get(position));
                    Log.d("cat12", category.get(position).getCategoryId());

                   // Toast.makeText(arg0.getContext(), "Selected: " + category.get(position).getCategoryId(), Toast.LENGTH_LONG).show();
                    if(!category.get(position).getCategoryId().equals("0")) {
                        cat_id = category.get(position).getCategoryId();
Log.d("error",cat_id);
                        Book subcat = new Book(cat_id);
                        flag=0;
                       new DownloadJSONSub().execute(subcat);
                    filter(flag);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });

           //create an ArrayAdaptar from the String Array
                    dataAdapter = new MyCustomAdapter(MainActivity.this,
                    R.layout.spinner_item, countryList);

            Spinner listView = (Spinner) findViewById(R.id.level);
            //      listView.setPrompt("Select Level");
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);
            listView.setSelection(countryList.size() - 1);


            //create an ArrayAdaptar from the String Array
            dataAdapter1 = new MyCustomAdapter(MainActivity.this,
                    R.layout.spinner_item, languageList);

            Spinner language = (Spinner) findViewById(R.id.language);
            //      listView.setPrompt("Select Level");
            // Assign adapter to ListView
            language.setAdapter(dataAdapter1);
            language.setSelection(languageList.size() - 1);

        }
    }

   private class DownloadJSONSub extends AsyncTask<Book, String, Void> {


        @Override
        protected Void doInBackground(Book... params) {
            subcategory = new ArrayList<Book>();

            Book subcate = new Book();
            subcate.setSubCategoryid("0");
            subcate.setSubCourse_category("--SubCategory--");
            subcategory.add(subcate);


            try {
                Log.d("secjson", String.valueOf(jsonobject));
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("subcategories");
                subcategoriesList = new ArrayList<String>();
                subcategoriesList.add("--SubCategory--");

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject   jsonobjec = jsonarray.getJSONObject(i);
                    if(jsonobjec.optString("categoryid").equals(cat_id))
                    {

                        subcate = new Book();
                    // int a=jsonobject.optInt("CategoryID")-1;
                    subcate.setSubCategoryid(jsonobjec.optString("subcategoryid"));
                    Log.d("subcatidd",jsonobjec.optString("subcategoryid"));
                    subcate.setSubCourse_category(jsonobjec.optString("name"));

                    subcategory.add(subcate);

                        subcategoriesList.add(jsonobjec.optString("name"));
                    }

                }
              //  Log.d("subcategories", String.valueOf(subcategory));
                Log.d("subcategories1", String.valueOf(subcategoriesList));
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            adapter1.notifyDataSetChanged();
            adapter();
        }
    }

    private void adapter() {
        // Spinner adapter
        adapter1=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                subcategoriesList)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subcat.setAdapter(adapter1);
        subcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0,
                View arg1, int position, long arg3) {
           // TODO Auto-generated method stub
            Log.d("subcat2", subcategoriesList.get(position));
        if(position!=0) {
            Log.d("subcat12", subcategory.get(position).getSubCategoryid());
            Log.d("subcat122", subcategory.get(position).getSubCourse_category());

          //  Toast.makeText(arg0.getContext(), "Selected: " + subcategory.get(position).getSubCategoryid(), Toast.LENGTH_LONG).show();
                subcat_id = subcategory.get(position).getSubCategoryid();
                Log.d("error", subcat_id);
                flag = 1;
                // new DownloadJSONSub().execute(subcat);
                filter(flag);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    });

}

    public void filter(int flag){


        ArrayList<Book> courselist=new ArrayList<Book>();
        ArrayList<String> s=new ArrayList<String>();
        ArrayList<String> m=new ArrayList<String>();
        ArrayList<String> la=new ArrayList<String>();
        ArrayList<String> le=new ArrayList<String>();
        //  Log.d("categoryid",cat_id);
        //Log.d("courselist",String.valueOf(result));
try {


    if(flag==0){
        Log.d("courselist1",String.valueOf(jArray));

        Log.d("flag", String.valueOf(flag));
        if( String.valueOf(jArray).isEmpty()){
            course_list.setVisibility(View.INVISIBLE);

            Toast.makeText(this,"No record found!!",Toast.LENGTH_LONG).show();
        }else{
    for (int i = 0; i < jArray.length(); i++) {
           jsonobjec = jArray.getJSONObject(i);
        if(jsonobjec.optString("CategoryID").equals(cat_id))
        {
            Book book = new Book();
            book.setName(jsonobjec.getString("title"));
            Log.d("name",jsonobjec.getString("title"));
            Log.d("subcatid",jsonobjec.getString("SubCategoryID"));
            book.setCourse_category(jsonobjec.getString("category"));
            if (jsonobjec.getString("tuition").equals("True")) {
                book.setTutionfee("$" + jsonobjec.getString("tuitionfee"));
            } else
                book.setTutionfee("Free");

            if (jsonobjec.getString("coursebanner").contains("data:image/jpeg;base64,"))
                course_image = jsonobjec.getString("coursebanner").replace("data:image/jpeg;base64,", "");

            else
                course_image = jsonobjec.getString("coursebanner").replace("data:image/gif;base64,", "");

//      else
//                        course_image = jObject.getString("coursebanner").replace("data:image/png;base64,", "");
            Log.d("image", course_image);

            book.setImageUrl(course_image);
            book.setAuthorName(jsonobjec.getString("authorName"));

            courselist.add(book);
             //js=new JSONObject(String.valueOf(jsonobjec));
s.add(String.valueOf(jsonobjec));

//            subcategoriesList.add(jsonobjec.optString("name"));
        }
        else
            continue;
    }
     j="{\"remain\":"+s+"}";
     jsnobject = new JSONObject(j);


     if(courselist.isEmpty()){
         course_list.setVisibility(View.INVISIBLE);
        Toast.makeText(this,"No record found!!",Toast.LENGTH_LONG).show();
    }
    else {
        Log.d("courselistvalue", String.valueOf(s));
       // j="{\"remain\":"+s+"}";
        Log.d("j",j);
         course_list.setVisibility(View.VISIBLE);
Log.d("ss", String.valueOf(jsnobject));

        setadapter(courselist);
    }
 }
}


    if(flag==1){

        Log.d("flag", String.valueOf(flag));
        Log.d("ss", String.valueOf(jsnobject));
        if( String.valueOf(jsnobject).isEmpty()){
            course_list.setVisibility(View.INVISIBLE);

            Toast.makeText(this,"No record found!!",Toast.LENGTH_LONG).show();
        }else{
        JSONArray jsubArray = jsnobject.getJSONArray("remain");
        for (int i = 0; i < jsubArray.length(); i++) {
         JSONObject   jsonobje = jsubArray.getJSONObject(i);

            if(jsonobje.optString("SubCategoryID").equals(subcat_id))
            {
                Book book = new Book();
                book.setName(jsonobje.getString("title"));
                Log.d("name",jsonobje.getString("title"));
                Log.d("subcatid",jsonobje.getString("SubCategoryID"));
                book.setCourse_category(jsonobje.getString("category"));
                if (jsonobje.getString("tuition").equals("True")) {
                    book.setTutionfee("$" + jsonobje.getString("tuitionfee"));
                } else
                    book.setTutionfee("Free");

                if (jsonobje.getString("coursebanner").contains("data:image/jpeg;base64,"))
                    course_image = jsonobje.getString("coursebanner").replace("data:image/jpeg;base64,", "");

                else
                    course_image = jsonobje.getString("coursebanner").replace("data:image/gif;base64,", "");

//      else
//                        course_image = jObject.getString("coursebanner").replace("data:image/png;base64,", "");
                Log.d("image", course_image);

                book.setImageUrl(course_image);
                book.setAuthorName(jsonobje.getString("authorName"));

                courselist.add(book);
                //js=new JSONObject(String.valueOf(jsonobjec));
                m.add(String.valueOf(jsonobje));

//            subcategoriesList.add(jsonobjec.optString("name"));
            }
            else
                continue;
        }
       d="{\"remain\":"+m+"}";

        if(courselist.isEmpty()){
            course_list.setVisibility(View.INVISIBLE);

            Toast.makeText(this,"No record found!!",Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("courselistvalue", String.valueOf(s));
            // j="{\"remain\":"+s+"}";
            Log.d("j",j);
            jsnobject = new JSONObject(j);
            course_list.setVisibility(View.VISIBLE);

            Log.d("ss", String.valueOf(jsnobject));

            setadapter(courselist);
        }
    }
    }

    if(flag==2) {
        s=m;
        j=d;
        Log.d("flag", String.valueOf(flag));
        Log.d("ss", String.valueOf(jsnobject));
        Log.d("ss", String.valueOf(levelid));
        JSONArray jsubArray = jsnobject.getJSONArray("remain");

            for (int x = 0; x < levelid.size(); x++) {
            for (int i = 0; i < jsubArray.length(); i++) {
                JSONObject jsonobje = jsubArray.getJSONObject(i);

                if (jsonobje.optString("levelID").equals(levelid.get(x))) {
                    Book book = new Book();
                    book.setName(jsonobje.getString("title"));
                    Log.d("name", jsonobje.getString("title"));
                    Log.d("subcatid", jsonobje.getString("SubCategoryID"));
                    book.setCourse_category(jsonobje.getString("category"));
                    if (jsonobje.getString("tuition").equals("True")) {
                        book.setTutionfee("$" + jsonobje.getString("tuitionfee"));
                    } else
                        book.setTutionfee("Free");

                    if (jsonobje.getString("coursebanner").contains("data:image/jpeg;base64,"))
                        course_image = jsonobje.getString("coursebanner").replace("data:image/jpeg;base64,", "");

                    else
                        course_image = jsonobje.getString("coursebanner").replace("data:image/gif;base64,", "");

//      else
//                        course_image = jObject.getString("coursebanner").replace("data:image/png;base64,", "");
                    Log.d("image", course_image);

                    book.setImageUrl(course_image);
                    book.setAuthorName(jsonobje.getString("authorName"));

                    courselist.add(book);
                    //js=new JSONObject(String.valueOf(jsonobjec));
                     le.add(String.valueOf(jsonobje));

//            subcategoriesList.add(jsonobjec.optString("name"));
                } else
                    continue;
            }
             led="{\"remain\":"+le+"}";

            if (courselist.isEmpty()) {
                course_list.setVisibility(View.INVISIBLE);

                Toast.makeText(this, "No record found!!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("courselistvalue", String.valueOf(s));
                // j="{\"remain\":"+s+"}";
                s=le;
                j=led;
                Log.d("j", j);
                jsnobject = new JSONObject(j);

                Log.d("ss", String.valueOf(jsnobject));
                course_list.setVisibility(View.VISIBLE);

                setadapter(courselist);
            }
        }
    }
 if(flag==3) {

        Log.d("flag", String.valueOf(flag));
        Log.d("ss", String.valueOf(jsnobject));
        Log.d("ss", String.valueOf(langid));
        JSONArray jsubArray = jsnobject.getJSONArray("remain");

            for (int x = 0; x < langid.size(); x++) {
            for (int i = 0; i < jsubArray.length(); i++) {
                JSONObject jsonobje = jsubArray.getJSONObject(i);

                if (jsonobje.optString("lang").equals(langid.get(x))) {
                    Book book = new Book();
                    book.setName(jsonobje.getString("title"));
                    Log.d("name", jsonobje.getString("title"));
                    Log.d("subcatid", jsonobje.getString("SubCategoryID"));
                    book.setCourse_category(jsonobje.getString("category"));
                    if (jsonobje.getString("tuition").equals("True")) {
                        book.setTutionfee("$" + jsonobje.getString("tuitionfee"));
                    } else
                        book.setTutionfee("Free");

                    if (jsonobje.getString("coursebanner").contains("data:image/jpeg;base64,"))
                        course_image = jsonobje.getString("coursebanner").replace("data:image/jpeg;base64,", "");

                    else
                        course_image = jsonobje.getString("coursebanner").replace("data:image/gif;base64,", "");

//      else
//                        course_image = jObject.getString("coursebanner").replace("data:image/png;base64,", "");
                    Log.d("image", course_image);

                    book.setImageUrl(course_image);
                    book.setAuthorName(jsonobje.getString("authorName"));

                    courselist.add(book);
                    //js=new JSONObject(String.valueOf(jsonobjec));
                        la.add(String.valueOf(jsonobje));

//            subcategoriesList.add(jsonobjec.optString("name"));
                } else
                    continue;
            }
             lad="{\"remain\":"+la+"}";

            if (courselist.isEmpty()) {
                course_list.setVisibility(View.INVISIBLE);

                Toast.makeText(this, "No record found!!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("courselistvalue", String.valueOf(s));
                // j="{\"remain\":"+s+"}";
              s=la;
                j=lad;

                Log.d("j", j);
                jsnobject = new JSONObject(j);
                course_list.setVisibility(View.VISIBLE);

                Log.d("ss", String.valueOf(jsnobject));

                setadapter(courselist);
            }
        }
    }
if(flag==4) {

        Log.d("flag", String.valueOf(flag));
        Log.d("ss", String.valueOf(jsnobject));
        Log.d("ss", String.valueOf(priceid));
        JSONArray jsubArray = jsnobject.getJSONArray("remain");

            for (int x = 0; x < priceid.size(); x++) {
            for (int i = 0; i < jsubArray.length(); i++) {
                JSONObject jsonobje = jsubArray.getJSONObject(i);

                if (jsonobje.optString("tuition").equals(priceid.get(x))) {
                    Book book = new Book();
                    book.setName(jsonobje.getString("title"));
                    Log.d("name", jsonobje.getString("title"));
                    Log.d("subcatid", jsonobje.getString("SubCategoryID"));
                    book.setCourse_category(jsonobje.getString("category"));
                    if (jsonobje.getString("tuition").equals("True")) {
                        book.setTutionfee("$" + jsonobje.getString("tuitionfee"));
                    } else
                        book.setTutionfee("Free");

                    if (jsonobje.getString("coursebanner").contains("data:image/jpeg;base64,"))
                        course_image = jsonobje.getString("coursebanner").replace("data:image/jpeg;base64,", "");

                    else
                        course_image = jsonobje.getString("coursebanner").replace("data:image/gif;base64,", "");

//      else
//                        course_image = jObject.getString("coursebanner").replace("data:image/png;base64,", "");
                    Log.d("image", course_image);

                    book.setImageUrl(course_image);
                    book.setAuthorName(jsonobje.getString("authorName"));

                    courselist.add(book);
                    //js=new JSONObject(String.valueOf(jsonobjec));
                    //    s.add(String.valueOf(jsonobje));

//            subcategoriesList.add(jsonobjec.optString("name"));
                } else
                    continue;
            }
            // j="{\"remain\":"+s+"}";

            if (courselist.isEmpty()) {
                course_list.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No record found!!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("courselistvalue", String.valueOf(s));
                // j="{\"remain\":"+s+"}";
                Log.d("j", j);
                jsnobject = new JSONObject(j);

                Log.d("ss", String.valueOf(jsnobject));
                course_list.setVisibility(View.VISIBLE);

                setadapter(courselist);
            }
        }
    }




} catch (JSONException e) {
    e.printStackTrace();
}


    }


    public  void setadapter(ArrayList<Book> books){

        adapter = new CustomListViewAdapter(MainActivity.this, R.layout.list_item, books);
        course_list.setAdapter(adapter);
    }

    private class MyCustomAdapter extends ArrayAdapter<Book> {

        private ArrayList<Book> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Book> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Book>();
            this.countryList.addAll(countryList);
        }


        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public int getCount() {
            return super.getCount() - 1; // This makes the trick: do not show last item
        }

        @Override
        public Book getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        public View getCustomView(final int position, View convertView,
                                  ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.spinner_item, null);

                holder = new ViewHolder();
                ///  holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Book country = (Book) cb.getTag();
//                        Toast.makeText(getApplicationContext(),
//                                "Clicked on Checkbox: " + cb.getText() +
//                                        " is " + cb.isChecked(),
//                                Toast.LENGTH_LONG).show();
                        country.setSelected(cb.isChecked());
                        checkButtonClick();

                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Book country = countryList.get(position);
            if (country.getLevelName().equals("Levels")){
                holder.name.setEnabled(false);
            }
            if (country.getLevelName().equals("Languages")){
                holder.name.setEnabled(false);
            }
            if (country.getLevelName().equals("Features")){
                holder.name.setEnabled(false);
            }
            if (country.getLevelName().equals("Price")){
                holder.name.setEnabled(false);
            }

            //     holder.code.setText(" (" +  country.getCode() + ")");
            holder.name.setText(country.getLevelName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }

    private void checkButtonClick() {
//
        StringBuffer responseText = new StringBuffer();
        responseText.append("The following were selected...\n");
levelid=new ArrayList<String>();
        priceid=new ArrayList<String>();
        langid=new ArrayList<String>();
        ArrayList<Book> countryList = dataAdapter.countryList;
        for (int i = 0; i < countryList.size(); i++) {
            Book country = countryList.get(i);
            if (country.isSelected()) {
                responseText.append("\n" + country.getLevelName()+country.getLevelId());
                flag=2;
                levelid.add(country.getLevelId());


            }
        }


//
        StringBuffer responseText1 = new StringBuffer();
        responseText1.append("The following were selected11...\n");

        ArrayList<Book> language = dataAdapter1.countryList;
        for (int i = 0; i < language.size(); i++) {
            Book langu = language.get(i);
            if (langu.isSelected()) {
                responseText1.append("\n" + langu.getLevelName());
                flag = 3;
                langid.add(langu.getLevelId());

            }
        }

            StringBuffer responseText2 = new StringBuffer();
            responseText2.append("The following were selected12...\n");

            ArrayList<Book> features = featuredataAdapter.countryList;
            for (int i = 0; i < features.size(); i++) {
                Book feat = features.get(i);
                if (feat.isSelected()) {
                    responseText2.append("\n" + feat.getLevelName());
                }
            }
            StringBuffer responseText3 = new StringBuffer();
            responseText3.append("The following were selected12...\n");
String pricestring;
            ArrayList<Book> price = pricedataAdapter.countryList;
            for (int i = 0; i < features.size(); i++) {
                Book pric = price.get(i);
                if (pric.isSelected()) {
                    responseText3.append("\n" + pric.getLevelName());
                    flag = 4;
                    if(pric.getLevelId().equals("0"))
                        pricestring="False";
                    else
                        pricestring="True";
                    priceid.add(pricestring);
                }
            }
        //    Log.d("levels", String.valueOf(responseText));
           // Log.d("lang", String.valueOf(responseText1));
            Log.d("feat", String.valueOf(responseText));
            Log.d("feat", String.valueOf(responseText3));
            Log.d("feat", String.valueOf(priceid));
            Log.d("feat", String.valueOf(levelid));
//            Toast.makeText(getApplicationContext(),
//                    responseText, Toast.LENGTH_LONG).show();

        filter(flag);
        }



}
