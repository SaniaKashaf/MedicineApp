package com.example.recipeeapp;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    TextView foodDescription,RecipeName,RecipePrice;
    ImageView foodImage;
    String key="";
    String imageUrl="";

    String fileName;

    String pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        RecipeName = (TextView) findViewById(R.id.txtRecipeName);
        RecipePrice = (TextView) findViewById(R.id.txtPrice);
        foodDescription = (TextView)findViewById(R.id.txtDescription);
        foodImage = (ImageView)findViewById(R.id.ivImage2);

        Bundle mBundle = getIntent().getExtras();

        if(mBundle!=null){

            foodDescription.setText(mBundle.getString("Description"));
            key = mBundle.getString("keyValue");
            imageUrl = mBundle.getString("Image");
            RecipeName.setText(mBundle.getString("RecipeName"));
            RecipePrice.setText(mBundle.getString("price"));
           // foodImage.setImageResource(mBundle.getInt("Image"));

            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(foodImage);


        }

    }

    public void btnDeleteRecipe(View view) {

        final AlertDialog.Builder alert=new AlertDialog.Builder(DetailActivity.this);
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

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipe");
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            reference.child(key).removeValue();
                            Toast.makeText(DetailActivity.this, "Recipe Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                        }
                    });

/*
                    startActivity(new Intent(DetailActivity.this,Upload_Recipe.class));
*/
                }
                else {
                    Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
        alertDialog.show();



        ///////////////////////



    }

    public void btnUpdateRecipe(View view) {

        final AlertDialog.Builder alert=new AlertDialog.Builder(DetailActivity.this);
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
                    startActivity(new Intent(getApplicationContext(),UpdateRecipeActivity.class)
                            .putExtra("recipeNameKey",RecipeName.getText().toString())
                            .putExtra("descriptionKey",foodDescription.getText().toString())
                            .putExtra("priceKey",RecipePrice.getText().toString())
                            .putExtra("oldimageUrl",imageUrl)
                            .putExtra("key",key)
                    );
/*
                    startActivity(new Intent(DetailActivity.this,Upload_Recipe.class));
*/
                }
                else {
                    Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
        alertDialog.show();







    }
}
