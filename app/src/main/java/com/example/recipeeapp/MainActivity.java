package com.example.recipeeapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<FoodData> myFoodList;
    FoodData mFoodData;
    ProgressDialog progressDialog;
    MyAdapter myAdapter;
    EditText txt_Search;
    FloatingActionButton floatingActionButton;
    String fileName;

    String pincode;

    ImageCarousel imageCarousel;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        txt_Search = (EditText)findViewById(R.id.txt_searchtext);
        floatingActionButton=findViewById(R.id.fb);

        imageCarousel=findViewById(R.id.carousel);


        imageCarousel.addData(new CarouselItem("https://cdn.images.express.co.uk/img/dynamic/11/590x/Headaches-832900.jpg",""));
        imageCarousel.addData(new CarouselItem("https://www.nps.org.au/assets/_750x468_crop_center-center_75_none/GettyImages-1134788257.jpg",""));
        imageCarousel.addData(new CarouselItem("https://storage.googleapis.com/afs-prod/media/467bd9672e684af0b63c1707edebe3f9/3000.jpeg",""));
        imageCarousel.addData(new CarouselItem("https://i.insider.com/5fc9146650e71a0011558b12?width=700",""));
        imageCarousel.addData(new CarouselItem("https://www.nps.org.au/assets/_750x468_crop_center-center_75_none/GettyImages-511932886.jpg",""));
        imageCarousel.addData(new CarouselItem("https://images.theconversation.com/files/470376/original/file-20220622-11-ijs4h6.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=320&h=213&fit=crop",""));
        imageCarousel.addData(new CarouselItem("https://cdn.images.express.co.uk/img/dynamic/11/590x/high-blood-pressure-paracetamol-ibuprofen-risk-medication-raising-hypertension-painkillers-1599282.jpg?r=1650566705062",""));
        imageCarousel.addData(new CarouselItem("https://www.news-medical.net/image.axd?picture=2019%2F11%2Fshutterstock_1185781648.jpg",""));
        imageCarousel.addData(new CarouselItem("https://static.independent.co.uk/2021/04/07/15/PA-25401997.jpg?quality=75&width=982&height=726&auto=webp",""));
        imageCarousel.addData(new CarouselItem("https://www.aetnainternational.com/common/images/dam/people-nw/shutterstock_717437125.jpg/_jcr_content/renditions/767.350.jpg",""));



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items ....");


        myFoodList = new ArrayList<>();

        myAdapter  = new MyAdapter(MainActivity.this,myFoodList);
        mRecyclerView.setAdapter(myAdapter);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        progressDialog.show();
        ValueEventListener eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myFoodList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                    FoodData foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());
                    myFoodList.add(foodData);

                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        txt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });



    }
    private void filter(String text) {

        ArrayList<FoodData> filterList = new ArrayList<>();

        for(FoodData item: myFoodList){

            if(item.getItemName().toLowerCase().contains(text.toLowerCase())){

                filterList.add(item);

            }

        }

        myAdapter.filteredList(filterList);

    }
    public void btn_uploadActivity(View view) {


        final AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
        View mview=getLayoutInflater().inflate(R.layout.dialog,null);

        EditText editText=mview.findViewById(R.id.editText);
        Button button=mview.findViewById(R.id.button);


        alert.setView(mview);
        AlertDialog alertDialog= alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat format=new SimpleDateFormat("HHmm", Locale.CANADA);
                Date now =new Date();
                fileName= format.format(now);

                pincode=editText.getText().toString();
                if (pincode.equals(fileName)){

                    startActivity(new Intent(MainActivity.this,Upload_Recipe.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
        alertDialog.show();

/*
        startActivity(new Intent(this,Upload_Recipe.class));
*/
    }
}
